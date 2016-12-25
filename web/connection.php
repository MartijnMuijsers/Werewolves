<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

var hadFirstConnectionAttempt = false;

function sendMessageToServer(type, data) {
	try {
		if (connection != null) {
			if (connection.readyState == 1) {
				var message = type;
				if (data.length == 0) {
					message += "|";
				} else {
					for (var i = 0; i < data.length; i++) {
						message += "|"+data[i];
					}
				}
				connection.send(message);
			}
		}
	} catch (error) {
		debug_message("Error while sending message to server (this may be non-critical if the connection is still being opened): " + error);
	}
}

var CLIENT_TO_SERVER = {
	SET_USERNAME: 0,
	JOIN_GAME: 1,
	REMOVE_USERNAME: 2,
	LEAVE_GAME: 3
};
var SERVER_TO_CLIENT = {
	UPDATE_PLAYER_LIST: 0,
	NO_SUCH_USERNAME_IN_GAME: 1,
	NO_SUCH_GAME: 2,
	GAME_STOPPED: 3,
	CARD_DEALT: 4,
	CHANGE_PHASE: 5,
	INVALID_USERNAME: 6,
	GAME_CODE_REMOVED: 7
};

var connection;
var hadFirstConnectionAttempt = false;

function setUpConnection() {
	try {
		
		debug_message("Attempting to connect to server at <?php echo $config->serverIP; ?>:<?php echo $config->serverPort; ?>...");
		
		connection = new WebSocket('ws://<?php echo $config->serverIP; ?>:<?php echo $config->serverPort; ?>/');
		
		connection.onopen = function() {
			debug_message("Connection to server opened");
			connectionOpenAtLastReconnect = true;
			unsuccessfulConnections = 0;
			if (hadFirstConnectionAttempt) {
				showConnectedToServerNotification();
			} else {
				showWelcomeToServerNotification();
			}
		};
		
		connection.onerror = function(error) {
			debug_message("Connection to server experienced an error");
		};
		
		connection.onclose = function(evt) {
			if (evt.code == 3110) {
				debug_message("Connection to server closed");
			} else {
				debug_message("Could not connect to server");
			}
			showServerOfflineNotification();
			setPlayerPhase(PLAYER_PHASE.CONNECTING);
			setTimeout(function() {reconnect();}, <?php echo $config->millisPerReconnect; ?>);
		};
		
		connection.onmessage = function(e) {
			var split = e.data.split("|");
			debug_message(split);
			var type = parseInt(split[0]);
			onMessage(type, split);
		};
		
	} catch (error) {
		debug_message("Could not connect to server");
		showServerOfflineNotification();
	}
}

function reconnect() {
	
	hadFirstConnectionAttempt = true;
	
	if (connection == null || connection.readyState == 3) {
		setUpConnection();
	}
	
	setTimeout(function() {reconnect();}, <?php echo $config->millisPerReconnect; ?>);
	
}