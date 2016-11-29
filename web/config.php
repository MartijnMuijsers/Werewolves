<?php

const SERVER_IP = 'localhost';
const SERVER_PORT = 9907;
const BROWSER_ERROR_PAGE = 'error.php';
const MILLIS_PER_RECONNECT = 1000;
const MILLIS_FOR_RECONNECT_MESSAGE = 8000;
const GAME_NAME = "Werewolves";
const IMAGES_PATH = "images/";
const DEFAULT_GAME_CODE = "641";
const ROLE_PACK = "enter_role_pack_here";
const DEBUG_MESSAGES_ON = false;

class Config {
	
	public $serverIP = SERVER_IP;
	public $serverPort = SERVER_PORT;
	public $browserErrorPage = BROWSER_ERROR_PAGE;
	public $millisPerReconnect = MILLIS_PER_RECONNECT;
    public $millisForReconnectMessage = MILLIS_FOR_RECONNECT_MESSAGE;
	public $gameName = GAME_NAME;
	public $imagesPath = IMAGES_PATH;
	public $defaultGameCode = DEFAULT_GAME_CODE;
	public $rolePack = ROLE_PACK;
	public $debugMessagesOn = DEBUG_MESSAGES_ON;
	
}

$config = new Config();

?>