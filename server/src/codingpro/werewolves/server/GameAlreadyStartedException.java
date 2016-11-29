package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameAlreadyStartedException extends Exception {
	
	private static final long serialVersionUID = -2723837241299486559L;
	
	@Getter
	protected final Game game;
	
}
