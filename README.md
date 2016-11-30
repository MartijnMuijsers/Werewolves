# **<center>Werewolves</center>**
*<center>A server and client to play the popular game Werewolves using phones or other devices</center>*

## Components

### Java server

A server that handles the state of players (such as choosing username, choosing game) and serves the cards. A server can have multiple games (with different game codes) running at the same time.
### Web files

A collection of files (*.php* amongst others) to be served by a web server such as Apache. Note that part of these files is a resource pack that has the images for roles. These resource packs are not included in this project, since they may be licensed differently.

## Requirements

* Pretty much any version of Windows, Linux, Mac or FreeBSD and most other operating systems works
* Java 8 or higher
* Apache, Tomcat or another web server supporting PHP
* *If compiling from source*:
 * Maven

## Installation instructions

### Getting *werewolves.jar*
You can either download the latest version of Werewolves [here](https://mega.nz/#!rsRDxKwY!iIVD5TgC6lLLMmXmGjK6LYUz98ycYfQeFXmTauID9qE "Click to download werewolves.jar"), or you can compile it from source. If you compile from source, you will need Maven.

### Running the server
You can run the *werewolves.jar* from any terminal or console using *java -jar werewolves.jar*. The first time you run the server, it will generate a number of files, such as *config.txt* (which has the configuration) and a *role_packs* folder. It will automatically close again, since there is no role pack added yet.

### Starting the web server
Additionally, start a web server (such as Apache) with the web server files (in Apache, that would be in the *htdocs* folder, for other servers it may be in *public_html* or something similar).

### Adding a role pack
You must add a role pack to play Werewolves. No role pack is included at the moment (for good reasons) but there is a separate role pack downloadable [here](https://mega.nz/#!SoxEmCba!Dj8HDGk16LIR05_CQpcuBNwDeXEaoN2PJkt0pKSddxI "Click to download role pack"), which has a *README.txt* file with instructions how to set up the role pack (it's just a bit of copying and a few configuration edits).

### Managing the game
Once you've added a role pack, you can start the server (*werewolves.jar*) again. The server has a **help** command that shows all (well, most) available commands. These are the ones that you will want to use right away:
* **create** creates a new game that players will be able to join (the game code of this new game will be displayed)
* **synccards &lt;code&gt;** synchronize the cards of a game with a given code with the roles that are in a file
* **start &lt;code&gt;** starts a game (will show all the players in the game their card)
* **stop &lt;code&gt;** stops a game (can then be started again)

The current only way to choose which cards to distribute in a game is to use the **synccards &lt;code&gt;** command. This will load the cards from the auto-generated file *cards_to_sync.txt*. Below is an example of the content of *cards_to_sync.txt* when used with the Werewolves of Heide role pack:

```
citizen
citizen
citizen
citizen
citizen
werewolf
werewolf
werewolf
witch
seer
hunter
```

Starting a game will also show the dealt cards and undealt cards (if there are more cards than players) in the console.

### Joining a game
Players can use their device to open the website (using the ip pointing to the web server started earlier). They can then easily join the game using the right game code.

There are various commands available to check the status of players and which players are in which game.

## Dependencies

### Maven dependencies
* Java WebSocket
* Lombok

### JavaScript libraries
* jQuery
* Toastr

### Other
* Various Google fonts