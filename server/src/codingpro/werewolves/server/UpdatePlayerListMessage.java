package codingpro.werewolves.server;

import java.util.List;
import java.util.Set;

import lombok.Getter;

/**
 * UpdatePlayerListMessage (server -> client)
 * - usernames...
 */
public class UpdatePlayerListMessage extends ServerToClientMessage {
	
	@Getter
	protected final Set<Username> usernames;
	
	public UpdatePlayerListMessage(Game game) {
		super(ServerToClientMessageType.UPDATE_PLAYER_LIST);
		usernames = game.getUsernames();
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		for (Username username : usernames) {
			data.add(username);
		}
		return data;
	}
	
}
