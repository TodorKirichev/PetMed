function fetchAvailableTimes() {
    const selectedDate = document.getElementById("datepicker").value;
    const vetUsername= document.getElementById("vetUsername").value;
    const timeSelect = document.getElementById("availableHours");

    if (!selectedDate) {
        timeSelect.innerHTML = '<option value="">Please select a time</option>';
        return;
    }

    fetch(`/appointments/available-hours?date=${selectedDate}&vetUsername=${vetUsername}`)
        .then(response => response.json())
        .then(data => {
            timeSelect.innerHTML = '';

            if (data.times.length > 0) {
                data.times.forEach(time => {
                    const option = document.createElement("option");
                    option.value = time;
                    option.text = time;
                    timeSelect.appendChild(option);
                });
            } else {
                timeSelect.innerHTML = '<option value="">No available times</option>';
            }
        })
        .catch(error => {
            console.error("Error fetching available times:", error);
            timeSelect.innerHTML = '<option value="">Error loading times</option>';
        });
}

document.getElementById("datepicker").addEventListener("change", fetchAvailableTimes);
