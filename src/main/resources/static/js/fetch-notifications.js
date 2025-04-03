document.addEventListener("DOMContentLoaded", function () {
    getNotifications();
});

function getNotifications() {
    const notificationsContainer = document.getElementById("notifications");
    const username = document.getElementById("username").value;
    fetch(`http://localhost:8081/api/notifications/appointments?username=${username}`)
        .then(response => {
            console.log(response);
            return response.json();
        })
        .then(data => {
            if (data.length > 0) {
                notificationsContainer.style.display = "flex";
                console.log(data)
                notificationsContainer.innerHTML = "";
                data.forEach(notification => {
                    const p = document.createElement("p");
                    p.classList.add("notification")
                    p.textContent = `You have upcoming appointment for ${notification.petName} on ${notification.date} at ${notification.time}`
                    notificationsContainer.appendChild(p);
                });
            }
        })
        .catch(error => console.error("Error fetching notifications", error));
}