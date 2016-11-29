package codingpro.werewolves.server;

/**
 * SetUsernameMessage (client -> server)
 * - username
 */
public class LeaveGameMessage extends ClientToServerMessage {
	
	public LeaveGameMessage(Player player) {
		super(ClientToServerMessageType.REMOVE_USERNAME, player);
	}
	
	@Override
	public void onReceive() {
		if (player.getPhase() == PlayerPhase.IN_GAME) {
			try {
				player.getGame().playerLeaves(player);
			} catch (PlayerNotInGameException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
}
