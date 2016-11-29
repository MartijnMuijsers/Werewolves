package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.Setter;

public class Game {
	
	public static final String cardsFilePath = "cards_to_sync.txt";
	
	private final int id;
	@Getter
	private final GameCode code;
	private Map<Username, RoleCard> dealtCards = null;
	private List<RoleCard> undealtCards = null;
	@Getter @Setter
	private List<RoleCard> cards;
	
	private Game(int id, GameCode code, List<RoleCard> cards) {
		IOUtils.createFileIfNotExists(cardsFilePath);
		this.id = id;
		this.code = code;
		this.cards = cards;
		ALL.add(this);
		BY_ID.put(id, this);
		BY_CODE.put(code, this);
	}
	
	public int getID() {
		return id;
	}
	
	/**
	 * @return null if the game has not started yet
	 */
	public Map<Username, RoleCard> getDealtCards() {
		return dealtCards;
	}
	
	/**
	 * @return null if the game has not started yet
	 */
	public List<RoleCard> getUndealtCards() {
		return undealtCards;
	}
	
	public void logDealtCards() {
		Log.response("--- Dealt cards in game " + getCode() + " ---");
		for (Entry<Username, RoleCard> entry : getDealtCards().entrySet()) {
			Log.response(entry.getKey()+" : " + entry.getValue().getRole().getEnglishName());
		}
		Log.response("--- Undealt cards in game " + getCode() + " ---");
		for (RoleCard undealtCard : getUndealtCards()) {
			Log.response(undealtCard.getRole().getEnglishName());
		}
	}
	
	/**
	 * @throws GameNotStartedYetException if this game has not been started yet
	 * @throws UsernameNotInGameException if the given username is not in this game
	 */
	public RoleCard getDealtCard(Username username) throws GameNotStartedYetException, UsernameNotInGameException {
		if (!hasStarted()) {
			throw new GameNotStartedYetException(this);
		}
		if (!hasUsername(username)) {
			throw new UsernameNotInGameException(username, this);
		}
		return dealtCards.get(username);
	}
	
