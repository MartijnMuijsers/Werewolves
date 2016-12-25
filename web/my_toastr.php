<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

toastr.options = {
	"closeButton": false,
	"debug": false,
	"newestOnTop": false,
	"progressBar": true,
	"positionClass": "toast-top-right",
	"preventDuplicates": false,
	"onclick": null,
	"showDuration": "7000",
	"hideDuration": "0",
	"timeOut": "7000",
	"extendedTimeOut": "7000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
}

var connectionOpenAtLastReconnect = true;
var millisOfLastUnsuccessfulConnectionNotification = -1000000000;

function showServerOfflineNotification() {
	var currentMillis = Date.now();
	if (connectionOpenAtLastReconnect || currentMillis-millisOfLastUnsuccessfulConnectionNotification >= <?php echo $config->millisForReconnectMessage; ?>) {
		toastr["info"]("Connecting to the server, please wait...");
		connectionOpenAtLastReconnect = false;
		millisOfLastUnsuccessfulConnectionNotification = currentMillis;
	}
}

function showConnectedToServerNotification() {
	toastr["success"]("Connected to the server! :)");
}

function showWelcomeToServerNotification() {
	toastr["success"]("Welcome to the <?php echo $config->gameName; ?> server!");
}

function showNotInThatGameNotification() {
	toastr["error"]("You are not in that game.");
}

function showNoSuchGameNotification() {
	toastr["error"]("There is no game with that code.");
}

function showUsernameNotAllowedNotification() {
	toastr["error"]("That username is not allowed.");
}

function showGameCodeDeletedNotification() {
	toastr["info"]("The game code was deleted.");
}