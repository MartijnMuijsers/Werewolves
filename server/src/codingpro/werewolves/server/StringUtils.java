package codingpro.werewolves.server;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {
	
	public static String removeNonAlphabeticCharacters(String text) {
		StringBuilder result = new StringBuilder("");
		for (char c : text.toCharArray()) {
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				result = result.append(c);
			}
		}
		return result.toString();
	}
	
	public static String normalize(String text) {
		return removeNonAlphabeticCharacters(text).toLowerCase();
	}
	
}
