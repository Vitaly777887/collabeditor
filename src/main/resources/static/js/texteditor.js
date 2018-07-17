'use strict';

var revision = 0;
var textarea = document.getElementById('textarea');
var $textarea = $("#textarea");

$(function () {
    if ($.cookie("nickname")) {
        document.querySelector('#name').value = $.cookie("nickname");
        document.getElementsByClassName("accent username-submit")[0].click();
    }
});

function fileIsPresent() {
    return getFilename() !== "none";
}

function apply(ot) {
    var text = textarea.value;
    var caret = $textarea.caret();
    console.log("OT: " + ot.data);
    if (ot.type === "INSERT") {

        textarea.value = text.substring(0, ot.from) + ot.data + text.substring(ot.from);
        if (caret >= ot.from) {
            $textarea.caret(caret + ot.to - ot.from);
        }
        else {
            $textarea.caret(caret);
        }
    }
    else if (ot.type === "DELETE") {
        textarea.value = text.substring(0, ot.to) + text.substring(ot.from);
        if (caret > ot.to) {
            $textarea.caret(caret + ot.from - ot.to - 2);
        }
        else {
            $textarea.caret(caret);
        }
    }
}

function sent(type, data, caretFrom) {
    var textEditorMessage;

    if (type === "DELETE") {
        textEditorMessage = {
            from: caretFrom,
            to: caretFrom - data.length,
            type: type,
            filename: getFilename(),
            revision: revision
        };
    }
    else if (type === "INSERT") {
        textEditorMessage = {
            from: caretFrom,
            to: caretFrom + data.length,
            data: data,
            type: type,
            filename: getFilename(),
            revision: revision
        };
    }
    stompClient.send("/app/textArea.sendChange", {}, JSON.stringify(textEditorMessage));
}

$textarea.on("keydown", function (e) {
    if (e.keyCode == 8 && fileIsPresent() && stompClient) {
        var caret = $textarea.caret();

        if (caret) {
            sent("DELETE", " ", caret);
        }
        e.preventDefault();
    }
});

$textarea.on('paste cut', function (e) {

    if (fileIsPresent()) {
        var caret = $textarea.caret();
        var data;

        if (e.type === "paste") {
            data = e.originalEvent.clipboardData.getData('text');
            sent("INSERT", data, caret);
        }
        else {
            data = window.getSelection().toString();
            sent("DELETE", data, caret + data.length);
        }
    }
    e.preventDefault();
});

$textarea.on("keypress", function (e) {

    if (fileIsPresent() && stompClient) {

        if (e.code == "KeyZ") {
            return true;
        }
        var caret = $textarea.caret();
        sent("INSERT", String.fromCharCode(e.keyCode), caret);
    }
    e.preventDefault();
});