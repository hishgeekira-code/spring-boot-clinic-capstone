"use strict";

function changeAppointmentStatus(appointmentId, newStatus) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const alertBox = document.getElementById('alert-box');
    const statusLabel = document.getElementById('status-label-' + appointmentId);

    const headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    };
    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    const bodyParams = new URLSearchParams();
    bodyParams.append('appointmentId', appointmentId);
    bodyParams.append('status', newStatus);

    fetch('/admin/appointments/update-status', {
        method: 'POST',
        headers: headers,
        body: bodyParams
    })
    .then(async response => {
        if (!response.ok) {
            throw new Error('Server error: ' + response.status);
        }
        return response.json();
    })
    .then(() => {
        // Баазад хадгалагдсан тул текстийг шинэчлэх
        if (statusLabel) {
            statusLabel.innerText = newStatus;

            if (newStatus === 'CONFIRMED') {
                statusLabel.style.backgroundColor = '#e0e7ff';
                statusLabel.style.color = '#4338ca';
            } else if (newStatus === 'COMPLETED') {
                statusLabel.style.backgroundColor = '#dcfce7';
                statusLabel.style.color = '#15803d';
            } else if (newStatus === 'CANCELLED') {
                statusLabel.style.backgroundColor = '#fee2e2';
                statusLabel.style.color = '#b91c1c';
            } else {
                statusLabel.style.backgroundColor = '#fef3c7';
                statusLabel.style.color = '#b45309';
            }
        }

        if (alertBox) {
            alertBox.style.display = 'block';
            alertBox.style.backgroundColor = '#dcfce7';
            alertBox.style.color = '#15803d';
            alertBox.innerText = 'Appointment #' + appointmentId + ' status successfully updated to ' + newStatus;

            setTimeout(() => {
                alertBox.style.display = 'none';
            }, 3000);
        }
    })
    .catch(error => {
        console.error('Update failed:', error);
        if (alertBox) {
            alertBox.style.display = 'block';
            alertBox.style.backgroundColor = '#fee2e2';
            alertBox.style.color = '#b91c1c';
            alertBox.innerText = 'Failed to update status: ' + error.message;
        }

        const selectElement = document.getElementById('status-select-' + appointmentId);
        if (selectElement && statusLabel) {
            selectElement.value = statusLabel.innerText.trim();
        }
    });
}