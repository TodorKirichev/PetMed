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
    row.innerHTML = `
        <td>${appointment.startTime}</td>
        <td>${appointment.petName}</td>
        <td>${appointment.petSpecies}</td>
        <td>${appointment.petBreed}</td>
        <td>${appointment.petOwnerFirstName}</td>
    `;

    const buttonCell = document.createElement("td");
    const button = createMedicalRecordButton(appointment);
    buttonCell.appendChild(button);
    row.appendChild(buttonCell);

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

    popup.style.display = "flex";
    diagnosisInput.value = "";
    treatmentTextarea.value = "";

    saveButton.addEventListener("click", () => saveMedicalRecord(appointment), { once: true });
    closeButton.addEventListener("click", closePopup);
};

const saveMedicalRecord = (appointment) => {
    const diagnosisInput = document.getElementById("diagnosis-input").value;
    const treatmentTextarea = document.getElementById("treatment-textarea").value;

    const data = {
        appointmentId: appointment.appointmentId,
        diagnosis: diagnosisInput,
        treatment: treatmentTextarea
    };

    fetch('/api/medical-records', {
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
};

const closePopup = () => {
    document.getElementById("popup").style.display = "none";
};
