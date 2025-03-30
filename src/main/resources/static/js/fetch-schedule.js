flatpickr("#datepicker", {
    dateFormat: "d-m-Y",
    locale: {
        firstDayOfWeek: 1
    },
    inline: true,
    defaultDate: new Date(),
    onChange: function (selectedDates, dateStr) {
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
        scheduleBody.innerHTML = "<tr><td colspan='5'>No appointments</td></tr>";
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
        <td data-title="Start time">${appointment.startTime}</td>
        <td data-title="Pet name">${appointment.petName}</td>
        <td data-title="Species">${appointment.petSpecies}</td>
        <td data-title="Reason">${appointment.reason}</td>
    `;
        const today = new Date().toDateString();
        const appointmentDate = new Date(appointment.date).toDateString();
        const buttonCell = document.createElement("td");
        const detailsButton = createViewDetailsButton(appointment);
        buttonCell.appendChild(detailsButton);
        buttonCell.classList.add("button-cell");
        if (appointmentDate === today && !appointment.hasMedicalRecord) {
            const recordButton = createMedicalRecordButton(appointment);
            buttonCell.appendChild(recordButton);
        }
        row.appendChild(buttonCell);

    } else {
        row.innerHTML = `
        <td data-title="Start time">${appointment.startTime}</td>
        <td data-title="Pet name"></td>
        <td data-title="Species"></td>
        <td data-title="Reason"></td>
        <td></td>
    `;
    }
    return row;
};

function createViewDetailsButton(appointment) {
    const button = document.createElement("button");
    button.textContent = "View details";
    button.addEventListener("click", () => openDetailsPopup(appointment));

    return button;
}

function openDetailsPopup(appointment) {
    const closeButton = document.getElementById("close-details");
    const popup = document.getElementById("details-popup");
    const time = document.getElementById("start-time");
    const petName = document.getElementById("pet-name");
    const petSpecies = document.getElementById("pet-species");
    const petBreed = document.getElementById("pet-breed");
    const petAge = document.getElementById("pet-age");
    const petOwner = document.getElementById("pet-owner");
    const petImage = document.getElementById("pet-image");
    const reason = document.getElementById("reason");
    const email = document.getElementById("email");
    const phone = document.getElementById("phone");

    time.textContent = appointment.startTime;
    petName.textContent = appointment.petName;
    petSpecies.textContent = appointment.petSpecies;
    petBreed.textContent = appointment.petBreed;
    petAge.textContent = appointment.petAge;
    petOwner.textContent = appointment.petOwnerName;
    petImage.src = appointment.petImageUrl;
    reason.textContent = appointment.reason;
    email.textContent = appointment.petOwnerEmail;
    phone.textContent = appointment.petOwnerPhone;

    popup.style.display = "flex";
    closeButton.addEventListener("click", closePopup)
    return undefined;
}

const createMedicalRecordButton = (appointment) => {
    const button = document.createElement("button");
    button.textContent = "Add record";
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

    saveButton.addEventListener("click", () => saveMedicalRecord(appointment), {once: true});
    closeButton.addEventListener("click", closePopup);
};

const saveMedicalRecord = (appointment) => {
    const diagnosisInput = document.getElementById("diagnosis-input");
    const diagnosisInputValue = diagnosisInput.value;
    const treatmentTextarea = document.getElementById("treatment-textarea");
    const treatmentTextareaValue = treatmentTextarea.value;

    const isDiagnosisInputValid = diagnosisInputValue && diagnosisInputValue.length < 100;

    if (isDiagnosisInputValid && treatmentTextareaValue) {
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
        const emptyErrorSpan = document.getElementById("empty-error");
        const lengthErrorSpan = document.getElementById("length-error");
        emptyErrorSpan.style.display = "none";
        lengthErrorSpan.style.display = "none";
        if (diagnosisInputValue.length < 100) {
            emptyErrorSpan.style.display = "block";
        } else {
            lengthErrorSpan.style.display = "block";
        }
        popupContentContainer.querySelector("#save-record").addEventListener("click", () => saveMedicalRecord(appointment), {once: true});
        popupContentContainer.querySelector("#close-record").addEventListener("click", closePopup);
    }
};

const closePopup = () => {
    document.getElementById("popup").style.display = "none";
    document.getElementById("details-popup").style.display = "none";
};

const today = new Date();
const formattedDate = today.toLocaleDateString("en-GB").split("/").join("-");
fetchSchedule(formattedDate);