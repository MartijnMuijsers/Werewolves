package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerHasNoUsernameException extends Exception {
	
	private static final long serialVersionUID = -1180424511365492683L;
	
	@Getter
	protected final Player player;
	
}
