package codingpro.werewolves.server;

/**
 * SetUsernameMessage (client -> server)
 * - username
 */
public class SetUsernameMessage extends ClientToServerMessage {
	
	protected Username username;
	
	/**
	 * @return null if the given username was invalid
	 */
	public Username getUsername() {
		return username;
	}
	
	public SetUsernameMessage(Player player, Username username) {
		super(ClientToServerMessageType.SET_USERNAME, player);
		this.username = username;
	}
	
	@Override
	public void onReceive() {
		if (player.getPhase() == PlayerPhase.CHOOSING_USERNAME) {
			if (username == null) {
				new InvalidUsernameMessage().send(player);
				return;
			}
			try {
				player.setChosenUsername(username);
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
}
