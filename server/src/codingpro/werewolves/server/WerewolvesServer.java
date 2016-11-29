package codingpro.werewolves.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WerewolvesServer extends WebSocketServer {
	
	private static WerewolvesServer instance;
	
	public static WerewolvesServer get() {
		return instance;
	}
	
	private static final Object messageProcessingLock = new Object();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Log.info("Starting Werewolves server...");
		String serverIP = Config.get().getServerIP();
		int serverPort = Config.get().getServerPort();
		Log.info("Binding address: " + serverIP + ":" + serverPort);
		instance = new WerewolvesServer(new InetSocketAddress(serverIP, serverPort));
		Thread serverThread = new Thread(instance);
		serverThread.start();
		Log.info("Werewolves server started!");
		Thread commandReaderThread = new Thread(new CommandReader());
		commandReaderThread.start();
		Log.info("Command reader started!");
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			instance.stop();
		}
		Log.info("Closing...");
		System.exit(0);
	}
	
	public WerewolvesServer(InetSocketAddress address) {
		super(address);
	}
	
	@Override
	public void onOpen(WebSocket socket, ClientHandshake handshake) {
		Player player = Player.createPlayer(socket);
		Log.info("A socket " + socket.getRemoteSocketAddress() + " has been opened, with new player " + player);
	}
	
	@Override
	public void onClose(WebSocket socket, int code, String reason, boolean remote) {
		Player player = Player.getBySocket(socket);
		if (player == null) {
			Log.warning("The socket " + socket.getRemoteSocketAddress() + " has been closed without a known player, for reason: " + reason);
			return;
		}
		Log.info("The socket of player " + player + " has been closed for reason: " + reason);
		if (player.getPhase() == PlayerPhase.IN_GAME) {
			try {
				player.getGame().playerLeaves(player);
			} catch (PlayerNotInGameException e1) {
				// Cannot happen
				e1.printStackTrace();
			} catch (IncorrectPlayerPhaseException e1) {
				// Cannot happen
				e1.printStackTrace();
			}
		}
		if (player.getPhase() == PlayerPhase.CHOOSING_GAME) {
			try {
				player.removeUsername();
			} catch (IncorrectPlayerPhaseException e) {
				// Cannot happen
				e.printStackTrace();
			}
		}
		player.remove();
	}
	
	@Override
	public void onMessage(WebSocket socket, String message) {
		Player player = Player.getBySocket(socket);
		if (player == null) {
			Log.warning("Received message from socket " + socket.getRemoteSocketAddress() + " without a known player: " + message);
			return;
		}
		int index = message.indexOf(Message.DELIMITER);
		String typeIDString = null;
		String contents = null;
		if (index == -1) {
			typeIDString = message;
			contents = "";
		} else if (index == message.length()-1) {
			typeIDString = message.substring(0, message.length()-1);
			contents = "";
		} else if (index == 0) {
			Log.warning("Received message from player " + player + " without a type: " + message);
		} else {
			typeIDString = message.substring(0, index);
			contents = message.substring(index+1);
		}
		if (typeIDString != null) {
			int typeID;
			try {
				typeID = Integer.parseInt(typeIDString);
			} catch (Exception e) {
				Log.warning("Received message from player " + player + " with an non-integer type: " + message);
				return;
			}
			ClientToServerMessageType type = ClientToServerMessageType.getByID(typeID);
			if (type == null) {
				Log.warning("Received message from player " + player + " with a non-existent type: " + message);
				return;
			}
			Log.debug("Received message from player " + player + ": " + message);
			synchronized (messageProcessingLock) {
				type.generate(player, contents).onReceive();
			}
		}
	}
	
	@Override
	public void onError(WebSocket socket, Exception error) {
		Player player = Player.getBySocket(socket);
		if (player == null) {
			Log.warning("An error occured on the socket of player " + player + ":");
		} else {
			Log.warning("An error occured on a socket without a known player, " + socket.getRemoteSocketAddress() + ":");
		}
		error.printStackTrace();
	}
	
}
