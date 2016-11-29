package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

public class ServerToClientGamePlayerMessage extends ServerToClientGameMessage {
	
	@Getter
	protected final Player player;
	
	protected ServerToClientGamePlayerMessage(ServerToClientMessageType type, Player player) {
		super(type, player.getGame());
		this.player = player;
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		data.add(player.getID());
		return data;
	}
	
	public void sendToPlayer() {
		send(player);
	}
	
}
