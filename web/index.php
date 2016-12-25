<?php

include_once "config.php";

?>

<html>

<head>
	
	<title><?php echo $config->gameName; ?></title>
	
	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link href="https://fonts.googleapis.com/css?family=Coming+Soon|Covered+By+Your+Grace|Permanent+Marker|EB+Garamond|Cinzel" rel="stylesheet">
	
	<link rel="stylesheet" type="text/css" href="styles.css">
	
	<script type="text/javascript" src="debug_message.php"></script>
	<script type="text/javascript" src="check_browser.php"></script>
	<script type="text/javascript" src="jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="toastr.min.js"></script>
	<script type="text/javascript" src="my_toastr.php"></script>
	<script type="text/javascript" src="connection.php"></script>
	<script type="text/javascript" src="game.php"></script>
	<script type="text/javascript" src="interface.php"></script>
	
	<script type="text/javascript">
		
		setUpConnection();
		
		function start() {
			
			hideErrorElements();
			
			setTimeout(function() {reconnect();}, <?php echo $config->millisPerReconnect; ?>);
			
		}
		
	</script>
	
	<link href="toastr.css" rel="stylesheet"/>
	
</head>

<body onload="start();">

	<div id="float_left_top" style="display: none;" onClick="javascript:onTopLeftBackButton();">
		&#9664;&nbsp;&nbsp;<span id="name_indicator"> </span><!--&nbsp;&nbsp;<span id="room_indicator"><span class="room_character">&#10070;</span>&nbsp;&nbsp;<span class="room_indicator_room">Room</span> 641</span>-->
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