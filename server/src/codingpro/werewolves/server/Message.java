package codingpro.werewolves.server;

public class Message {
	
	public static final String DELIMITER = "|";
	
	protected int id;
	
	protected Message(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
}
