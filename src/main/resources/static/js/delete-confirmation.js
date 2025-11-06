function confirmDelete(button) {
    const name = button.getAttribute("data-name");
    return confirm(`Är du säker på att du vill ta bort övningen "${name}"?`);
}
