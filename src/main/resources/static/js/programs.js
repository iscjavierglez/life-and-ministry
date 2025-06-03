/**
 * JavaScript functions for program management
 */
document.addEventListener('DOMContentLoaded', function() {
    // Add event listeners for existing buttons
    addAssignationEventListeners();
    removeAssignationEventListeners();

    // Initialize Add Week button functionality
    const addWeekBtn = document.getElementById('add-week-btn');
    if (addWeekBtn) {
        addWeekBtn.addEventListener('click', function() {
            addNewWeek();
        });
    }
});

/**
 * Add a new week to the program form
 */
function addNewWeek() {
    const weeklyProgramsContainer = document.getElementById('weekly-programs-container');
    const weekTemplate = document.getElementById('week-template');
    const weekCount = document.querySelectorAll('.weekly-program-card').length;

    // Clone the template
    const newWeek = weekTemplate.content.cloneNode(true);

    // Update indices in the cloned template
    updateIndices(newWeek, weekCount);

    // Add the new week to the container
    weeklyProgramsContainer.appendChild(newWeek);

    // Add event listeners to the new week's buttons
    addAssignationEventListeners();
    removeAssignationEventListeners();

    // Add event listeners to the new week's remove button
    const removeWeekBtns = document.querySelectorAll('.remove-week-btn');
    const lastRemoveWeekBtn = removeWeekBtns[removeWeekBtns.length - 1];
    if (lastRemoveWeekBtn) {
        lastRemoveWeekBtn.addEventListener('click', function() {
            removeWeek(this);
        });
    }
}

/**
 * Remove a week from the program form
 * @param {HTMLElement} button - The remove button that was clicked
 */
function removeWeek(button) {
    const weekCard = button.closest('.weekly-program-card');
    if (weekCard) {
        // Check if this is the last week
        const weekCards = document.querySelectorAll('.weekly-program-card');
        if (weekCards.length <= 1) {
            alert('Cannot remove the last week. At least one week is required.');
            return;
        }

        // Remove the week card
        weekCard.remove();

        // Reindex all remaining weeks
        reindexWeeks();
    }
}

/**
 * Reindex all weeks after a week is removed
 */
function reindexWeeks() {
    const weekCards = document.querySelectorAll('.weekly-program-card');
    weekCards.forEach((card, index) => {
        // Update title input
        const titleInput = card.querySelector('input[name$=".title"]');
        if (titleInput) {
            titleInput.name = titleInput.name.replace(/weeklyProgram\[\d+\]/, `weeklyProgram[${index}]`);
            if (titleInput.id) {
                titleInput.id = titleInput.id.replace(/weeklyProgram\d+/, `weeklyProgram${index}`);
            }
        }

        // Update all other inputs, selects, and hidden fields
        updateElementIndices(card, 'input', index);
        updateElementIndices(card, 'select', index);
        updateElementIndices(card, 'button[data-week-index]', index);
    });
}

/**
 * Update indices of elements within a container
 * @param {HTMLElement} container - The container element
 * @param {string} selector - CSS selector for elements to update
 * @param {number} newIndex - The new index to set
 */
function updateElementIndices(container, selector, newIndex) {
    const elements = container.querySelectorAll(selector);
    elements.forEach(element => {
        if (element.name) {
            element.name = element.name.replace(/weeklyProgram\[\d+\]/, `weeklyProgram[${newIndex}]`);
        }
        if (element.id) {
            element.id = element.id.replace(/weeklyProgram\d+/, `weeklyProgram${newIndex}`);
        }
        if (element.getAttribute('data-week-index')) {
            element.setAttribute('data-week-index', newIndex);
        }
    });
}

/**
 * Update indices in a new week template
 * @param {DocumentFragment} template - The cloned template
 * @param {number} index - The index for the new week
 */
function updateIndices(template, index) {
    // Update title input
    const titleInput = template.querySelector('input[name$=".title"]');
    if (titleInput) {
        titleInput.name = titleInput.name.replace(/weeklyProgram\[\d+\]/, `weeklyProgram[${index}]`);
        titleInput.value = `Week ${index + 1}`;
        if (titleInput.id) {
            titleInput.id = titleInput.id.replace(/weeklyProgram\d+/, `weeklyProgram${index}`);
        }
    }

    // Update all other inputs, selects, and hidden fields
    updateElementIndices(template, 'input', index);
    updateElementIndices(template, 'select', index);
    updateElementIndices(template, 'button[data-week-index]', index);
}

