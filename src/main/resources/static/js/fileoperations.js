'use strict';

function getFilename() {
    return $("#fileName").text().replace("Filename: ", "");
}

//setInterval(saveFile, 20000);

function saveFile() {
    if (getFilename() != "none") {

        var data = new FormData();
        data.append("filename", getFilename());
        $.ajax({
            url: "/saveFile",
            type: "POST",
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                console.log("File: " + getFilename() + " is saved");
            },
            error: function () {
                alert("fail")
            }
        })
    }
}

$("#new").click(function (e) {
    e.preventDefault();

    var filename = prompt("Enter filename");
    var data = new FormData();
    data.append("filename", filename);
    $.ajax({
        url: "/newFile",
        type: "POST",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            leave();
            textarea.value = "";
            revision = 0;
            $("#fileName").text("Filename: " + response);
            console.log("new rev= ", revision);
            join();
        },
        error: function () {
            alert("fail")
        }
    })
});

$("#upload").click(function (e) {
    e.preventDefault();

    $('#fileupload').trigger('click');

});
$("#fileupload").change(function (e) {
    e.preventDefault();

    var file = fileupload.files[0];
    var reader = new FileReader;
    if (file.size < 1000000) {

        reader.onloadend = function (evt) {
            textarea.value = reader.result;
        };
        reader.readAsText(file, 'cp1251'); // for windows

        var formData = new FormData();
        formData.append('file', file, file.name.replace(".txt", ""));
        $.ajax({
            url: "/uploadFile",
            type: "POST",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (response) {
                leave();
                $("#fileName").text("Filename: " + response);
                revision = 0;
                console.log("load rev= ", revision);
                join();
            },
            error: function () {
                alert("fail")
            }
        })
    }
    else {
        alert("Too big file");
    }
});

$("#open").click(function (e) {
    e.preventDefault();
    $("#open").hide();
    $("#chooseFile").show();
    $.ajax({
        url: "/listFiles",
        type: "GET",
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            response.forEach(function (item, i, arr) {
                    $("#chooseFile").append(new Option(item, item));
                }
            )
        },
        error: function () {
            alert("fail")
        }
    })
});
$("#chooseFile").change(function () {
    var filename = this.value;
    var data = new FormData();
    data.append("filename", filename);
    $.ajax({
        url: "/chooseFile",
        type: "POST",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            leave();
            textarea.value = response[0];
            revision = response[1];
            $("#fileName").text("Filename: " + filename);
            console.log("choose rev= ", revision);
            join();
        },
        error: function () {
            alert("fail")
        }
    })
    $("#chooseFile option[value!='0']").remove();
    $("#open").show();
    $("#chooseFile").hide();
});