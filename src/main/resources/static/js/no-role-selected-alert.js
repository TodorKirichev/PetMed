const forms = document.querySelectorAll("form");
forms.forEach(form => {
    form.addEventListener("submit", alertIfNotSelected)
})

function alertIfNotSelected(event) {
    const form = event.target;
    const select = form.parentElement.querySelector("select[name='new-role']");
    const selectedRole = select.value;

    if (!selectedRole) {
        event.preventDefault();
        alert("Select a role before submitting")
    }
}