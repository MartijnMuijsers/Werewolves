<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

function debug_message(message) {
	<?php
	if ($config->debugMessagesOn) {
		echo "console.log(message);";
	}
	?>
}