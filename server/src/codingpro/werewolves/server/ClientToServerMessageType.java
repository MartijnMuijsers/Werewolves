package codingpro.werewolves.server;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public enum ClientToServerMessageType {
	
	SET_USERNAME(0, (player, stringTokenizer) -> new SetUsernameMessage(
			player,
			Username.getByText(stringTokenizer.nextToken())
	)),
	JOIN_GAME(1, (player, stringTokenizer) -> new JoinGameMessage(
			player,
			Game.getByCode(GameCode.getByText(stringTokenizer.nextToken()))
	)),
	REMOVE_USERNAME(2, (player, stringTokenizer) -> new RemoveUsernameMessage(
			player
	)),
	LEAVE_GAME(3, (player, stringTokenizer) -> new LeaveGameMessage(
			player
	));
	
	private int id;
	private ClientToServerMessageGenerator messageGenerator;
	
	private ClientToServerMessageType(int id, ClientToServerMessageGenerator messageGenerator) {
		this.id = id;
		this.messageGenerator = messageGenerator;
	}
	
	public int getID() {
		return id;
	}
	
	public ClientToServerMessage generate(Player player, String messageContents) {
		return generate(player, new StringTokenizer(messageContents, Message.DELIMITER));
	}
	
	public ClientToServerMessage generate(Player player, StringTokenizer stringTokenizer) {
		return messageGenerator.generate(player, stringTokenizer);
	}
	
	protected static interface ClientToServerMessageGenerator {
		
		public ClientToServerMessage generate(Player player, StringTokenizer stringtokenizer);
		
	}
	
	private static Map<Integer, ClientToServerMessageType> BY_ID = new HashMap<>();
	
	static {
		for (ClientToServerMessageType type : values()) {
			BY_ID.put(type.getID(), type);
		}
	}
	
	public static ClientToServerMessageType getByID(int id) {
		return BY_ID.get(id);
	}
	
}
