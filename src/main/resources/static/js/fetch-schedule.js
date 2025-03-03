const date = new Date();

const fetchSchedule = () => {
    const formattedDate = date.toLocaleDateString('sv-SE');
    const vetUsername = document.getElementById("vet-username").value;

    document.getElementById("schedule-info").textContent = `Schedule for ${formattedDate}`;

    fetch(`/api/appointments/date?date=${formattedDate}&vetUsername=${vetUsername}`)
        .then(response => response.json())
        .then(data => {
            const scheduleBody = document.getElementById("schedule-body");
            scheduleBody.innerHTML = "";

            if (data.length === 0) {
                scheduleBody.innerHTML = "<tr><td colspan='6'>No appointments</td></tr>";
                return;
            }

            data.forEach(appointment => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${appointment.startTime}</td>
                    <td>${appointment.petName}</td>
                    <td>${appointment.petSpecies}</td>
                    <td>${appointment.petBreed}</td>
                    <td>${appointment.petOwnerFirstName}</td>
                `;

                const buttonCell = document.createElement("td");
                const button = document.createElement("button");
                button.textContent = "Add Medical Record";
                button.classList.add("medical-record-btn");

                button.addEventListener("click", () => {
                    const popup = document.getElementById("popup");
                    const saveButton = document.getElementById("save-record");
                    const closeButton = document.getElementById("close-record");
                    const diagnosisInput = document.getElementById("diagnosis-input");
                    const treatmentTextarea = document.getElementById("treatment-textarea");

                    popup.style.display = "flex";
                    diagnosisInput.value = "";
                    treatmentTextarea.value = "";

                    saveButton.addEventListener("click", () => {
                        const appointmentId = appointment.appointmentId;
                        const diagnosis = diagnosisInput.value;
                        const treatment = treatmentTextarea.value;

                        const data = {
                            appointmentId,
                            diagnosis,
                            treatment
                        };

                        fetch('/api/medical-records', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(data)
                        })
                            .catch(error => {
                                console.error("Error during POST request:", error);
                            })
                            .finally(() => {
                                popup.style.display = "none";
                            });

                    }, { once: true });

                    closeButton.addEventListener("click", closePopup);
                });

                buttonCell.appendChild(button);
                row.appendChild(buttonCell);
                scheduleBody.appendChild(row);
            });
        })
        .catch(error => console.error("Error fetching appointments:", error));
};

const closePopup = () => {
    const popup = document.getElementById("popup");
    popup.style.display = "none";
};

const changeDate = (days) => {
    date.setDate(date.getDate() + days);
    fetchSchedule();
};

document.getElementById("prev-day").addEventListener("click", () => changeDate(-1));
document.getElementById("next-day").addEventListener("click", () => changeDate(1));

fetchSchedule();
