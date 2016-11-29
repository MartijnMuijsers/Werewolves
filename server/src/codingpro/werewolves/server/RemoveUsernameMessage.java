package codingpro.werewolves.server;

/**
 * SetUsernameMessage (client -> server)
 * - username
 */
public class RemoveUsernameMessage extends ClientToServerMessage {
	
	public RemoveUsernameMessage(Player player) {
		super(ClientToServerMessageType.REMOVE_USERNAME, player);
	}
	
	@Override
	public void onReceive() {
		if (player.getPhase() == PlayerPhase.CHOOSING_GAME) {
			try {
				player.removeUsername();
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
}
