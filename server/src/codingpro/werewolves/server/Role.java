package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Role {
	
	static {
		IOUtils.createDirectory("role_packs");
	}
	
	private static final String unknownDescription = "Details unknown";
	private static final List<Role> roles = new ArrayList<>();
	
	@Getter
	private final String identifier;
	@Getter
	private final String title;
	@Getter
	private final String description;
	@Getter
	private final int numberOfCardImages;
	
	private Role(String identifier, String title, int numberOfCardImages) {
		this(identifier, title, unknownDescription, numberOfCardImages);
	}
	
	private Role(String identifier, String title, String description, int numberOfCardImages) {
		this.identifier = identifier;
		this.title = title;
		this.description = description;
		this.numberOfCardImages = numberOfCardImages;
	}
	
	/**
	 * @return null if none matches
	 */
	public static Role getFromInputText(String text) {
		text = StringUtils.normalize(text);
		for (Role role : roles) {
			if (StringUtils.normalize(role.getIdentifier()).equals(text) ||
					StringUtils.normalize(role.getTitle()).equals(text)) {
				return role;
			}
		}
		return null;
	}
	
}
