document.getElementById("datepicker").addEventListener("change", fetchAvailableTimes);

function fetchAvailableTimes() {
    const selectedDate = document.getElementById("datepicker").value;
    const vetUsername = document.getElementById("vetUsername").value;
    const timeContainer = document.getElementById("availableHoursContainer");
    timeContainer.style.display = "flex";

    fetch(`/api/appointments/available-hours?date=${selectedDate}&vetUsername=${vetUsername}`)
        .then(response => response.json())
        .then(data => {
            timeContainer.innerHTML = '';

            if (data.length > 0) {
                const p = document.createElement("p");
                p.textContent = "Available times:";
                timeContainer.appendChild(p);
                data.forEach(time => {
                    const label = document.createElement("label");
                    label.classList.add('radio-button')
                    label.innerHTML = `
                        <input type="radio" name="time" th:field="*{time}" value="${time}">
                        <span>${time}</span>
                    `;
                    const div = document.createElement("div");
                    div.classList.add('label-container');
                    div.appendChild(label);
                    timeContainer.appendChild(div);
                });
            } else {
                timeContainer.innerHTML = '<p>No available times.</p>';
            }
        });
}
