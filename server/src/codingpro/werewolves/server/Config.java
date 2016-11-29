package codingpro.werewolves.server;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.Getter;

public final class Config {
	
	private static Config instance = null;
	public static Config get() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	private static final String filePath = "config.txt"; 
	
	private Config() {
		if (IOUtils.createFileIfNotExists(filePath)) {
			try {
				BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
				try {
					for (Field field : getClass().getDeclaredFields()) {
						if (!Modifier.isStatic(field.getModifiers())) {
							String key = field.getName();
							try {
								Class<?> fieldClass = field.getType();
								String valueString = null;
								if (fieldClass.equals(String.class)) {
									valueString = ""+field.get(this);
								} else if (fieldClass.equals(int.class)) {
									valueString = ""+field.getInt(this);
								} else {
									Log.error("The setting " + key + " could not be saved since its type is unknown.");
								}
								if (valueString != null) {
									w.write(key);
									w.write('=');
									w.write(valueString);
									w.write('\n');
								}
							} catch (IllegalAccessException e) {
								Log.warning("Could not save a configuration setting due to a security setting: "  + key);
								e.printStackTrace();
							}
						}
					}
					w.close();
				} catch (IOException e) {
					Log.warning("Could not write to the configuration file! Does the application have the right permissions?");
					e.printStackTrace();
				} finally {
					try {
						w.close();
					} catch (IOException e) {}
				}
			} catch (FileNotFoundException e) {
				// Cannot happen as long as the file system did not suddenly change
				e.printStackTrace();
			}
		}
		load();
	}
	
	private void load() {
		for (String line : new FileLineIterable(filePath)) {
			String trimmedLine = line.trim();
			if (!(trimmedLine.isEmpty() || trimmedLine.startsWith("#"))) {
				int index = line.indexOf('=');
				if (index == -1) {
					Log.warning("Ignoring a line in the configuration without a '=': " + line);
				} else {
					String key;
					if (index == 0) {
						key = "";
					} else {
						key = line.substring(0, index).trim();
					}
					if (key.isEmpty()) {
						Log.warning("Ignoring a line in the configuration without a key: " + line);
					} else {
						String value;
						if (index == line.length()-1) {
							value = "";
						} else {
							value = line.substring(index+1);
						}
						try {
							Field field = getClass().getDeclaredField(key);
							if (!Modifier.isStatic(field.getModifiers())) {
								Class<?> fieldClass = field.getType();
								if (fieldClass.equals(String.class)) {
									field.set(this, value);
								} else if (fieldClass.equals(int.class)) {
									try {
										int intValue = Integer.parseInt(value);
										field.set(this, intValue);
									} catch (NumberFormatException e) {
										Log.warning("Ignoring a line in the configuration that should have had an integer value: " + line);
									}
								} else {
									Log.error("The setting " + key + " could not be loaded since its type is unknown.");
								}
							} else {
								Log.warning("Ignoring a line in the configuration with a forbidden key: " + line);
							}
						} catch (NoSuchFieldException e) {
							Log.warning("Ignoring a line in the configuration with an invalid key: " + line);
						} catch (SecurityException | IllegalAccessException e) {
							Log.warning("Could not load a configuration setting due to a security setting: " + line);
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	@Getter
	private String serverIP = "localhost";
	@Getter
	private int serverPort = 9907;
	@Getter
	private String rolePack = "";
	
}
