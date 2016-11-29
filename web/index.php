<?php

include_once "config.php";

?>

<html>

<head>
	
	<title><?php echo $config->gameName; ?></title>
	
	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link href="https://fonts.googleapis.com/css?family=Coming+Soon|Covered+By+Your+Grace|Permanent+Marker|EB+Garamond|Cinzel" rel="stylesheet">
	
	<link rel="stylesheet" type="text/css" href="styles.css">
	
	<script type="text/javascript">
		
		function debug_message(message) {
			<?php
			if ($config->debugMessagesOn) {
				echo "console.log(message);";
			}
			?>
		}
		
	</script>
	
	<script type="text/javascript">
		
		var isChromium = window.chrome,
		winNav = window.navigator,
		vendorName = winNav.vendor,
		isOpera = winNav.userAgent.indexOf("OPR") > -1,
		isIEedge = winNav.userAgent.indexOf("Edge") > -1,
		isIOSChrome = winNav.userAgent.match("CriOS");
		
		var isSomeFormOfChrome;
		
		if(isIOSChrome){
		   isSomeFormOfChrome = true;
		} else if(isChromium !== null && isChromium !== undefined && vendorName === "Google Inc." && isOpera == false && isIEedge == false) {
		   isSomeFormOfChrome = true;
		} else { 
		   isSomeFormOfChrome = false;
		}
		
		if (!isSomeFormOfChrome) {
			window.location.href = "<?php echo $config->browserErrorPage; ?>";
		}
		
	</script>
	
	<script type="text/javascript" src="jquery-1.12.4.min.js"></script>
	
	<script src="toastr.min.js"></script>
	
	<script type="text/javascript">
		
		Array.prototype.peek = function() {
			return this[this.length-1];
		}
		
	</script>
	
	<script type="text/javascript">
		
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
		var hadFirstConnectionAttempt = false;
		
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
		
		function showServerOfflineNotification() {
			var currentMillis = Date.now();
			if (connectionOpenAtLastReconnect || currentMillis-millisOfLastUnsuccessfulConnectionNotification >= <?php echo $config->millisForReconnectMessage; ?>) {
				toastr["info"]("Connecting to the server, please wait...");
				connectionOpenAtLastReconnect = false;
				millisOfLastUnsuccessfulConnectionNotification = currentMillis;
			}
		}
		
		var connection;
		
		function setUpConnection() {
			try {
				
				debug_message("Attempting to connect to server at <?php echo $config->serverIP; ?>:<?php echo $config->serverPort; ?>...");
				
				connection = new WebSocket('ws://<?php echo $config->serverIP; ?>:<?php echo $config->serverPort; ?>/');
				
				connection.onopen = function() {
					debug_message("Connection to server opened");
					connectionOpenAtLastReconnect = true;
					unsuccessfulConnections = 0;
					if (hadFirstConnectionAttempt) {
						toastr["success"]("Connected to the server! :)");
					} else {
						toastr["success"]("Welcome to the <?php echo $config->gameName; ?> server!");
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
							toastr["error"]("You are not in that game.");
							letGameCodeFlashRed();
						}
						break;
					case SERVER_TO_CLIENT.NO_SUCH_GAME:
						if (playerPhase == PLAYER_PHASE.CHOOSING_GAME) {
							toastr["error"]("There is no game with that code.");
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
							toastr["error"]("That username is not allowed.");
							letUsernameFlashRed();
						}
						break;
					case SERVER_TO_CLIENT.GAME_CODE_REMOVED:
						if (playerPhase == PLAYER_PHASE.IN_GAME) {
							debug_message("yew");
							toastr["info"]("The game code was deleted.");
							setPlayerPhase(PLAYER_PHASE.CHOOSING_GAME);
						}
						break;
					}
				};
				
			} catch (error) {
				debug_message("Could not connect to server");
				showServerOfflineNotification();
			}
		}
		
		setUpConnection();
		
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
		
		function start() {
			
			$("#error_vertical_centered").hide();
			$("#fullpagediv").show();
			
			setTimeout(function() {reconnect();}, <?php echo $config->millisPerReconnect; ?>);
			
		}
		
		function reconnect() {
			
			hadFirstConnectionAttempt = true;
			
			if (connection == null || connection.readyState == 3) {
				setUpConnection();
			}
			
			setTimeout(function() {reconnect();}, <?php echo $config->millisPerReconnect; ?>);
			
		}
		
		var lastUsernameFlash = [0, 0];
		
		function letUsernameFlashRed() {
			debug_message("flashing username");
			letFlashRed(document.getElementById("username_input"), 0);
		}
		
		function letGameCodeFlashRed() {
			letFlashRed(document.getElementById("game_code_input"), 1);
		}
		
		function letFlashRed(element, id) {
			element.className = "input glowing_input";
			lastUsernameFlash[id] = Date.now();
			setTimeout(function() {
				if (Date.now()-lastUsernameFlash[id] >= 200) {
					element.className = "input";
				}
			}, 210);
		}
		
		function submitUsername() {
			var value = document.getElementById('username_input').value;
			lastChosenUsername = value;
			if (value != "") {
				sendMessageToServer(CLIENT_TO_SERVER.SET_USERNAME, [
					value.toLowerCase()
				]);
			}
		}
		
		function submitGameCode() {
			var value = document.getElementById('game_code_input').value;
			if (value != "") {
				sendMessageToServer(CLIENT_TO_SERVER.JOIN_GAME, [
					value
				]);
			}
		}
		
		function topLeftBackButton() {
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
		
	</script>
	
	<link href="toastr.css" rel="stylesheet"/>
	
</head>

<body onload="start();">

	<div id="float_left_top" style="display: none;" onClick="javascript:topLeftBackButton();">
		&#9664;&nbsp;&nbsp;<span id="name_indicator">Hi</span><!--&nbsp;&nbsp;<span id="room_indicator"><span class="room_character">&#10070;</span>&nbsp;&nbsp;<span class="room_indicator_room">Room</span> 641</span>-->
	</div>

	<div id="error_vertical_centered" style="display: none;">
	<div id="errordiv"><span id="errorspan"><span class="title"><?php echo $config->gameName; ?></span><br><br>Loading <?php echo $config->gameName; ?>... If it does not show up after a few seconds, click <a href="<?php echo $config->browserErrorPage; ?>">here</a>.</span></div>
	</div>
	
	<div id="fullpagediv">
		<div id="connecting_div" style="display: none;">
			<div class="centered_box connecting_box">
				<span class="decent_font">
					<span class="title"><?php echo $config->gameName; ?></span>
					<br><br>
					Connecting...
				</span>
			</div>
		</div>
		<div id="choosing_username_div" style="display: none;">
			<div class="centered_box big_red_border">
				<div class="centered_box smaller_inner_box align_center decent_font">
					<div class="margins">
						<span class="title"><?php echo $config->gameName; ?></span>
						<br><br>
						Please enter your name:
						<br><br>
						<input id="username_input" class="input" type="text" autocomplete="off" autofocus maxlength="30"/>
						<br><br>
						<center>
							<div id="username_button" class="button" onClick="javascript:submitUsername();">Submit</div>
						</center>
					</div>
				</div>
			</div>
		</div>
		<div id="choosing_game_div" style="display: none;">
			<div class="centered_box big_red_border">
				<div class="centered_box smaller_inner_box align_center decent_font">
					<div class="margins">
						<span class="title"><?php echo $config->gameName; ?></span>
						<br><br>
						Enter a game code to join:
						<br><br>
						<input id="game_code_input" class="input" type="number" autocomplete="off" autofocus maxlength="3" value="<?php echo $config->defaultGameCode; ?>"/>
						<br><br>
						<center>
							<div id="game_code_button" class="button" onClick="javascript:submitGameCode();">Submit</div>
						</center>
					</div>
				</div>
			</div>
		</div>
		<div id="in_game_div" style="display: none;">
			<div id="not_started_div" style= "display: none;">
				<div class="centered_box big_red_border">
				<div class="centered_box smaller_inner_box align_center decent_font">
				<div class="margins_count">
						<span class="title"><?php echo $config->gameName; ?></span>
						<br><br>
						<div class="margin_up"></div>
						<span id="player_count">0</span><br>
						<span id="player_count_desc">players in game!</span>
				</div>
				</div>
				</div>
			</div>
			<div id="started_div" style="display: none;">
				<div class="centered_box_down full_box">
				<center>
				<img id="card_image" style="display: none;"/><br>
				<span id="card_description" style="display: none;"></span>
				<center>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		
		var $input = $('#username_input');
		$input.keyup(function(e) {
			var max = 30;
			if ($input.val().length > max) {
				$input.val($input.val().substr(0, max));
			}
		});
		$("#username_input").keyup(function(event){
			if (event.keyCode == 13){
				$("#username_button").click();
			}
		});
		$input = $('#game_code_input');
		$input.keyup(function(e) {
			var max = 3;
			if ($input.val().length > max) {
				$input.val($input.val().substr(0, max));
			}
		});
		$("#game_code_input").keyup(function(event){
			if (event.keyCode == 13){
				$("#game_code_button").click();
			}
		});
		
	</script>
	
	<script type="text/javascript">
		
		$("#connecting_div").hide();
		$("#choosing_username_div").hide();
		$("#choosing_game_div").hide();
		$("#in_game_div").hide();
		
		if (playerPhase == -1) {
			setTimeout(function() {
				if (playerPhase == -1) {
					setPlayerPhase(PLAYER_PHASE.CONNECTING);
				}
			}, 500);
		} else {
			setPlayerPhase(playerPhase);
		}
		
		setTimeout(function() {
			if (document.getElementById("fullpagediv").style.display == "none") {
				document.getElementById("error_vertical_centered").show();
			}
		}, 450);
			
	</script>
	
</body>

</html>