	public void loadCards() {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(cardsFilePath)));
			synchronized (cards) {
				cards.clear();
				while (true) {
					String line = r.readLine();
					if (line == null) {
						break;
					}
					if (!line.isEmpty()) {
						Role role = Role.getFromInputText(line);
						if (role != null) {
							cards.add(new RoleCard(role));
						} else {
							Log.warning("Could not understand line from cards file: " + line);
						}
					}
				}
			}
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void syncCards() {
		new Thread(() -> {
			while (true) {
				if (hasBeenRemoved()) {
					break;
				}
				loadCards();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public Set<Username> getUsernames() {
		if (hasStarted()) {
			return dealtCards.keySet();
		}
		Set<Username> usernames = new HashSet<>();
		for (Player player : getPlayers()) {
			usernames.add(player.getUsername());
		}
		return usernames;
	}
	
	public boolean hasTooManyUsernamesToStart() {
		return getUsernames().size() > cards.size();
	}
	
	/**
	 * @throws GameAlreadyStartedException if this game had already started
	 * @throws TooManyUsernamesException if this game has more usernames than cards
	 */
	public void start() throws GameAlreadyStartedException, TooManyUsernamesException {
		if (hasStarted()) {
			throw new GameAlreadyStartedException(this);
		}
		if (hasTooManyUsernamesToStart()) {
			throw new TooManyUsernamesException(this);
		}
		synchronized (cards) {
			Collections.shuffle(cards);
			Set<Username> usernames = getUsernames();
			dealtCards = new HashMap<>();
			Iterator<RoleCard> cardIterator = cards.iterator();
			for (Username username : usernames) {
				RoleCard card = cardIterator.next();
				dealtCards.put(username, card);
			}
			undealtCards = new ArrayList<>();
			while (cardIterator.hasNext()) {
				undealtCards.add(cardIterator.next());
			}
		}
		for (Player player : getPlayers()) {
			try {
				new CardDealtMessage(player, this).send(player);
			} catch (GameNotStartedYetException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (UsernameNotInGameException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @throws GameNotStartedYetException if this game had not been started yet
	 */
	public void stop() throws GameNotStartedYetException {
		if (!hasStarted()) {
			throw new GameNotStartedYetException(this);
		}
		dealtCards = null;
		undealtCards = null;
		new GameStoppedMessage().sendToAllPlayers(this);
		new UpdatePlayerListMessage(this).sendToAllPlayers(this);
	}
	
	public boolean hasStarted() {
		return getDealtCards() != null;
	}
	
	public boolean hasPlayer(Player player) {
		return player.getGame() == this;
	}
	
	public boolean hasUsername(Username username) {
		if (hasStarted()) {
			return dealtCards.containsKey(username);
		}
		for (Player player : getPlayers()) {
			if (player.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public Iterable<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		for (Player player : Player.getAll()) {
			if (hasPlayer(player)) {
				players.add(player);
			}
		}
		return players;
	}
	
	/**
	 * @throws PlayerIsAlreadyInGameException when the player is already in a game
	 * @throws PlayerHasNoUsernameException when the player has no username
	 * @throws UsernameNotInGameException when this game has already started and the player's username is not in this game
	 * @throws IncorrectPlayerPhaseException when the player is not in the correct phase
	 */
	public void playerJoins(Player player) throws PlayerIsAlreadyInGameException, PlayerHasNoUsernameException, UsernameNotInGameException, IncorrectPlayerPhaseException {
		if (player.hasGame()) {
			throw new PlayerIsAlreadyInGameException(player);
		}
		if (!player.hasUsername()) {
			throw new PlayerHasNoUsernameException(player);
		}
		if (hasStarted() && !hasUsername(player.getUsername())) {
			throw new UsernameNotInGameException(player.getUsername(), this);
		}
		player.joinGame(this);
		if (!hasStarted()) {
			new UpdatePlayerListMessage(this).sendToAllPlayers(this);
		} else {
			try {
				new CardDealtMessage(player, this).send(player);
			} catch (GameNotStartedYetException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @throws PlayerNotInGameException when the player is not in this game
	 * @throws IncorrectPlayerPhaseException when the player is not in the correct phase
	 */
	public void playerLeaves(Player player) throws PlayerNotInGameException, IncorrectPlayerPhaseException {
		if (!hasPlayer(player)) {
			throw new PlayerNotInGameException(player);
		}
		player.leaveGame();
		if (!hasStarted()) {
			new UpdatePlayerListMessage(this).sendToAllPlayers(this);
		}
	}
	
	private static List<Game> ALL = new ArrayList<>();
	private static Map<Integer, Game> BY_ID = new HashMap<>();
	private static Map<GameCode, Game> BY_CODE = new HashMap<>();
	
	public static Iterable<Game> getAll() {
		return ALL;
	}
	
	/**
	 * @return null if no game with the given id exists
	 */
	public static Game getByID(int id) {
		return BY_ID.get(id);
	}
	
	/**
	 * @return null if no game with the given code exists
	 */
	public static Game getByCode(GameCode code) {
		if (code == null) {
			return null;
		}
		return BY_CODE.get(code);
	}
	
	public static void logOverview() {
		Log.response("--- Game overview ---");
		for (Game game : getAll()) {
			Log.response(game.getID() + " / " + game.getCode() + " : " + (game.hasStarted()?"started":"not started") + " \\ " + game.getUsernames());
		}
	}
	
	public void remove() {
		ALL.remove(this);
		BY_ID.remove(id);
		BY_CODE.remove(code);
		new GameCodeRemovedMessage().sendToAllPlayers(this);
		for (Player player : getPlayers()) {
			try {
				playerLeaves(player);
			} catch (PlayerNotInGameException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasBeenRemoved() {
		return !ALL.contains(this);
	}
	
	private static int lastID = -1;
	
	/**
	 * @throws GameCodeAlreadyInUseException if the given code is already in use
	 */
	public static Game createGame(GameCode code, List<RoleCard> cards) throws GameCodeAlreadyInUseException {
		if (code.isInUse()) {
			throw new GameCodeAlreadyInUseException(code);
		}
		lastID++;
		return new Game(lastID, code, cards);
	}
	
	public static Game createGame(List<RoleCard> cards) {
		try {
			return createGame(GameCode.generateNotInUse(), cards);
		} catch (GameCodeAlreadyInUseException e) {
			// Cannot happen
			e.printStackTrace();
			return null;
		}
	}
	
}
