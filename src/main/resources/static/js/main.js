'use strict';

var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var textarea = document.querySelector("#textarea");
var fileupload = document.querySelector("#fileupload");
var revision = 0;

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();
    if (!$.cookie("nickname")) {
        $.cookie("nickname", username);
    }
    if (username) {
        $("#username-page").hide();
        $("#main-page").show();

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
    connectingElement.classList.add('hidden');
}

function join() {
    if (getFilename() == "none") {
        return;
    }
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, filename: getFilename(), type: 'JOIN'})
    )
}

function leave() {
    if (getFilename() == "none") {
        return;
    }
    while (messageArea.firstChild) {
        messageArea.removeChild(messageArea.firstChild);
    }
    $("#chat-editors").text("Editors:")
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, filename: getFilename(), type: 'LEAVE'})
    )
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            filename: getFilename()
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (getFilename() == "nono" || getFilename() != message.filename) {
        return;
    }
    if (message.type === 'DELETE' || message.type === 'INSERT') {
        apply(message);
        revision = message.revision;
        return;
    }
    var messageElement = document.createElement('li');
    if (message.type === 'JOIN') {
        $("#chat-editors").text($("#chat-editors").text().concat(" " + message.sender));
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        $("#chat-editors").text($("#chat-editors").text().replace(" " + message.sender), "");
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';

    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);