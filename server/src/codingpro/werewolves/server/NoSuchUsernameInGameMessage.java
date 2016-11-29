package codingpro.werewolves.server;

import java.util.List;

/**
 * NoSuchUsernameInGameMessage (server -> client)
 */
public class NoSuchUsernameInGameMessage extends ServerToClientMessage {
	
	public NoSuchUsernameInGameMessage() {
		super(ServerToClientMessageType.NO_SUCH_USERNAME_IN_GAME);
	}
	
	@Override
	public List<Object> getData() {
		return super.getData();
	}
	
}
