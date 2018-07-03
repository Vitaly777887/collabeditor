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
    console.log("OT: " + ot.data)
    if (ot.type == "INSERT") {

        textarea.value = text.substring(0, ot.from) + ot.data + text.substring(ot.from);
        $("#textarea").caret(caret + ot.from - ot.to + 1);
    }
    else if (ot.type == "DELETE") {
        textarea.value = text.substring(0, ot.to) + text.substring(ot.from);
        $("#textarea").caret(caret - ot.from + ot.to + 1)
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

setInterval(checkChanges, 700);

function checkChanges() {
    if (getFilename() != "none"&&getFilename()!=null) {

        var formData = new FormData();
        formData.append("type", "CHECK");
        formData.append("data", "")
        formData.append("filename", getFilename());
        formData.append("revision", revision);
        formData.append("from", 0);
        formData.append("to", 0);
        $.ajax({
            url: "/ot",
            type: "POST",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                revision += response.length;
                response.forEach(function (ot) {
                    apply(ot)
                });
                console.log("rev " + revision + response);
            },
            error: function () {
                console.log("fail")
            }
        })
    }
}

textarea.onkeyup = function (event) {
    if (event.keyCode == 8) {

        var caret = $("#textarea").caret();
        var formData = new FormData();
        formData.append("type", "DELETE");
        formData.append("data", "");
        formData.append("from", caret + 1);
        formData.append("to", caret);
        formData.append("filename", getFilename());
        formData.append("revision", revision);
        $.ajax({
            url: "/ot",
            type: "POST",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                revision += response.length + 1;
                response.forEach(function (ot) {
                    apply(ot)
                });
                console.log("rev " + revision + response);
            },
            error: function () {
                alert("fail")
            }
        })
    }

};

textarea.onkeypress = function (e) {
    if (getFilename() != "none") {

        var char = getChar(e);
        var caret = $("#textarea").caret();
        var formData = new FormData();
        formData.append("type", "INSERT");
        formData.append("data", char);
        formData.append("from", caret);
        formData.append("to", caret + 1);
        formData.append("filename", getFilename());
        formData.append("revision", revision);
        $.ajax({
            url: "/ot",
            type: "POST",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                revision += response.length + 1;
                response.forEach(function (ot) {
                    apply(ot)
                });
                console.log("rev " + revision + response);
            },
            error: function () {
                alert("fail")
            }
        })
    }
}

