window.onload = function () {
    let message = document.getElementById("message");
    if (message) {
        setTimeout(() => {
            message.style.display = "none";
        }, 3000);
    }
};