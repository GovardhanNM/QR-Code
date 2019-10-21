var uid;

function isURL() {
    var str = document.getElementById("link");
    var p = (/(http:|https:)?\/\/(www\.)?(youtube.com|youtu.be)\/(watch)?(\?v=)?(\S+)?/);
    if (str.value.match(p)) {
        str.style.border = "";
        return true;
    }
    str.style.border = "1px solid red";
    return false;
}

function check_length(param) {
    var str = document.getElementById(param);
    if (str.value.length < 4) {
        str.style.border = "1px solid red";
    } else {
        str.style.border = "";
    }
}



function check_param(name, image, file) {
    if (name.value.length < 4) {
        name.style.border = "1px solid red";
        alert("Name should atleast be 4 characters")
        return false;
    } else {
        name.style.border = "";
    }
    if (!isURL()) {
        alert("Please provide the link of Youtube video");
        return false;
    }
    if (image.value.length == 0) {
        alert("Please choose a image")
        image.style.border = "1px solid red";
        return false;
    } else {
        image.style.border = "";
    }
    if (file.value.length == 0) {
        alert("Please choose a file");
        file.style.border = "1px solid red";
        return false;
    } else {
        file.style.border = "";
    }
    return true;
}

function upload_image(image_name, file_name, name_field, link) {
    var bar = document.getElementById("myProgress");
    bar.style.display = "block";
    var elem = document.getElementById("myBar");
    var para = document.getElementById("QR");
    var width = 1;
    var image_url;
    var storage = firebase.storage();
    var storageRef = storage.ref(uid + '/' + image_name.name);
    para.innerHTML = "Uploading Data";
    var uploadTask = storageRef.put(image_name);

    uploadTask.on('state_changed', function(snapshot) {
        if (width >= 100) {
            clearInterval(id);
        } else {
            width = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
            elem.style.width = width + '%';
        }
        switch (snapshot.state) {
            case firebase.storage.TaskState.PAUSED: // or 'paused'
                console.log('Upload is paused');
                break;
            case firebase.storage.TaskState.RUNNING: // or 'running'
                console.log('Upload is running');
                break;
        }
    }, function(error) {
        // Handle unsuccessful uploads
    }, function() {
        uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {});
    });
}


function upload_file(file_name, name_field, link, image_name) {
    var user = firebase.auth().currentUser.uid;
    console.log(user);
    var database = firebase.database();
    if (file_name) {
        var reader = new FileReader();

        reader.onload = function(e) {
            data = reader.result;
            database.ref("users/" + user).child(name_field).set({ data: data, name: image_name.name, link: link });
        }
    };
    reader.readAsText(file_name);
}

function validate() {
    var name = document.getElementById("name");
    var link = document.getElementById("link");
    var image = document.getElementById("myimage");
    var file = document.getElementById("myfile");

    var flag = check_param(name, image, file);

    if (flag) {
        var image_name = image.files[0];
        var file_name = file.files[0];
        upload_image(image_name, file_name, name.value, link.value);
        upload_file(file_name, name.value, link.value, image_name);
        var para = document.getElementById("QR");
        document.getElementById("myProgress").style.display = "none";
        para.innerHTML = "Your QR Code is ready to Save!!!";
        document.getElementById("saveqr").style.display = "block";
        document.getElementById("qr").style.display = "block";
        var qr = new QRious({
            element: document.getElementById('qr'),
            value: '{ "name":"' + name.value + '", "uid":"' + uid + '" }',
            background: 'white',
            foreground: 'black',
            backgroundAlpha: 1,
            foregroundAlpha: 1,
            level: 'L',
            mime: 'image/png',
            size: 250,
            padding: null
        });
    }
}

function saveQR() {
    var name = document.getElementById("name").value;
    var canvas = document.getElementById('qr');
    var image = canvas.toDataURL("image/jpeg");
    var link = document.createElement('a');
    link.download = name + ".jpg";
    link.href = canvas.toDataURL("image/jpg").replace("image/png", "image/octet-stream");
    link.click();
}

window.onload = function() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            uid = firebase.auth().currentUser.uid;
        }
    });
};