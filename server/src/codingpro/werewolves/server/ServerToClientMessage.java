package codingpro.werewolves.server;

import java.util.ArrayList;
import java.util.List;

import org.java_websocket.WebSocket;

import lombok.Getter;

public class ServerToClientMessage extends Message {
	
	@Getter
	protected ServerToClientMessageType type;
	
	protected ServerToClientMessage(ServerToClientMessageType type) {
		super(type.getID());
		this.type = type;
	}
	
	private void send(WebSocket socket) {
		if (socket.isOpen()) {
			socket.send(toString());
		}
	}
	
	public void send(Player player) {
		send(player.getSocket());
	}
	
	public void send(Iterable<Player> players) {
		for (Player player : players) {
			send(player);
		}
	}
	
	public void sendToAllPlayers(Game game) {
		send(game.getPlayers());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		builder.append(id);
		for (Object data : getData()) {
			builder.append(Message.DELIMITER).append(data);
		}
		return builder.toString();
	}
	
	protected List<Object> getData() {
		return new ArrayList<>();
	}
	
}
