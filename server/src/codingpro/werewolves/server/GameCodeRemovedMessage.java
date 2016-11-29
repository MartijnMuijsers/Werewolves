package codingpro.werewolves.server;

import java.util.List;

/**
 * GameStoppedMessage (server -> client)
 */
public class GameCodeRemovedMessage extends ServerToClientMessage {
	
	public GameCodeRemovedMessage() {
		super(ServerToClientMessageType.GAME_CODE_REMOVED);
	}
	
	@Override
	public List<Object> getData() {
		return super.getData();
	}
	
}
