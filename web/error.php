<?php

include_once "config.php";

?>

<html>

<head>
	
	<title><?php echo $config->gameName; ?></title>
	
	<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link href="https://fonts.googleapis.com/css?family=Coming+Soon|Covered+By+Your+Grace|Permanent+Marker|EB+Garamond|Cinzel" rel="stylesheet">
	
	<link rel="stylesheet" type="text/css" href="styles.css">
    
</head>

<body>
    
    <div id="error_vertical_centered">
	<div id="errordiv"><span id="errorspan"><span class="decent_font"><span class="title"><?php echo $config->gameName; ?></span><br><br>Oh no, something went wrong! Please <a href="index.php">try again</a> in the <a href="https://www.google.com/chrome" target="_blank">latest version of Google Chrome</a>, other browsers may not support all functions.</span></span></div>
    </div>
    
</body>

</html>