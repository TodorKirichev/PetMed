document.addEventListener("DOMContentLoaded", () => {
    const today = new Date();
    const formattedDate = today.toLocaleDateString("en-GB").split("/").join("-");
    fetchSchedule(formattedDate);
});

flatpickr("#datepicker", {
    dateFormat: "d-m-Y",
    locale: {
        firstDayOfWeek: 1
    },
    inline: true,
    defaultDate: new Date(),
    onChange: function(selectedDates, dateStr) {
        fetchSchedule(dateStr);
    }
});

const fetchSchedule = (selectedDate) => {
    const vetUsername = document.getElementById("vet-username").value;
    document.getElementById("schedule-info").textContent = `Schedule for ${selectedDate}`;

    fetch(`/api/appointments/date?date=${selectedDate}&vetUsername=${vetUsername}`)
        .then(response => response.json())
        .then(data => renderSchedule(data))
        .catch(error => console.error("Error fetching appointments:", error));
};

const renderSchedule = (appointments) => {
    const scheduleBody = document.getElementById("schedule-body");
    scheduleBody.innerHTML = "";

    if (appointments.length === 0) {
        scheduleBody.innerHTML = "<tr><td colspan='6'>No appointments</td></tr>";
        return;
    }

    appointments.forEach(appointment => {
        const row = createAppointmentRow(appointment);
        scheduleBody.appendChild(row);
    });
};

const createAppointmentRow = (appointment) => {
    const row = document.createElement("tr");
    if (appointment.petName != null) {
        row.innerHTML = `
        <td>${appointment.startTime}</td>
        <td>${appointment.petName}</td>
        <td>${appointment.petSpecies}</td>
        <td>${appointment.petBreed}</td>
        <td>${appointment.petOwnerName}</td>
    `;
        const today = new Date().toDateString();
        const appointmentDate = new Date(appointment.date).toDateString();
        if (appointmentDate === today && !appointment.hasMedicalRecord) {
            const buttonCell = document.createElement("td");
            const button = createMedicalRecordButton(appointment);
            buttonCell.appendChild(button);
            row.appendChild(buttonCell);
        } else {
            row.appendChild(document.createElement("td"));
        }

    } else {
        row.innerHTML = `
        <td>${appointment.startTime}</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    `;
    }
    return row;
};

const createMedicalRecordButton = (appointment) => {
    const button = document.createElement("button");
    button.textContent = "Add Medical Record";
    button.classList.add("medical-record-btn");

    button.addEventListener("click", () => openMedicalRecordPopup(appointment));

    return button;
};

const openMedicalRecordPopup = (appointment) => {
    const popup = document.getElementById("popup");
    const saveButton = document.getElementById("save-record");
    const closeButton = document.getElementById("close-record");
    const diagnosisInput = document.getElementById("diagnosis-input");
    const treatmentTextarea = document.getElementById("treatment-textarea");

    popup.querySelector("span.error").style.display = "none";

    popup.style.display = "flex";
    diagnosisInput.value = "";
    treatmentTextarea.value = "";

    saveButton.addEventListener("click", () => saveMedicalRecord(appointment), { once: true });
    closeButton.addEventListener("click", closePopup);
};

const saveMedicalRecord = (appointment) => {
    const diagnosisInput = document.getElementById("diagnosis-input");
    const diagnosisInputValue = diagnosisInput.value;
    const treatmentTextarea = document.getElementById("treatment-textarea");
    const treatmentTextareaValue = treatmentTextarea.value;

    if (diagnosisInputValue && treatmentTextareaValue) {
        const data = {
            appointmentId: appointment.appointmentId,
            diagnosis: diagnosisInputValue,
            treatment: treatmentTextareaValue
        };

        fetch('/api/medical-records/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .catch(error => console.error("Error during POST request:", error))
            .finally(() => {
                closePopup();
                window.location.reload();
            });
    } else {
        const popupContentContainer = document.querySelector(".popup-content");
        popupContentContainer.querySelector("span.error").style.display = "block";
        popupContentContainer.querySelector("#save-record").addEventListener("click", () => saveMedicalRecord(appointment), { once: true });
        popupContentContainer.querySelector("#close-record").addEventListener("click", closePopup);
    }
};

const closePopup = () => {
    document.getElementById("popup").style.display = "none";
};
