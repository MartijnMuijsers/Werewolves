<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

var PLAYER_PHASE = {
	CONNECTING: 0,
	CHOOSING_USERNAME: 1,
	CHOOSING_GAME: 2,
	IN_GAME: 3
};
var GAME_PHASE = {
	NOT_STARTED: 0,
	STARTED: 1
};

var playerPhase = -1;
var gamePhase = -1;

var lastChosenUsername = "";
function setPlayerPhase(newPlayerPhase) {
	switch (playerPhase) {
		case PLAYER_PHASE.CONNECTING:
			$("#connecting_div").hide();
			break;
		case PLAYER_PHASE.CHOOSING_USERNAME:
			debug_message("hide");
			$("#choosing_username_div").hide();
			break;
		case PLAYER_PHASE.CHOOSING_GAME:
			$("#choosing_game_div").hide();
			break;
		case PLAYER_PHASE.IN_GAME:
			$("#in_game_div").hide();
			break;
	}
	debug_message("old phase was "+playerPhase);
	playerPhase = newPlayerPhase;
	debug_message("new phase is " + playerPhase);
	switch (playerPhase) {
		case PLAYER_PHASE.CONNECTING:
			$("#connecting_div").show();
			$('#float_left_top').hide();
			break;
		case PLAYER_PHASE.CHOOSING_USERNAME:
			debug_message("show");
			$("#choosing_username_div").show();
			setTimeout(function() { document.getElementById("username_input").focus(); }, 5);
			$('#float_left_top').hide();
			break;
		case PLAYER_PHASE.CHOOSING_GAME:
			setGamePhase(GAME_PHASE.NOT_STARTED);
			$("#choosing_game_div").show();
			setTimeout(function() { document.getElementById("game_code_input").focus(); }, 5);
			document.getElementById('name_indicator').innerHTML = lastChosenUsername;
			$('#float_left_top').show();
			break;
		case PLAYER_PHASE.IN_GAME:
			$("#in_game_div").show();
			$('#float_left_top').show();
			break;
	}
}

function setGamePhase(newGamePhase) {
	switch (gamePhase) {
		case GAME_PHASE.NOT_STARTED:
			$("#not_started_div").hide();
			break;
		case GAME_PHASE.STARTED:
			$("#started_div").hide();
			$("#card_image").hide();
			$("#card_description").hide();
			break;
	}
	gamePhase = newGamePhase;
	switch (gamePhase) {
		case GAME_PHASE.NOT_STARTED:
			$("#not_started_div").show();
			break;
		case GAME_PHASE.STARTED:
			$("#started_div").show();
			break;
	}
}

function onMessage(type, split) {
	switch (type) {
	case SERVER_TO_CLIENT.UPDATE_PLAYER_LIST:
		if (playerPhase == PLAYER_PHASE.IN_GAME && gamePhase == GAME_PHASE.NOT_STARTED) {
			var players = split.length-1;
			var countElement = document.getElementById("player_count");
			countElement.innerHTML = players;
			var countDescElement = document.getElementById("player_count_desc");
			if (players == 1) {
				countDescElement.innerHTML = "player in game!";
			} else {
				countDescElement.innerHTML = "players in game!";
			}
		}
		break;
	case SERVER_TO_CLIENT.NO_SUCH_USERNAME_IN_GAME:
		if (playerPhase == PLAYER_PHASE.CHOOSING_GAME) {
			showNotInThatGameNotification();
			letGameCodeFlashRed();
		}
		break;
	case SERVER_TO_CLIENT.NO_SUCH_GAME:
		if (playerPhase == PLAYER_PHASE.CHOOSING_GAME) {
			showNoSuchGameNotification();
			letGameCodeFlashRed();
		}
		break;
	case SERVER_TO_CLIENT.GAME_STOPPED:
		if (playerPhase == PLAYER_PHASE.IN_GAME && gamePhase == GAME_PHASE.STARTED) {
			setGamePhase(GAME_PHASE.NOT_STARTED);
		}
		break;
	case SERVER_TO_CLIENT.CARD_DEALT:
		if (playerPhase == PLAYER_PHASE.IN_GAME) {
			if (gamePhase == GAME_PHASE.NOT_STARTED) {
				setGamePhase(GAME_PHASE.STARTED);
			}
			var imageElement = $('#card_image');
			var descriptionElement = $('#card_description');
			var imageURL = "role_packs/<?php echo $config->rolePack; ?>/<?php echo $config->imagesPath; ?>"+split[7];
			debug_message("image element is " + imageElement);
			debug_message("image element DOM is " + imageElement.get());
			debug_message("Setting card image to " + imageURL);
			imageElement.attr("src", imageURL);
			debug_message("It is now " + imageElement.get().src);
			descriptionElement.get().innerHTML = split[5];
			imageElement.show();
			descriptionElement.show();
		}
		break;
	case SERVER_TO_CLIENT.CHANGE_PHASE:
		debug_message("Trying with "+ split[1]);
		debug_message("Must change phase to " + PLAYER_PHASE[split[1]]);
		setPlayerPhase(PLAYER_PHASE[split[1]]);
		break;
	case SERVER_TO_CLIENT.INVALID_USERNAME:
		if (playerPhase == PLAYER_PHASE.CHOOSING_USERNAME) {
			showUsernameNotAllowedNotification();
			letUsernameFlashRed();
		}
		break;
	case SERVER_TO_CLIENT.GAME_CODE_REMOVED:
		if (playerPhase == PLAYER_PHASE.IN_GAME) {
			debug_message("yew");
			showGameCodeDeletedNotification();
			setPlayerPhase(PLAYER_PHASE.CHOOSING_GAME);
		}
		break;
	}
}

function submitUsername() {
	var value = getUsernameInputValue();
	lastChosenUsername = value;
	if (value != "") {
		sendMessageToServer(CLIENT_TO_SERVER.SET_USERNAME, [
			value.toLowerCase()
		]);
	}
}

function submitGameCode() {
	var value = getGameCodeInputValue();
	if (value != "") {
		sendMessageToServer(CLIENT_TO_SERVER.JOIN_GAME, [
			value
		]);
	}
}

function onTopLeftBackButton() {
	debug_message("button");
	debug_message("current player phase: " + playerPhase)
	switch (playerPhase) {
		case PLAYER_PHASE.CHOOSING_GAME:
		debug_message("a");
		sendMessageToServer(CLIENT_TO_SERVER.REMOVE_USERNAME, []);
		break;
		case PLAYER_PHASE.IN_GAME:
		debug_message("b");
		sendMessageToServer(CLIENT_TO_SERVER.LEAVE_GAME, []);
		break;
	}
}