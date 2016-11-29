package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameCodeAlreadyInUseException extends Exception {
	
	private static final long serialVersionUID = 2525110730369316162L;
	
	@Getter
	protected final GameCode code;
	
}
