package codingpro.werewolves.server;

import java.util.List;

/**
 * NoSuchGameMessage (server -> client)
 */
public class InvalidUsernameMessage extends ServerToClientMessage {
	
	public InvalidUsernameMessage() {
		super(ServerToClientMessageType.INVALID_USERNAME);
	}
	
	@Override
	public List<Object> getData() {
		return super.getData();
	}
	
}
