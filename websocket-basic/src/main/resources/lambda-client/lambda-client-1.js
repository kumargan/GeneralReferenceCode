var sjsc = require('sockjs-client');
var StompJs = require('stomp-client');

const client = new StompJs.Client();
client.brokerURL = 'ws://localhost:15674/ws';

console.log(client.brokerURL);

	// Fallback code
	if (typeof WebSocket !== 'function') {
	  // For SockJS you need to set a factory that creates a new SockJS instance
	  // to be used for each (re)connect
	  client.webSocketFactory = function () {
	    // Note that the URL is different from the WebSocket URL
		  new SockJS('http://127.0.0.1:8080/feed/ticks');
	  };
	}
	
	client.onConnect = function (frame) {
		  // Do something, all subscribes must be done is this callback
		  // This is needed because this will be executed after a (re)connect
		};

		client.onStompError = function (frame) {
		  // Will be invoked in case of error encountered at Broker
		  // Bad login/passcode typically will cause an error
		  // Complaint brokers will set `message` header with a brief message. Body may contain details.
		  // Compliant brokers will terminate the connection after any error
		  console.log('Broker reported error: ' + frame.headers['message']);
		  console.log('Additional details: ' + frame.body);
		};

		client.activate();