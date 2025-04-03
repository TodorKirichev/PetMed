document.addEventListener("DOMContentLoaded", function () {
    getNotifications();
});

function getNotifications() {
    const notificationsContainer = document.getElementById("notifications");
    const username = document.getElementById("username").value;

    fetch(`http://localhost:8081/api/notifications/appointments?username=${username}`)
        .then(response => response.json())
        .then(data => renderNotifications(notificationsContainer, data))
        .catch(error => console.error("Error fetching notifications", error));
}

function renderNotifications(notificationsContainer, data) {
    if (data.length > 0) {
        notificationsContainer.style.display = "flex";
        notificationsContainer.innerHTML = "";
        data.forEach(notification => {
            const notificationWrapper = document.createElement("div");
            const p = document.createElement("p");
            const span = document.createElement("span");
            notificationWrapper.classList.add("notification-wrapper");
            span.classList.add("close-x");
            span.id = "close-x";
            span.addEventListener("click", function () {
                closeNotification(notification.notificationId)
            });
            p.classList.add("notification")
            p.textContent = `You have upcoming appointment for ${notification.petName} on ${notification.date} at ${notification.time}`
            notificationWrapper.appendChild(p);
            notificationWrapper.appendChild(span)
            notificationsContainer.appendChild(notificationWrapper);
        });
    }
}

function closeNotification(notificationId) {
    console.log(notificationId)
    fetch(`http://localhost:8081/api/notifications/close-notification?notificationId=${notificationId}`)
        .then(data => {
            window.location.reload();
        })
        .catch(error => console.error("Error fetching notifications", error));
}