package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerIsAlreadyInGameException extends Exception {
	
	private static final long serialVersionUID = -3438830753411431566L;
	
	@Getter
	protected final Player player;
	
}
