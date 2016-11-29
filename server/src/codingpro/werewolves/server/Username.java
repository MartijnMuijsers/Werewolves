package codingpro.werewolves.server;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Username {
	
	private static final int minLength = 1;
	private static final int maxLength = 30;
	private static final char[] possibleCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ~!@#$%^&*()_+`-= {}[]\\:\";'<>?,./".toCharArray();
	
	@Getter
	final String text;
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * @return null if the given text is an invalid username
	 */
	public static Username getByText(String text) {
		if (text.length() < minLength || text.length() > maxLength) {
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
		return new Username(text);
	}
	
}
