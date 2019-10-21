function signout() {
    if (firebase.auth().currentUser) {
        firebase.auth().signOut();
        window.location = "index.html";
    }
}

function initApp() {
    firebase.auth().onAuthStateChanged(function(user) {
        if (!user) {
            window.location = "index.html";
        }
    });
}