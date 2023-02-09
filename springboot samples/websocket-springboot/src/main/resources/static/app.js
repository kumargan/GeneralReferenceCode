var stompClient = null;
const hashMap = new Object();

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
    var socket = new SockJS('/feed/ticks');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        stompClient.subscribe('/user/queue/reply', function (greeting) {
            showGreeting(greeting.body);
        });
    });
}

function subscribe(scrip){
    var obj = stompClient.subscribe('/topic/'+scrip, function (greeting) {
        showGreeting(greeting.body);
    });
    console.log("object value ",obj)
    hashMap[scrip]=obj.id;
}

function unsubscribe(scrip){
    stompClient.unsubscribe(hashMap[scrip]);
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
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

