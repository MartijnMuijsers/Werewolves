package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.List;

import java.io.File;

import lombok.Getter;

public class Role {
	
	public static void loadRoles() {
		if (!rolesLoaded) {
			Log.info("Loading roles...");
			IOUtils.createDirectory(rolePacksFolderPath);
			String rolePack = Config.get().getRolePack().trim();
			if (rolePack.isEmpty()) {
				Log.info("No role pack (setting 'rolePack') was defined in the configuration!");
				WerewolvesServer.exit();
			}
			String roleFilename = rolePacksFolderPath+"/"+rolePack+"/roles.txt";
			File roleFile = new File(roleFilename);
			if (!roleFile.exists()) {
				Log.info("Role pack " + rolePack + " has no roles.txt!");
				WerewolvesServer.exit();
			}
			for (String line : new FileLineIterable(roleFilename)) {
				String[] split = line.split("\\|");
				String identifier;
				String title;
				String description;
				String numberOfCardImagesString;
				if (split.length == 3) {
					identifier = split[0];
					title = split[1];
					description = null;
					numberOfCardImagesString = split[2];
				} else if (split.length == 4) {
					identifier = split[0];
					title = split[1];
					description = split[2];
					numberOfCardImagesString = split[3];
				} else {
					Log.warning("Invalid number of delimiters in roles file line: " + line);
					continue;
				}
				try {
					int numberOfCardImages = Integer.parseInt(numberOfCardImagesString);
					if (description == null) {
						description = unknownDescription;
					}
					roles.add(new Role(identifier, title, description, numberOfCardImages));
				} catch (NumberFormatException e) {
					Log.warning("Invalid number of card images in roles file line: " + line);
				}
			}
			rolesLoaded = true;
		}
	}
	
	private static final String rolePacksFolderPath = "role_packs";
	private static final String unknownDescription = "Details unknown";
	private static boolean rolesLoaded = false;
	private static final List<Role> roles = new ArrayList<>();
	
	@Getter
	private final String identifier;
	@Getter
	private final String title;
	@Getter
	private final String description;
	@Getter
	private final int numberOfCardImages;
	
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
