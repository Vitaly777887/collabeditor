'use strict';

var $chooseFile = $("#chooseFile"),
    $fileUpload = $("#fileUpload"),
    $fileName = $("#fileName"),
    $open = $("#open"),
    $new = $("#new"),
    $upload = $("#upload"),
    $saveRevision = $("#saveRevision"),
    $openRevision = $("#openRevision"),
    $chooseFileRevision = $("#chooseFileRevision");

function getFilename() {
    return $fileName.text().replace("Filename: ", "");
}

function getListFiles() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/listFiles', false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        return xhr.responseText;
    }
}

$new.on("click", function (e) {
    e.preventDefault();

    var filename = prompt("Enter filename");
    if (filename == null) {
        return;
    }
    //err
    if (getListFiles().includes(filename)) {
        alert("Filename is present. Change filename");
        return;
    }
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
            $fileName.text("Filename: " + response);
            console.log("new rev= ", revision);
            join();
            hideRevisionName();
        },
        error: function () {
            alert("fail")
        }
    })
});

$upload.on("click", function (e) {
    e.preventDefault();
    $fileUpload.trigger('click');
});

$fileUpload.on("change", function (e) {
    e.preventDefault();

    var file = fileUpload.files[0];
    var reader = new FileReader;
    if (file.size < 1000000) {

        reader.onloadend = function () {
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
                $fileName.text("Filename: " + response);
                revision = 0;
                console.log("load rev= ", revision);
                join();
                hideRevisionName();
            },
            error: function () {
                alert("fail uploadFile")
            }
        })
    }
    else {
        alert("Too big file");
    }
});

$open.on("click", function (e) {
    e.preventDefault();
    $open.hide();
    $chooseFile.show();
    $.ajax({
        url: "/listFiles",
        type: "GET",
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            $("#chooseFile option[value!='0']").remove();
            response.forEach(function (item, i, arr) {
                $chooseFile.append(new Option(item, item));
                }
            )
        },
        error: function () {
            alert("fail")
        }
    })
});

$chooseFile.on("change", function () {
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
            $fileName.text("Filename: " + filename);
            console.log("choose rev= ", revision);
            join();
            hideRevisionName();
        },
        error: function () {
            alert("fail chooseFile")
        }
    });
    $open.show();
    $chooseFile.hide();
});

$chooseFile.on("blur", function () {
    $open.show();
    $chooseFile.hide();
});

$openRevision.on("click", function (e) {
    e.preventDefault();
    $openRevision.hide();
    $chooseFileRevision.show();
    var data = new FormData();
    data.append("filename", getFilename());
    $.ajax({
        url: "/listFileRevisions",
        type: "POST",
        cache: false,
        data: data,
        contentType: false,
        processData: false,
        success: function (response) {
            $("#chooseFileRevision option[value!='0']").remove();
            response.forEach(function (item, i, arr) {
                    $chooseFileRevision.append(new Option(item, item));
                }
            )
        },
        error: function () {
            alert("fail openRevision")
        }
    })
});

$chooseFileRevision.on("change", function () {
    var fileRevisionName = this.value;
    var data = new FormData();
    data.append("name", fileRevisionName);
    $.ajax({
        url: "/chooseFileRevisions",
        type: "POST",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            textarea.value = response;
            console.log("choose filerev= ", revision);
            setRevisionNameAndShow(fileRevisionName);
        },
        error: function () {
            alert("fail chooseFileRevisions")
        }
    });
    $openRevision.show();
    $chooseFileRevision.hide();
});

$chooseFileRevision.on("blur", function () {
    $openRevision.show();
    $chooseFileRevision.hide();
});

$saveRevision.on("click", function () {
    if (!isNotRevisionFile()) {
        return;
    }
    var name = prompt("Enter name of revision", getFilename() + " rev:" + revision + " date:" + new Date().toDateString());
    if (name == null) {
        return;
    }
    var formData = new FormData();

    formData.append('filename', getFilename());
    formData.append('revision', revision);
    formData.append("name", name);
    $.ajax({
        url: "/saveFileRevision",
        type: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (response) {
            console.log("save fileRev= ", response);
        },
        error: function () {
            alert("fail saveRevision")
        }
    })
});