/**
 * Add event listeners to all add assignation buttons
 */
function addAssignationEventListeners() {
    document.querySelectorAll('.add-assignation').forEach(button => {
        button.addEventListener('click', function() {
            const section = this.getAttribute('data-section');
            const weekIndex = this.getAttribute('data-week-index');
            addAssignation(section, weekIndex);
        });
    });
}

/**
 * Add event listeners to all remove assignation buttons
 */
function removeAssignationEventListeners() {
    document.querySelectorAll('.remove-assignation').forEach(button => {
        button.addEventListener('click', function() {
            this.closest('.d-flex').remove();
        });
    });
}

/**
 * Add a new assignation to a section
 * @param {string} section - The section to add the assignation to (treasuresFromGod, applyToTheFieldMinistry, livingAsChristians)
 * @param {number} weekIndex - The index of the week
 */
function addAssignation(section, weekIndex) {
    const container = document.querySelector(`.weekly-program-card[data-week-index="${weekIndex}"] [data-section="${section}"] .assignation-container`);
    if (!container) return;

    const assignationCount = container.querySelectorAll('.d-flex').length;

    // Create a new assignation div
    const newAssignation = document.createElement('div');
    newAssignation.className = 'd-flex align-items-center mb-2';

    // Assignation name input
    const nameDiv = document.createElement('div');
    nameDiv.className = 'flex-grow-1 me-2';
    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.className = 'form-control';
    nameInput.name = `weeklyProgram[${weekIndex}].${section}[${assignationCount}].name`;
    nameInput.placeholder = 'Assignation Name';
    nameInput.required = true;
    nameDiv.appendChild(nameInput);

    // Publisher select
    const publisherDiv = document.createElement('div');
    publisherDiv.className = 'flex-grow-1 me-2';
    const publisherSelect = document.createElement('select');
    publisherSelect.className = 'form-select';
    publisherSelect.name = `weeklyProgram[${weekIndex}].${section}[${assignationCount}].publisherId`;

    // Clone options from an existing select
    const existingSelect = document.querySelector(`select[name^="weeklyProgram[${weekIndex}].${section}["]`);
    if (existingSelect) {
        existingSelect.querySelectorAll('option').forEach(option => {
            publisherSelect.appendChild(option.cloneNode(true));
        });
    } else {
        // Default empty option
        const emptyOption = document.createElement('option');
        emptyOption.value = '';
        emptyOption.textContent = '-- Select Publisher --';
        publisherSelect.appendChild(emptyOption);
    }

    publisherDiv.appendChild(publisherSelect);

    // Duration input
    const durationDiv = document.createElement('div');
    durationDiv.style.width = '100px';
    durationDiv.className = 'me-2';
    const durationInput = document.createElement('input');
    durationInput.type = 'number';
    durationInput.className = 'form-control';
    durationInput.name = `weeklyProgram[${weekIndex}].${section}[${assignationCount}].duration`;
    durationInput.placeholder = 'Minutes';
    durationInput.min = '1';
    durationInput.required = true;
    durationDiv.appendChild(durationInput);

    // Hidden type input
    const typeInput = document.createElement('input');
    typeInput.type = 'hidden';
    typeInput.name = `weeklyProgram[${weekIndex}].${section}[${assignationCount}].type`;

    // Set the type value based on the section
    if (section === 'treasuresFromGod') {
        typeInput.value = 'DISCURSO_TESOROS';
    } else if (section === 'applyToTheFieldMinistry') {
        typeInput.value = 'SEAMOS_MEJORES_MAESTROS';
    } else if (section === 'livingAsChristians') {
        typeInput.value = 'VIDA_CRISTIANA';
    }

    // Remove button
    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'btn btn-sm btn-danger remove-assignation';
    removeButton.innerHTML = '<i class="bi bi-trash"></i>';
    removeButton.addEventListener('click', function() {
        this.closest('.d-flex').remove();
    });

    // Add all elements to the new assignation div
    newAssignation.appendChild(nameDiv);
    newAssignation.appendChild(publisherDiv);
    newAssignation.appendChild(durationDiv);
    newAssignation.appendChild(typeInput);
    newAssignation.appendChild(removeButton);

    // Add the new assignation to the container
    container.appendChild(newAssignation);
}
