package codingpro.werewolves.server;

import java.util.List;

/**
 * GameStoppedMessage (server -> client)
 */
public class GameStoppedMessage extends ServerToClientMessage {
	
	public GameStoppedMessage() {
		super(ServerToClientMessageType.GAME_STOPPED);
	}
	
	@Override
	public List<Object> getData() {
		return super.getData();
	}
	
}
