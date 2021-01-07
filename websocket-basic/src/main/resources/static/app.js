const hashMap = new Object();
var socket=null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	socket = new WebSocket('ws://localhost:8080/ticks');
	//const socket = new WebSocket(uri);
	//Connection opened
	socket.addEventListener('open', function (event) {
	    setConnected(true);
	    console.log('Connection open');
	});
}


function subscribe(scrip){
if(socket==null){
connect();
}
console.log(scrip);
    var data = JSON.stringify({ "pmlId": scrip, "type":'S' });
    var obj = socket.send(data, function () {
         console.log("Data sent",data);
    });
  // Listen for messages
  	socket.addEventListener('message', function (event) {
  	    console.log('Message from server ', event.data);
  	     //create a JSON object
              var jsonObject = JSON.parse(event.data);
              var status = jsonObject.status;
            showGreeting(status);
  });

}

function unsubscribe(scrip){
    stompClient.unsubscribe(hashMap[scrip]);
}

function disconnect() {
    if (socket !== null) {
        socket.close();
        setConnected(false);
        console.log("Disconnected");
    }

}

function sendName(scrip) {
    stompClient.send("/app/hello1", {}, JSON.stringify({'name': $("#name").val()}));
	
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#subscribe" ).click(function() { 
    	var scrip = $('#name').val();
    	console.log('subscribe ',scrip);	
    	subscribe(scrip);
    	});
    $( "#unsubscribe" ).click(function() { 
    	var scrip = $('#name').val();
    	console.log('unsubscribe ',scrip);	
    	unsubscribe(scrip); 
    	});
});

