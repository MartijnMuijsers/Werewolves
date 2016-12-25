<?php

header('Content-Type: text/javascript');

?>

<?php

include_once "config.php";

?>

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