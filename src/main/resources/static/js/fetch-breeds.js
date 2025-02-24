function fetchBreeds() {
    const species = document.getElementById("species").value;

    if (!species) return;

    fetch('/api/breeds?species=' + species)
        .then(response => response.json())
        .then(data => {
            const breedSelect = document.getElementById("breed");
            breedSelect.innerHTML = '';

            data.forEach(breed => {
                const option = document.createElement("option");
                option.value = breed;
                option.text = breed;
                breedSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error fetching breeds:", error));
}

document.getElementById("species").addEventListener("change", fetchBreeds);