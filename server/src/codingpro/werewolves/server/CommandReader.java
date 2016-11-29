package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandReader implements Runnable {
	
	@Override
	public void run() {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String line = r.readLine();
				if (line.trim().isEmpty()) {
					continue;
				}
				StringTokenizer s = new StringTokenizer(line);
				String commandString = s.nextToken();
				Command command;
				try {
					command = Command.valueOf(commandString.toUpperCase());
				} catch (Exception e) {
					command = Command.NON_EXISTENT;
				}
				switch (command) {
				case CREATE:
				{
					Game game = null;
					List<RoleCard> cards = new ArrayList<>();
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrInUse(code)) {
							try {
								game = Game.createGame(code, cards);
							} catch (GameCodeAlreadyInUseException e) {
								// Cannot happen
								e.printStackTrace();
							}
						}
					} else {
						game = Game.createGame(cards);
					}
					if (game != null) {
						Log.response("A game with code " + game.getCode() + " has been created");
					}
				}
				break;
				case START:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (!command.logIfGameAlreadyStarted(game)) {
								if (!command.logIfGameHasTooManyUsernamesToStart(game)) {
									try {
										game.start();
									} catch (GameAlreadyStartedException e) {
										// Cannot happen
										e.printStackTrace();
									} catch (TooManyUsernamesException e) {
										// Cannot happen
										e.printStackTrace();
									}
									Log.response("Game " + code + " has been started!");
									game.logDealtCards();
								}
							}
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case STOP:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (!command.logIfGameNotStartedYet(game)) {
								try {
									game.stop();
								} catch (GameNotStartedYetException e) {
									// Cannot happen
									e.printStackTrace();
								}
								Log.response("Game " + code + " has been stopped!");
							}
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case REMOVE:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (game.hasStarted()) {
								try {
									game.stop();
								} catch (GameNotStartedYetException e) {
									// Cannot happen
									e.printStackTrace();
								}
								Log.response("Game " + code + " has been stopped!");
							}
							game.remove();
							Log.response("Game " + code + " has been removed!");
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case HELP:
				{
					Log.response("--- Command overview ---");
					for (Command availableCommand : Command.values()) {
						if (availableCommand.isListed()) {
							availableCommand.logHelpInList();
						}
					}
				}
				break;
				case WHO:
				{
					Player.logOverview();
				}
				break;
				case WHERE:
				{
					Game.logOverview();
				}
				break;
				case CARDS:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (!command.logIfGameNotStartedYet(game)) {
								game.logDealtCards();
							}
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case EXIT:
				{
					WerewolvesServer.exit();
				}
				break;
				case SETCARDS:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (game.hasStarted()) {
								Log.response("Warning: the game " + code + " has already been started!");
							}
							game.loadCards();
							Log.response("Game " + code + " now has the cards from file!");
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case SYNCCARDS:
				{
					if (s.hasMoreTokens()) {
						GameCode code = GameCode.getByText(s.nextToken());
						if (!command.logIfCodeInvalidOrNotInUse(code)) {
							Game game = Game.getByCode(code);
							if (game.hasStarted()) {
								Log.response("Warning: the game " + code + " has already been started!");
							}
							game.syncCards();
							Log.response("Game " + code + " will now sync with the cards from file!");
						}
					} else {
						command.logSyntaxError();
					}
				}
				break;
				case ECHO:
				{
					Log.response("Echo!");
				}
				break;
				case WHY:
				{
					Log.response("42");
				}
				break;
				default:
				{
					Log.response("Unknown command!");
					Log.response("Use the help command to see a list of all commands available");
				}
				break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
