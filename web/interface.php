<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

var lastRedFlash = [0, 0];

function letFlashRed(element, id) {
	element.className = "input glowing_input";
	lastRedFlash[id] = Date.now();
	setTimeout(function() {
		if (Date.now()-lastRedFlash[id] >= 200) {
			element.className = "input";
		}
	}, 210);
}

function letUsernameFlashRed() {
	debug_message("flashing username");
	letFlashRed(document.getElementById("username_input"), 0);
}

function letGameCodeFlashRed() {
	letFlashRed(document.getElementById("game_code_input"), 1);
}

function getUsernameInputValue() {
	return document.getElementById('username_input').value;
}

function getGameCodeInputValue() {
	return document.getElementById('game_code_input').value;
}

function hideErrorElements() {
	$("#error_vertical_centered").hide();
	$("#fullpagediv").show();
}