#Lightcycle (Tron 'Inspired' Game)

A little tron game made in java.  If you don't know what a tron game is, look it up!

Requires a server to connect to.  Doesn't actually have a .config file for this, just gotta hard code it. 
The networking is pretty awful too.  Local games are fun though

###How to Run

**Server:** run `java TronDaemon` once compiled, just so the client won't fail

**Client:** inside LoginBox (client/LoginBox.java), under the top constructor, change the server address to 
whatever you're using for a server (probably should be your own computer, owing to the fairly awful networking
that this uses).  Then, just compile and run `java Tron`, and go on your merry way
