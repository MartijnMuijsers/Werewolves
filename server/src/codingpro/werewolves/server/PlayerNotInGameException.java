package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerNotInGameException extends Exception {
	
	private static final long serialVersionUID = 2708292018185287439L;
	
	@Getter
	protected final Player player;
	
}
