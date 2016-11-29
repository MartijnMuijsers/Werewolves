package codingpro.werewolves.server;

import lombok.Getter;

public enum Command {
	
	NON_EXISTENT,
	CREATE("create", "Creates a new game"),
	START("start <code>", "Starts a game"),
	STOP("stop <code>", "Stops a game"),
	REMOVE("remove <code>", "Removes a game"),
	HELP("help", "See a list of all commands available"),
	WHO("who", "See a list of players and their info"),
	WHERE("where", "See a list of games and their info"),
	CARDS("cards <code>", "See all dealt cards in a game"),
	EXIT("exit", "Stop the server"),
	SETCARDS("setcards <code>", "Load the cards from the cards file into a game"),
	SYNCCARDS("synccards <code>", "Keep loading the cards from the cards file into a game"),
	ECHO("echo", "Echoes", false),
	WHY("why", "Answers an existential question", false);
	
	@Getter
	private final String syntax;
	@Getter
	private final String description;
	@Getter
	private final boolean isListed;
	
	private Command() {
		this(null, null, false);
	}
	
	private Command(String syntax, String description) {
		this(syntax, description, true);
	}
	
	private Command(String syntax, String description, boolean isListed) {
		this.syntax = syntax;
		this.description = description;
		this.isListed = isListed;
	}
	
	public void logSyntax() {
		Log.response("Command syntax: " + syntax);
	}
	
	public void logHelp() {
		Log.response("Command: " + syntax + " - " + description);
	}
	
	public void logHelpInList() {
		Log.response(syntax + " - " + description);
	}
	
	public void logSyntaxError() {
		Log.response("Syntax error!");
		logHelp();
	}
	
	public boolean logIfCodeInvalid(GameCode code) {
		if (code == null) {
			Log.response("The given code is invalid.");
			logHelp();
			return true;
		}
		return false;
	}
	
	public boolean logIfCodeNotInUse(GameCode code) {
		if (!code.isInUse()) {
			Log.response("The given code is not in use.");
			logHelp();
			return true;
		}
		return false;
	}
	
	public boolean logIfCodeInUse(GameCode code) {
		if (code.isInUse()) {
			Log.response("The given code is in use.");
			logHelp();
			return true;
		}
		return false;
	}
	
	public boolean logIfCodeInvalidOrNotInUse(GameCode code) {
		if (logIfCodeInvalid(code)) {
			return true;
		}
		if (logIfCodeNotInUse(code)) {
			return true;
		}
		return false;
	}
	
	public boolean logIfCodeInvalidOrInUse(GameCode code) {
		if (logIfCodeInvalid(code)) {
			return true;
		}
		if (logIfCodeInUse(code)) {
			return true;
		}
		return false;
	}
	
	public boolean logIfGameAlreadyStarted(Game game) {
		if (game.hasStarted()) {
			Log.response("That game has already started.");
			logHelp();
			return true;
		}
		return false;
	}
	
	public boolean logIfGameHasTooManyUsernamesToStart(Game game) {
		if (game.hasTooManyUsernamesToStart()) {
			Log.response("That game has more usernames than cards.");
			logHelp();
			return true;
		}
		return false;
	}
	
	public boolean logIfGameNotStartedYet(Game game) {
		if (!game.hasStarted()) {
			Log.response("That game has not started yet.");
			logHelp();
			return true;
		}
		return false;
	}
	
}
