package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

/**
 * CardDealtMessage (server -> client)
 * - role identifier
 * - image id
 * - role title
 * - empty string
 * - role description
 * - empty string
 * - image filename
 */
public class CardDealtMessage extends ServerToClientMessage {
	
	@Getter
	protected final RoleCard card;
	
	public CardDealtMessage(RoleCard card) {
		super(ServerToClientMessageType.CARD_DEALT);
		this.card = card;
	}
	
	/**
	 * @throws GameNotStartedYetException if the given game has not been started yet
	 * @throws UsernameNotInGameException if the given username is not in the given game
	 */
	public CardDealtMessage(Player player, Game game) throws GameNotStartedYetException, UsernameNotInGameException {
		this(game.getDealtCard(player.getUsername()));
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		Role role = card.getRole();
		data.add(role.getIdentifier());
		data.add(card.getImageID());
		data.add(role.getTitle());
		data.add("");
		data.add(role.getDescription());
		data.add("");
		data.add(card.getImageFilename());
		return data;
	}
	
}
