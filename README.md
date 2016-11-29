# **<center>Werewolves</center>**
*<center>A server and client to play the popular game Werewolves using phones or other devices</center>*

## Components
### Java server
A server that handles the state of players (such as choosing username, choosing game) and serves the cards. A server can have multiple games (with different game codes) running at the same time.
### Web files
A collection of files (*.php* amongst others) to be served by a web server such as Apache. Note that part of these files is a resource pack that has the images for roles. These resource packs are not included in this project, since they may be licensed differently.

## How to play
### Running the server
Compile the Java server from source and run it (for example as a *.jar* file). Also, start a web server such as Apache and let it serve the web files (make sure that you have added a resource pack). There are some configurations to be set in both, specifically the preferred server ip and port.
### Managing the games
The server has a **/help** command that shows all available commands. There are commands to create a game, and later start it when enough players have joined.
### Joining a game
Players can use their device to open the website (using the ip pointing to the web server started earlier). They can then easily join the game using the right game code.