function display_input(param1) {
    var x = document.getElementById(param1);
    if (x.style.display == "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
}

function check_files(image, file) {
    var link_hidden = document.getElementById("link_hidden");
    var image_hidden = document.getElementById("image_hidden");
    var file_hidden = document.getElementById("file_hidden");

    if (link_hidden.style.display != "block" && image_hidden.style.display != "block" && file_hidden.style.display != "block") {
        alert("Nothing to Update");
        return false;
    }
    if (link_hidden.style.display == "block" && !isURL()) {
        alert("Please provide the link of Youtube video");
        return false;
    }
    if (image_hidden.style.display == "block" && image.value.length == 0) {
        alert("Please choose a image")
        image.style.border = "1px solid red";
        return false;
    } else {
        image.style.border = "";
    }
    if (file_hidden.style.display == "block" && file.value.length == 0) {
        alert("Please choose a file");
        file.style.border = "1px solid red";
        return false;
    } else {
        file.style.border = "";
    }
    return true;
}

function update_info() {
    var link = document.getElementById("link");
    var image = document.getElementById("myimage");
    var file = document.getElementById("myfile");

    var flag = check_files(image, file);

    if (flag == true) {
        alert("Form will be submitted");
    }
}