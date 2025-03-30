document.querySelector('input[type="file"]').addEventListener("change", handleFileSelection);

function handleFileSelection() {
    const fileInput = document.querySelector('input[type="file"]');

    if (fileInput.files.length > 0) {
        const fileName = fileInput.files[0].name;
        fileInput.setAttribute("file-name", fileName);
        fileInput.classList.add("has-file");
    } else {
        fileInput.classList.remove("has-file");
    }
}