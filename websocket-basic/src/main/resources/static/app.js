const hashMap = new Object();
var socket=null;
let pmlIdMap = new Map();
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
	socket = new WebSocket('ws://'+ document.location.host + '/ticks');
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
  	     //create a JSON object
              var jsonObject = JSON.parse(event.data);
              var status = jsonObject.status;
              if(pmlIdMap.has(status)){
                 pmlIdMap.set(status, pmlIdMap.get(status)+1);
              }
              else{
                 pmlIdMap.set(status, 1);
              }
              showSubscriptionData(pmlIdMap);
            //showGreeting(status);
  });

}

function showSubscriptionData(map){
   var mapStr = "<tr><td>"
                       + "PML ID" + "</td>" +
                       "<td>" + "Message Count" +
                        "</td></tr>";
   for (let [key, value] of map.entries()) {
      mapStr = mapStr.concat("<tr><td>"
      + key + "</td>" +
      "<td>" + value +
       "</td></tr>");
   }
   //console.log(mapStr);
   greetings.innerHTML = mapStr;
   //$("#greetings").replaceAll(mapStr);
}
function unsubscribe(scrip){
  console.log(scrip);
      var data = JSON.stringify({ "pmlId": scrip, "type":'U' });
      var obj = socket.send(data, function () {
           console.log("Data sent",data);
      });
}

function disconnect() {
    if (socket !== null) {
        socket.close();
        setConnected(false);
        console.log("Disconnected");
        pmlIdMap = new Map();
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

