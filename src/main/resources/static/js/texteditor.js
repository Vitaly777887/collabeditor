'use strict';

var revision;
var textarea = document.getElementById('textarea');
//cookie
$(function () {
    if ($.cookie("nickname")) {
        document.querySelector('#name').value = $.cookie("nickname");
        document.getElementsByClassName("accent username-submit")[0].click();
    }
})

function apply(ot) {

    var text = textarea.value;
    var caret = $("#textarea").caret();
    console.log("OT: " + ot.data);
    if (ot.type == "INSERT") {

        textarea.value = text.substring(0, ot.from) + ot.data + text.substring(ot.from);
        if (caret >= ot.from) {
            $("#textarea").caret(caret + ot.to - ot.from);
        }
        else {
            $("#textarea").caret(caret);
        }
    }
    else if (ot.type == "DELETE") {
        textarea.value = text.substring(0, ot.to) + text.substring(ot.from);
        if (caret > ot.to) {
            $("#textarea").caret(caret + ot.from - ot.to - 2);
        }
        else {
            $("#textarea").caret(caret);
        }
    }
}

function getChar(event) {
    if (event.which == null) {
        if (event.keyCode < 32) return null;
        return String.fromCharCode(event.keyCode)
    }

    if (event.which != 0 && event.charCode != 0) {
        if (event.which < 32) return null;
        return String.fromCharCode(event.which);
    }

    return null;
}

textarea.onkeydown = function (event) {
    if (event.keyCode == 8 && getFilename() != "none" && stompClient) {
        var caret = $("#textarea").caret();

        if (caret != 0) {
            sent("DELETE", " ", caret);
        }
        event.preventDefault();
    }
};

$("#textarea").on('paste cut', function (e) {
    var caret = $("#textarea").caret();
    var data;

    if (e.type == "paste") {
        data = e.originalEvent.clipboardData.getData('text');
        sent("INSERT", data, caret);
    }
    else {
        data = window.getSelection().toString();
        sent("DELETE", data, caret + data.length);
    }
    e.preventDefault();

});

textarea.onkeypress = function (e) {

    if (getFilename() != "none" && stompClient) {

        if (e.code == "KeyZ") {
            return true;
        }
        var caret = $("#textarea").caret();
        var char = getChar(e);
        sent("INSERT", char, caret);
    }
    e.preventDefault();

};

function sent(type, data, caretFrom) {
    var textEditorMessage;

    if (type == "DELETE") {
        textEditorMessage = {
            from: caretFrom,
            to: caretFrom - data.length,
            type: type,
            filename: getFilename(),
            revision: revision
        };
    }
    else if (type == "INSERT") {
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


