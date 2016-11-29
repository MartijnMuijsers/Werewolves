package codingpro.werewolves.server;

import java.util.List;

/**
 * NoSuchGameMessage (server -> client)
 */
public class NoSuchGameMessage extends ServerToClientMessage {
	
	public NoSuchGameMessage() {
		super(ServerToClientMessageType.NO_SUCH_GAME);
	}
	
	@Override
	public List<Object> getData() {
		return super.getData();
	}
	
}
