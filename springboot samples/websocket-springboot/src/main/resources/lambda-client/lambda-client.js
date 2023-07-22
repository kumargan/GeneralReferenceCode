var sjsc = require('sockjs-client');
var Stomp = require('stomp-client');

var StompJs = require('@stomp/stompjs');

//exports.handler = async (event) => {
//    // TODO implement
//    const response = {
//        statusCode: 200,
//        body: JSON.stringify('Hello from Lambda!'),
//    };
//    
//    start();
//    
//    return response;
//};

const client = new StompJs.Client({
	  brokerURL: 'ws://localhost:8080/feed/topic',
	  connectHeaders: {
	    login: 'user',
	    passcode: 'password',
	  },
	  debug: function (str) {
	    console.log(str);
	  },
	  reconnectDelay: 5000,
	  heartbeatIncoming: 4000,
	  heartbeatOutgoing: 4000,
	});

	client.onConnect = function (frame) {
		console.log('coonected: ' + frame.body);
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

function connect() {
	var url = '/feed/ticks';
	var client = sjsc.create(url);
	client.on('connection', function () { // connection is established 
		});
	
	client.on('data', function (msg) { // received some data
		});
	
	client.on('error', function (e) { // something went wrong 
		
	});
}

function subscribe(scrip,stompClient){
	
	var obj = stompClient.subscribe('/topic/'+scrip, function (greeting, headers) {
		console.log(greeting);
	});
	console.log("object value ",obj)
	hashMap[scrip]=obj.id;
}

function randomIntFromInterval(min, max) { // min and max included 
	  return Math.floor(Math.random() * (max - min + 1) + min);
}

function singleConnection(stompClient){
	for (var i = 0; i < 100; i++) {
		subscribe(randomIntFromInterval(0,10000),stompClient);
	}
}

function start(){
	var j=0;
	for(j=0;j<1;j++){
		connect();
		console.log("making connection ",j)
	}
}

//start();

