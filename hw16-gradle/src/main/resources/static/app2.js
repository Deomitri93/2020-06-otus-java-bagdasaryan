let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#chatLine").show();
    } else {
        $("#chatLine").hide();
    }
    $("#message").html("");
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
//        stompClient.subscribe('/topic/response.' + $("#roomId").val(), (message) => showMessage(JSON.parse(message.body).messageStr));
        stompClient.subscribe('/topic/response.randomUser', (user) => showMessage(JSON.parse(user.body).messageStr));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => stompClient.send("/app/users.randomUser", {}, JSON.stringify({'getRandomUser': 0}))

const showMessage = (message) => $("#randomUserLabel").value = "Hello world!!!"//message

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#getRandomUser").click(sendMsg);
});
