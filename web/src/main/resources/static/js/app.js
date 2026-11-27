const apiUrl = document.querySelector("#car-page").dataset.apiUrl;
const rows = document.querySelector("#car-rows");
const rowTemplate = document.querySelector("#car-row").content.firstElementChild;
const table = document.querySelector("#car-table");
const emptyList = document.querySelector("#empty-list");
const listMessage = document.querySelector("#list-message");
const brandSearch = document.querySelector("#brand-search");
const idSearch = document.querySelector("#id-search");
const editor = document.querySelector("#car-editor");
const editorForm = document.querySelector("#editor-form");
const editorTitle = document.querySelector("#editor-title");
const editorMessage = document.querySelector("#editor-message");
const saveButton = document.querySelector("#save-car");
const cancelButton = document.querySelector("#cancel-edit");

let editingId = "";
let listRequest = 0;

async function request(path, options) {
    let response;
    try {
        response = await fetch(apiUrl + path, {cache: "no-store", ...options});
    } catch {
        throw new Error("Nem sikerült kapcsolódni az alkalmazáshoz.");
    }

    if (!response.ok) {
        const body = await response.json().catch(() => null);
        throw new Error(body?.error || `A kérés sikertelen (HTTP ${response.status}).`);
    }
    return response.status === 204 ? null : response.json();
}

function showMessage(element, message, isError = false) {
    element.classList.toggle("is-error", isError);
    element.textContent = message;
}

function renderCars(cars, emptyMessage) {
    const newRows = document.createDocumentFragment();
    for (const car of cars) {
        const row = rowTemplate.cloneNode(true);
        row.dataset.id = car.id;
        for (const field of row.querySelectorAll("[data-field]")) {
            field.textContent = car[field.dataset.field] ?? "";
        }
        if (car.fuelType) {
            row.querySelector(".fuel-badge").classList.add(car.fuelType);
        }
        row.querySelector(".car-id").title = car.id;
        newRows.append(row);
    }

    rows.replaceChildren(newRows);
    table.hidden = cars.length === 0;
    emptyList.hidden = !table.hidden;
    emptyList.textContent = emptyMessage;
}

async function loadCars(path = "", message = "") {
    const currentRequest = ++listRequest;
    try {
        const result = await request(path);
        if (currentRequest === listRequest) {
            renderCars(Array.isArray(result) ? result : [result], path ? "Nincs találat." : "Az autók listája üres.");
            showMessage(listMessage, message);
        }
    } catch (error) {
        if (currentRequest === listRequest) {
            const prefix = message ? message + " A lista frissítése sikertelen. " : "";
            showMessage(listMessage, prefix + error.message, true);
        }
    }
}

function showAllCars(message = "") {
    brandSearch.reset();
    idSearch.reset();
    return loadCars("", message);
}

function openEditor(car = {}) {
    editingId = car.id ?? "";
    editorTitle.textContent = editingId ? "Autó szerkesztése" : "Új autó felvitele (REST)";
    for (const field of editorForm.elements) {
        if (field.name) {
            field.value = car[field.name] ?? "";
        }
    }
    showMessage(editorMessage, "");
    editor.showModal();
}

brandSearch.addEventListener("submit", event => {
    event.preventDefault();
    const query = new URLSearchParams({brand: brandSearch.elements.brand.value.trim()});
    loadCars("/search?" + query);
});

idSearch.addEventListener("submit", event => {
    event.preventDefault();
    const id = idSearch.elements.carId.value.trim();
    if (id) {
        loadCars("/" + encodeURIComponent(id));
    } else {
        showMessage(listMessage, "Add meg az autó azonosítóját.", true);
    }
});

document.querySelector("#all-cars").addEventListener("click", () => showAllCars());
document.querySelector("#new-rest-car").addEventListener("click", () => openEditor());
cancelButton.addEventListener("click", () => editor.close());
editor.addEventListener("cancel", event => {
    if (saveButton.disabled) {
        event.preventDefault();
    }
});

editorForm.addEventListener("submit", async event => {
    event.preventDefault();
    const car = Object.fromEntries(new FormData(editorForm));
    car.horsepower = editorForm.elements.horsepower.valueAsNumber;
    const path = editingId ? "/" + encodeURIComponent(editingId) : "";

    saveButton.disabled = true;
    cancelButton.disabled = true;
    showMessage(editorMessage, "");
    try {
        await request(path, {
            method: editingId ? "PUT" : "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(car)
        });
        editor.close();
        await showAllCars("Az autó adatai mentve.");
    } catch (error) {
        showMessage(editorMessage, error.message, true);
    } finally {
        saveButton.disabled = false;
        cancelButton.disabled = false;
    }
});

rows.addEventListener("click", async event => {
    const button = event.target.closest("button[data-action]");
    if (!button) {
        return;
    }

    const row = button.closest("tr");
    const path = "/" + encodeURIComponent(row.dataset.id);
    const deleting = button.dataset.action === "delete";
    const carName = row.cells[0].textContent + " " + row.cells[1].textContent;
    if (deleting && !window.confirm(`Törlöd ezt az autót: ${carName}?`)) {
        return;
    }

    button.disabled = true;
    showMessage(listMessage, "");
    try {
        if (deleting) {
            await request(path, {method: "DELETE"});
            await showAllCars("Az autó törölve.");
        } else {
            const car = await request(path);
            if (!editor.open) {
                openEditor(car);
            }
        }
    } catch (error) {
        showMessage(listMessage, error.message, true);
    } finally {
        button.disabled = false;
    }
});
