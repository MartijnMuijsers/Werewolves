package codingpro.werewolves.server;

public abstract class ClientToServerMessage extends Message {
	
	protected ClientToServerMessageType type;
	protected Player player;
	
	protected ClientToServerMessage(ClientToServerMessageType type, Player player) {
		super(type.getID());
		this.type = type;
		this.player = player;
	}
	
	public ClientToServerMessageType getType() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public abstract void onReceive();
	
}
