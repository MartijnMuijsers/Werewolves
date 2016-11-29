package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TooManyUsernamesException extends Exception {
	
	private static final long serialVersionUID = 81625320996816886L;
	
	@Getter
	protected final Game game;
	
}
