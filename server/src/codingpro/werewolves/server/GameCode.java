package codingpro.werewolves.server;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class GameCode {
	
	private static final int length = 3;
	private static final char[] possibleCharacters = "0123456789".toCharArray();
	
	@Getter
	final String text;
	
	@Override
	public String toString() {
		return text;
	}
	
	public boolean isInUse() {
		return Game.getByCode(this) != null;
	}
	
	public static GameCode generateNotInUse() {
		while (true) {
			GameCode code = generate();
			if (!code.isInUse()) {
				return code;
			}
		}
	}
	
	private static GameCode generate() {
		char[] charArray = new char[length];
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = possibleCharacters[(int) (Math.random()*possibleCharacters.length)];
		}
		return new GameCode(new String(charArray));
	}
	
	/**
	 * @return null if the given text is an invalid code
	 */
	public static GameCode getByText(String text) {
		if (text.length() != length) {
			return null;
		}
		char[] charArray = text.toCharArray();
		checkCharacters: for (char c : charArray) {
			for (char possibleC : possibleCharacters) {
				if (c == possibleC) {
					continue checkCharacters;
				}
			}
			return null;
		}
		return new GameCode(text);
	}
	
}
