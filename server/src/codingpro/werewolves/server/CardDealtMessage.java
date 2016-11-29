package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

/**
 * CardDealtMessage (server -> client)
 * - role enum name
 * - image id
 * - english role name
 * - dutch role name
 * - english role description
 * - dutch role description
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
		data.add(role.name());
		data.add(card.getImageID());
		data.add(role.getEnglishName());
		data.add(role.getDutchName());
		data.add(role.getEnglishDescription());
		data.add(role.getDutchDescription());
		data.add(card.getImageFilename());
		return data;
	}
	
}
