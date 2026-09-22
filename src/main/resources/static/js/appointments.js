"use strict";

function changeAppointmentStatus(appointmentId, newStatus) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const alertBox = document.getElementById('alert-box');
    const statusLabel = document.getElementById('status-label-' + appointmentId);

    const headers = {
        'Content-Type': 'application/json'
    };
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    // Fetch API дуудах
    fetch('/api/appointments/' + appointmentId + '/status', {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify({ status: newStatus })
    })
    .then(async response => {
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.error || 'Failed to update status');
        }
        return data;
    })
    .then(data => {
        // Дэлгэцийн текстийг шинэчлэх
        statusLabel.innerText = data.status;

        alertBox.style.display = 'block';
        alertBox.style.backgroundColor = '#d4edda';
        alertBox.style.color = '#155724';
        alertBox.innerText = 'Appointment #' + appointmentId + ' status successfully updated to ' + data.status;

        setTimeout(() => {
            alertBox.style.display = 'none';
        }, 3500);
    })
    .catch(error => {
        alertBox.style.display = 'block';
        alertBox.style.backgroundColor = '#f8d7da';
        alertBox.style.color = '#721c24';
        alertBox.innerText = 'Error: ' + error.message;

        // Алдаа гарвал сонголтыг хуучин төлөв рүү буцаах
        const selectElement = document.getElementById('status-select-' + appointmentId);
        if (selectElement && statusLabel) {
            selectElement.value = statusLabel.innerText.trim();
        }
    });
}