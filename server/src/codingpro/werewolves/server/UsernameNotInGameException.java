package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameNotInGameException extends Exception {
	
	private static final long serialVersionUID = -6842909369621126453L;
	
	@Getter
	protected final Username username;
	@Getter
	protected final Game game;
	
}
