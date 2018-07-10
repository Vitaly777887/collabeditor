'use strict';

var revision;
var textarea = document.getElementById('textarea');
var qtextarea = $("#textarea");

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
    var caret = qtextarea.caret();
    console.log("OT: " + ot.data);
    if (ot.type === "INSERT") {

        textarea.value = text.substring(0, ot.from) + ot.data + text.substring(ot.from);
        if (caret >= ot.from) {
            qtextarea.caret(caret + ot.to - ot.from);
        }
        else {
            qtextarea.caret(caret);
        }
    }
    else if (ot.type === "DELETE") {
        textarea.value = text.substring(0, ot.to) + text.substring(ot.from);
        if (caret > ot.to) {
            qtextarea.caret(caret + ot.from - ot.to - 2);
        }
        else {
            qtextarea.caret(caret);
        }
    }
}

function getChar(e) {
    if (e.which == null) {
        if (e.keyCode < 32) return null;
        return String.fromCharCode(e.keyCode)
    }

    if (e.which != 0 && e.charCode != 0) {
        if (e.which < 32) return null;
        return String.fromCharCode(e.which);
    }

    return null;
}

textarea.onkeydown = function (e) {
    if (e.keyCode == 8 && fileIsPresent() && stompClient) {
        var caret = qtextarea.caret();

        if (caret != 0) {
            sent("DELETE", " ", caret);
        }
        e.preventDefault();
    }
};

qtextarea.on('paste cut', function (e) {
    if (fileIsPresent()) {

        var caret = qtextarea.caret();
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

textarea.onkeypress = function (e) {

    if (fileIsPresent() && stompClient) {

        if (e.code == "KeyZ") {
            return true;
        }
        var caret = qtextarea.caret();
        var char = getChar(e);
        sent("INSERT", char, caret);
    }
    e.preventDefault();

};

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