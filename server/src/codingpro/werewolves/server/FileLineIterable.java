package codingpro.werewolves.server;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.Getter;

public class FileLineIterable implements Iterable<String> {
	
	@Getter
	private final String path;
	
	public FileLineIterable(String path) {
		this.path = path;
	}
	
	@Override
	public Iterator<String> iterator() {
		return new FileLineIterator();
	}
	
	private class FileLineIterator implements Iterator<String> {
		
		private final BufferedReader reader;
		private String nextLine = null;
		
		public FileLineIterator() {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			} catch (Exception e) {
				reader = null;
			}
			this.reader = reader;
			readLine();
		}
		
		private void readLine() {
			if (reader != null) {
				try {
					nextLine = reader.readLine();
				} catch (IOException e) {
					nextLine = null;
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return nextLine != null;
		}
		
		@Override
		public String next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			String toReturn = nextLine;
			readLine();
			return toReturn;
		}
		
	}
	
}
