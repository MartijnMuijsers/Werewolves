package codingpro.werewolves.server;

import java.util.HashMap;
import java.util.Map;

public enum ServerToClientMessageType {
	
	UPDATE_PLAYER_LIST(0),
	NO_SUCH_USERNAME_IN_GAME(1),
	NO_SUCH_GAME(2),
	GAME_STOPPED(3),
	CARD_DEALT(4),
	CHANGE_PHASE(5),
	INVALID_USERNAME(6),
	GAME_CODE_REMOVED(7);
	
	private int id;
	
	private ServerToClientMessageType(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	private static Map<Integer, ServerToClientMessageType> BY_ID = new HashMap<>();
	
	static {
		for (ServerToClientMessageType type : values()) {
			BY_ID.put(type.getID(), type);
		}
	}
	
	public static ServerToClientMessageType getByID(int id) {
		return BY_ID.get(id);
	}
	
}
