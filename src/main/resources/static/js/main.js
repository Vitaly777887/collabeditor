'use strict';

var usernameForm = document.querySelector('#usernameForm'),
    messageForm = document.querySelector('#messageForm'),
    messageInput = document.querySelector('#message'),
    messageArea = document.querySelector('#messageArea'),
    connectingElement = document.querySelector('.connecting'),
    textarea = document.querySelector("#textarea"),
    fileUpload = document.querySelector("#fileUpload");

var stompClient,
    username;

var $chatEditors = $("#chat-editors");
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
    if (!fileIsPresent()) {
        return;
    }
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, filename: getFilename(), type: 'JOIN'})
    )
}

function leave() {
    if (!fileIsPresent()) {
        return;
    }
    while (messageArea.firstChild) {
        messageArea.removeChild(messageArea.firstChild);
    }
    $chatEditors.text("Editors:");
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, filename: getFilename(), type: 'LEAVE'})
    )
}

function onError() {
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

    if (!fileIsPresent() || getFilename() != message.filename) {
        return;
    }
    if (message.type === DELETE || message.type === INSERT) {
        apply(message);
        revision = message.revision;
        return;
    }
    var messageElement = document.createElement('li');
    if (message.type === 'JOIN') {
        $chatEditors.text($chatEditors.text().concat(" " + message.sender));
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        $chatEditors.text($chatEditors.text().replace(" " + message.sender), "");
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