package codingpro.werewolves.server;

import java.io.File;
import java.io.IOException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOUtils {
	
	/**
	 * @return whether or not the file had to be created AND that was successful
	 */
	public static boolean createFileIfNotExists(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * @return whether or not the directory had to be created AND that was successful
	 */
	public static boolean createDirectory(String path) {
		File file = new File(path);
		return file.mkdirs();
	}
	
}
