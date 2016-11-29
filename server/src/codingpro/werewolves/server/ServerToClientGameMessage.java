package codingpro.werewolves.server;

import java.util.List;

import lombok.Getter;

public class ServerToClientGameMessage extends ServerToClientMessage {
	
	@Getter
	protected final Game game;
	
	protected ServerToClientGameMessage(ServerToClientMessageType type, Game game) {
		super(type);
		this.game = game;
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = super.getData();
		data.add(game.getID());
		return data;
	}
	
	public void sendToAllPlayers() {
		sendToAllPlayers(game);
	}
	
}
