$(document).ready(function() {

    console.log("Loaded Scheduler JS - Final Save Version");

    // Config for Prefixes
    const fieldConfig = {
        'headerContainer': { prefix: 'HEAD', class: 'dynamic-header' },
        'bodyContainer':   { prefix: 'VAR',  class: 'dynamic-body' },
        'buttonContainer': { prefix: 'BTN',  class: 'dynamic-button' }
    };

    // --- 1. ADD / REMOVE LOGIC (LIFO) ---
    function addField(containerId) {
        const config = fieldConfig[containerId];
        const currentCount = $('#' + containerId).children('.input-group').length;
        const newIndex = currentCount + 1;

        let html = `
            <div class="input-group mb-2">
                <span class="input-group-text bg-light border-0 fw-bold text-muted">${config.prefix} ${newIndex}</span>
                <input type="text" class="form-control ${config.class}" placeholder="Value for {{${newIndex}}}">
            </div>`;
        $('#' + containerId).append(html);
    }

    function removeLastField(containerId) {
        const container = $('#' + containerId);
        if(container.children('.input-group').length > 0) {
            container.children('.input-group').last().remove();
        } else {
            Swal.fire({ toast: true, position: 'top-end', icon: 'info', title: 'Nothing to remove', showConfirmButton: false, timer: 1500 });
        }
    }

    // Button Listeners
    $('#addHeaderBtn').click(function() { addField('headerContainer'); });
    $('#addBodyBtn').click(function() { addField('bodyContainer'); });
    $('#addButtonBtn').click(function() { addField('buttonContainer'); });
    $('#removeHeaderBtn').click(function() { removeLastField('headerContainer'); });
    $('#removeBodyBtn').click(function() { removeLastField('bodyContainer'); });
    $('#removeButtonBtn').click(function() { removeLastField('buttonContainer'); });


    // --- 2. DATA HELPER (GET TEMPLATE INFO ONLY) ---
    function getTemplateData() {
        const extractValues = (className) => {
            let vals = [];
            $('.' + className).each(function() {
                if($(this).val() && $(this).val().trim() !== '') {
                    vals.push($(this).val().trim());
                }
            });
            return vals;
        };

        let buttonList = extractValues('dynamic-button');
        let buttonMap = {};
        if (buttonList.length > 0) buttonMap["0"] = buttonList;

        return {
            templateName: $('#templateName').val(),
            languageCode: $('#languageCode').val(),
            headerValue: extractValues('dynamic-header'),
            bodyValue: extractValues('dynamic-body'),
            buttonValue: buttonMap
        };
    }

    // --- 3. TEST API (Single Number) ---
    $('#btnTest').click(function() {
        let testPhone = $('#testPhoneNumber').val();
        let baseData = getTemplateData();

        if(!testPhone || !baseData.templateName) {
            Swal.fire({ icon: 'warning', title: 'Missing Details', text: 'Enter Template Name & Test Phone Number.', confirmButtonColor: '#f39c12' });
            return;
        }

        // Merge Base Data with Phone Number
        let payload = { ...baseData, phoneNumber: testPhone };

        let btn = $(this);
        btn.prop('disabled', true).html('<i class="fa-solid fa-spinner fa-spin"></i> Sending...');
        $('.form-control').removeClass('is-invalid');
        $('#bodyContainer').removeClass('border border-danger p-2 rounded');

        $.ajax({
            url: '/api/schedule/test',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(response) {
                if(response.result === "true") {
                    Swal.fire({ icon: 'success', title: 'Test Successful!', text: 'Message sent. You can now schedule.', confirmButtonColor: '#28a745' });
                    $('#scheduleCard').removeClass('section-disabled');
                } else {
                    Swal.fire({ icon: 'error', title: 'Test Failed', text: response.message, confirmButtonColor: '#d33' });
                }
            },
            error: function(xhr) {
                handleError(xhr);
            },
            complete: function() {
                btn.prop('disabled', false).html('Send Test <i class="fa-regular fa-paper-plane ms-1"></i>');
            }
        });
    });

    // --- 4. SAVE API (Bulk by Status) ---
    $('#btnSave').click(function() {
        let contactStatus = $('#contactStatus').val();
        let scheduleTime = $('#scheduledTime').val();
        let baseData = getTemplateData();

        if(!contactStatus || !scheduleTime) {
            Swal.fire({ icon: 'warning', title: 'Incomplete', text: 'Select a Customer Group and Date/Time.', confirmButtonColor: '#f39c12' });
            return;
        }

        // Merge Base Data with Status & Time
        // Matches InteraktTemplateSaveRequestDto
        let payload = {
            ...baseData,
            contactStatus: contactStatus,
            scheduledTime: scheduleTime
        };

        let btn = $(this);
        btn.prop('disabled', true).text('Scheduling...');

        $.ajax({
            url: '/api/schedule/save',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(response) {
                if(response.result === "true") {
                    Swal.fire({ icon: 'success', title: 'Scheduled!', text: 'Campaign created successfully.', confirmButtonColor: '#28a745' })
                        .then((r) => { if(r.isConfirmed) location.reload(); });
                } else {
                    Swal.fire({ icon: 'error', title: 'Failed', text: response.message, confirmButtonColor: '#d33' });
                    btn.prop('disabled', false).text('Schedule for Group');
                }
            },
            error: function() {
                Swal.fire({ icon: 'error', title: 'Server Error', confirmButtonColor: '#d33' });
                btn.prop('disabled', false).text('Schedule for Group');
            }
        });
    });

    // Helper for error messages
    function handleError(xhr) {
        let errorMsg = "Unknown Error";
        try { errorMsg = JSON.parse(xhr.responseText).message || xhr.responseText; } catch(e){ errorMsg = xhr.responseText; }

        if (errorMsg.includes("Missing variable values")) {
            $('#bodyContainer').addClass('border border-danger p-2 rounded');
            Swal.fire({ icon: 'error', title: 'Variable Mismatch', html: `<p>Template requires more variables.</p><small class="text-muted">${errorMsg}</small>`, confirmButtonColor: '#d33' });
        } else if (errorMsg.includes("Template does not exist")) {
            $('#templateName').addClass('is-invalid');
            Swal.fire({ icon: 'error', title: 'Invalid Template', text: 'Template name not found.', confirmButtonColor: '#d33' });
        } else {
            Swal.fire({ icon: 'error', title: 'Error', text: errorMsg, confirmButtonColor: '#d33' });
        }
    }
});