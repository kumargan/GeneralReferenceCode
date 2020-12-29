var Stomp = require('stompjs');

// Use raw TCP sockets
//var client = Stomp.overTCP('localhost', 8080);
var client = Stomp.overWS('ws://localhost:8080/feed/ticksju');
// uncomment to print out the STOMP frames
client.debug = console.log;

client.connect('user', 'password', function(frame) {
  console.log('connected to Stomp');

  client.subscribe('/topic/100', function(message) {
    console.log("received message " + message.body);

    // once we get a message, the client disconnects
    //client.disconnect();
  });

});