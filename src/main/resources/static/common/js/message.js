const webSocket = new WebSocket(`ws://${location.host}/msg`);


webSocket.addEventListener("message", function(data) {
    console.log("message:", data);
});
