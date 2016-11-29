package codingpro.werewolves.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IncorrectPlayerPhaseException extends Exception {
	
	private static final long serialVersionUID = 462437982003142142L;
	
	@Getter
	protected final Player player;
	
}
