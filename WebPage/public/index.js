function login() {
    var rect_email = document.getElementById("email");
    var rect_password = document.getElementById("password");
    var email = rect_email.value;
    var password = rect_password.value;
    if (email.length < 4 && password.length < 4) {
        alert("Please enter valid email and password");
        rect_email.focus();
        rect_email.style.border = "1px solid red";
        rect_password.style.border = "1px solid red";
    } else if (email.length < 4) {
        alert("Please enter valid email");
        rect_email.focus();
        rect_email.style.border = "1px solid red";
    } else if (password.length < 4) {
        alert("Please enter valid password");
        rect_password.focus();
        rect_password.style.border = "1px solid red";
    } else {
        firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
            // Handle Errors here.
            var errorCode = error.code;
            var errorMessage = error.message;
            // [START_EXCLUDE]
            if (errorCode === 'auth/wrong-password') {
                alert('Wrong password.');
            } else {
                alert(errorMessage);
            }

        });
        firebase.auth().onAuthStateChanged(function(user) {
            if (user) {
                window.location = "divpage.html";
            } else {
                document.getElementById("email").focus();
            }
        });
    }
}

function check_length(param1) {
    var val = document.getElementById(param1);
    if (val.value.length > 4) {
        val.style.border = "";
    } else {
        val.style.border = "1px solid red";
    }
}


function initApp() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            window.location = "divpage.html";
        } else {
            document.getElementById("email").focus();
        }
    });
}