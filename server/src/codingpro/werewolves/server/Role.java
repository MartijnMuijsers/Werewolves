package codingpro.werewolves.server;

import lombok.Getter;

public enum Role {
	
	MODERATOR("Moderator", "Spelleider"),
	WEREWOLF(8, "Werewolf", "Weerwolf"),
	CITIZEN(19, "Citizen", "Burger"),
	SEER("Seer", "Ziener"),
	WITCH("Witch", "Heks"),
	SLUT(3, "Slut", "Slet"),
	HUNTER(4, "Hunter", "Jager"),
	CUPID("Cupid", "Cupido"),
	THIEF(3, "Thief", "Dief"),
	RAVEN(3, "Raven", "Raaf"),
	MEDUSA_WOLF("Medusa Wolf", "Medusawolf"),
	LYCANTHROPE("Lycanthrope", "Lycantroop"),
	WHITE_WOLF(3, "White Wolf", "Witte Wolf"),
	HAIRY_MAN("Hairy Man", "Harige Man"),
	AGITATOR(3, "Agitator", "Onruststoker"),
	LOCAL_MADMAN(3, "Local Madman", "Dorpsgek"),
	TOUGH_GUY(3, "Tough Guy", "Bink"),
	TITUS_AND_HIS_BEAR("Titus And His Bear", "Titus En Zijn Beer"),
	TRADER("Trader", "Handelaar"),
	BROTHER(4, "Brother", "Gebroeder"),
	SISTER(6, "Sister", "Gezuster"),
	COP("Cop", "Politieagent"),
	ALPHA_WOLF("Alpha Wolf", "Alfawolf"),
	COTTAGE_CITIZEN("Cottage Citizen", "Dorpse Dorpeling"),
	FLUTE_PLAYER("Flute Player", "Fluitspeler"),
	SUICIDE_BOMBER("Suicide Bomber", "Terrorist"),
	SHEPHERD(3, "Shepherd", "Herder"),
	PYROMANIAC("Pyromaniac", "Pyromaan"),
	SEKTARIAN(3, "Sektarian", "Sektariër"),
	SCAPEGOAT("Scapegoat", "Zondebok"),
	HEALER("Healer", "Genezer"),
	VILLAGE_ELDER("Village Elder", "Dorpsoudste"),
	INFECTED(3, "Infected", "Besmet"),
	APPRENTICE_SEER("Apprentice seer", "Leerlingziener"),
	MAGICIAN("Magician", "Magiër"),
	WHITE_WITCH("White Witch", "Witte Heks"),
	BLACK_WITCH("Black Witch", "Zwarte Heks"),
	SURVIVAL_EXPERT("Survival Expert", "Survivalexpert"),
	DEMON_WOLF("Demon Wolf", "Demonenwolf"),
	FORESTER("Forester", "Boswachter"),
	SICK_WOLF("Sick Wolf", "Zieke Wolf"),
	ILLUSIONIST("Illusionist", "Goochelaar"),
	WARDEN("Warden", "Cipier"),
	ANGEL("Angel", "Engel"),
	COUNCILLOR("Councillor", "Wethouder"),
	KNIGHT("Knight", "Ridder"),
	REMORSEFUL_WOLF("Remorseful Wolf", "Berouwvolle Wolf"),
	WOLFS_CHILD("Wolf's Child", "Wolfskind"),
	BIG_BAD_WOLF("Big Bad Wolf", "Grote Boze Wolf"),
	MAID("Maid", "Dienstmeisje"),
	VAMPIRE_WOLF("Vampire Wolf", "Vampierwolf"),
	WOLF_DOG("Wolf Dog", "Wolfshond"),
	AURA_SEER("Aura Seer", "Auraziener"),
	BAKER("Baker", "Bakker"),
	SNEAKY_GIRL("Sneaky Girl", "Stiekeme Meisje"),
	DETECTIVE("Detective", "Rechercheur"),
	ASSISTANT("Assistant", "Assistent"),
	TANNER("Tanner", "Leerlooier"),
	CLOWN("Clown", "Clown"),
	VAMPIRE(5, "Vampire", "Vampier"),
	MEDUSA_VAMPIRE("Medusa Vampire", "Medusavampier"),
	SAD_VAMPIRE("Sad Vampire", "Trieste Vampier"),
	MURDERER("Murderer", "Moordenaar"),
	DEVIL("Devil", "Duivel"),
	APPRENTICE_WITCH("Apprentice Witch", "Leerlingheks"),
	CUPID_WOLF("Cupid Wolf", "Cupidowolf"),
	GAMBLER("Gambler", "Gokker");
	
	private static final int defaultNumberOfCardImages = 2;
	private static final String unknownEnglishDescription = "Details unknown";
	private static final String unknownDutchDescription = "Details onbekend";
	
	@Getter
	private final int numberOfCardImages;
	@Getter
	private final String englishName;
	@Getter
	private final String dutchName;
	@Getter
	private final String englishDescription;
	@Getter
	private final String dutchDescription;
	
	private Role(String englishName, String dutchName) {
		this(defaultNumberOfCardImages, englishName, dutchName);
	}
	
	private Role(String englishName, String dutchName, String englishDescription, String dutchDescription) {
		this(defaultNumberOfCardImages, englishName, dutchName, englishDescription, dutchDescription);
	}
	
	private Role(int numberOfCardImages, String englishName, String dutchName) {
		this(numberOfCardImages, englishName, dutchName, unknownEnglishDescription, unknownDutchDescription);
	}
	
	private Role(int numberOfCardImages, String englishName, String dutchName, String englishDescription, String dutchDescription) {
		this.numberOfCardImages = numberOfCardImages;
		this.englishName = englishName;
		this.dutchName = dutchName;
		this.englishDescription = englishDescription;
		this.dutchDescription = dutchDescription;
	}
	
	/**
	 * @return null if none matches
	 */
	public static Role getFromInputText(String text) {
		text = StringUtils.normalize(text);
		for (Role role : values()) {
			if (StringUtils.normalize(role.name()).equals(text) ||
					StringUtils.normalize(role.getEnglishName()).equals(text) ||
					StringUtils.normalize(role.getDutchName()).equals(text)) {
				return role;
			}
		}
		return null;
	}
	
}
