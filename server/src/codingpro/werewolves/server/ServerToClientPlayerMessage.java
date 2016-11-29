package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

public class ServerToClientPlayerMessage extends ServerToClientMessage {
	
	@Getter
	protected final Player player;
	
	protected ServerToClientPlayerMessage(ServerToClientMessageType type, Player player) {
		super(type);
		this.player = player;
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		data.add(player);
		return data;
	}
	
	public void sendToPlayer() {
		send(player);
	}
	
}
