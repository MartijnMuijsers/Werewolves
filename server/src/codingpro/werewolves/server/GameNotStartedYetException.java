package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameNotStartedYetException extends Exception {
	
	private static final long serialVersionUID = 3795577178372283537L;
	
	@Getter
	protected final Game game;
	
}
