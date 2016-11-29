package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;

import lombok.Getter;

public class Player {
	
	private final int id;
	@Getter
	private final WebSocket socket;
	private Username username = null;
	private Game game = null;
	@Getter
	private PlayerPhase phase;
	
	private Player(int id, WebSocket socket) {
		this.id = id;
		this.socket = socket;
		ALL.add(this);
		BY_ID.put(id, this);
		BY_SOCKET.put(socket, this);
		setPhase(PlayerPhase.CHOOSING_USERNAME);
	}
	
	public int getID() {
		return id;
	}
	
	/**
	 * @return null if this player has not chosen a username
	 */
	public Username getUsername() {
		return username;
	}
	
	/**
	 * Should only be called from SetUsernameMessage
	 * @throws IncorrectPlayerPhaseException if this player is not in the correct phase
	 */
	public void setChosenUsername(Username username) throws IncorrectPlayerPhaseException {
		if (phase != PlayerPhase.CHOOSING_USERNAME) {
			throw new IncorrectPlayerPhaseException(this);
		}
		setPhase(PlayerPhase.CHOOSING_GAME);
		this.username = username;
	}
	
	/**
	 * Should only be called from RemoveUsernameMessage
	 * @throws IncorrectPlayerPhaseException if this player is not in the correct phase
	 */
	public void removeUsername() throws IncorrectPlayerPhaseException {
		if (phase != PlayerPhase.CHOOSING_GAME) {
			throw new IncorrectPlayerPhaseException(this);
		}
		setPhase(PlayerPhase.CHOOSING_USERNAME);
		username = null;
	}
	
	public boolean hasUsername() {
		return getUsername() != null;
	}
	
	/**
	 * @return null if this player is not in a game
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * Should only be called from Game
	 * @throws IncorrectPlayerPhaseException if this player is not in the correct phase
	 */
	public void joinGame(Game game) throws IncorrectPlayerPhaseException {
		if (phase != PlayerPhase.CHOOSING_GAME) {
			throw new IncorrectPlayerPhaseException(this);
		}
		setPhase(PlayerPhase.IN_GAME);
		this.game = game;
		Log.info("A player named " + username + " has joined the game " + game.getCode());
	}
	
	/**
	 * Should only be called from Game
	 * @throws IncorrectPlayerPhaseException if this player is not in the correct phase
	 */
	public void leaveGame() throws IncorrectPlayerPhaseException {
		if (phase != PlayerPhase.IN_GAME) {
			throw new IncorrectPlayerPhaseException(this);
		}
		setPhase(PlayerPhase.CHOOSING_GAME);
		game = null;
	}
	
	public boolean hasGame() {
		return getGame() != null;
	}
	
	private void setPhase(PlayerPhase phase) {
		this.phase = phase;
		new ChangePhaseMessage(phase).send(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			return other.id == id;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		return ""+id;
	}
	
	private static List<Player> ALL = new ArrayList<>();
	private static Map<Integer, Player> BY_ID = new HashMap<>();
	private static Map<WebSocket, Player> BY_SOCKET = new HashMap<>();
	
	public static Iterable<Player> getAll() {
		return ALL;
	}
	
	/**
	 * @return null if no player with the given id exists
	 */
	public static Player getByID(int id) {
		return BY_ID.get(id);
	}
	
	/**
	 * @return null if no player with the given socket exists
	 */
	public static Player getBySocket(WebSocket socket) {
		return BY_SOCKET.get(socket);
	}
	
	public static void logOverview() {
		Log.response("--- Player overview ---");
		for (Player player : Player.getAll()) {
			Log.response(player.getID() + " / " + player.getUsername() + " : " + player.getPhase());
		}
	}
	
	public void remove() {
		ALL.remove(this);
		BY_ID.remove(id);
		BY_SOCKET.remove(socket);
	}
	
	private static int lastID = -1;
	
	public static Player createPlayer(WebSocket socket) {
		lastID++;
		return new Player(lastID, socket);
	}
	
}
