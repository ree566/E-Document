var stompClient = null;

//        connect();

function setConnected(connected) {
}

function connect() {
    var socket = new SockJS('<c:url value="/socket" />');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage(rowId, action) {
    stompClient.send("/app/chat", {},
            JSON.stringify({'action': action, 'rowId': rowId}));
}

function showMessageOutput(messageOutput) {
    var row = $("#" + messageOutput.rowId);
    if (messageOutput.action == "LOCK") {
        row.addClass("danger");
    } else if (messageOutput.action == "UNLOCK") {
        row.removeClass("danger");
    }
}