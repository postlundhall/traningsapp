document.addEventListener("DOMContentLoaded", function () {
    const fields = document.querySelectorAll("input[maxlength], textarea[maxlength]");

    fields.forEach(field => {
        const maxLength = parseInt(field.getAttribute("maxlength"), 10);

        // Create a counter <small> if not already present
        let counter = field.nextElementSibling;
        if (!counter || !counter.classList.contains("char-counter")) {
            counter = document.createElement("small");
            counter.className = "form-text text-muted char-counter d-block mt-1";
            field.insertAdjacentElement("afterend", counter);
        }

        function updateCounter() {
            const currentLength = field.value.length;
            counter.textContent = `${currentLength} / ${maxLength}`;

            // Reset classes first
            counter.classList.remove("text-muted", "text-warning", "text-danger");

            // Color feedback for counter text
            if (currentLength >= maxLength) {
                counter.classList.add("text-danger");
            } else if (currentLength >= maxLength - 25) {
                counter.classList.add("text-warning");
            } else {
                counter.classList.add("text-muted");
            }
        }

        // Update on input & initialize on load
        field.addEventListener("input", updateCounter);
        updateCounter();
    });
});