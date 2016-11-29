package codingpro.werewolves.server;

/**
 * SetGameMessage (client -> server)
 * - game
 */
public class JoinGameMessage extends ClientToServerMessage {
	
	protected Game game;
	
	/**
	 * @return null if the given gamecode was incorrect
	 */
	public Game getGame() {
		return game;
	}
	
	public JoinGameMessage(Player player, Game game) {
		super(ClientToServerMessageType.JOIN_GAME, player);
		this.game = game;
	}
	
	@Override
	public void onReceive() {
		if (player.getPhase() == PlayerPhase.CHOOSING_GAME) {
			if (game == null) {
				new NoSuchGameMessage().send(player);
				return;
			}
			if (game.hasStarted() && !game.hasUsername(player.getUsername())) {
				new NoSuchUsernameInGameMessage().send(player);
				return;
			}
			try {
				game.playerJoins(player);
			} catch (PlayerIsAlreadyInGameException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (PlayerHasNoUsernameException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (UsernameNotInGameException e) {
				// Cannot happen
				e.printStackTrace();
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
	}
	
}
