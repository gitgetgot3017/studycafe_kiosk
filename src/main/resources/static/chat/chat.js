const socket = new WebSocket('ws://localhost:8080/chat');

// 서버로부터 메시지를 수신할 때 호출되는 콜백 함수
socket.onmessage = function(event) {

    const messages = JSON.parse(event.data);

    if (Array.isArray(messages)) {
        messages.forEach(message => {
            console.log(message);
            appendMessage(message);
        });
    } else {
        console.log(event.data);
        appendMessage(event.data);
    }
};

function appendMessage(message) {
    const messageDiv = document.getElementById("chat-body");
    const newMessage = document.createElement("div");
    newMessage.textContent = message.content;
    messageDiv.appendChild(newMessage);
}