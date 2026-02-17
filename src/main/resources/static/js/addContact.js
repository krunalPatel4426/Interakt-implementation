$(document).ready(function() {

    // --- 1. ADD ROW LOGIC ---
    function addRow() {
        let rowCount = $('#tableBody tr').length + 1;

        let html = `
                <tr>
                    <td><span class="fw-bold text-muted row-index">${rowCount}</span></td>
                    <td>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="fa-solid fa-phone"></i></span>
                            <input type="text" class="form-control phone-input" placeholder="e.g. +919876543210">
                        </div>
                    </td>
                    <td>
                        <select class="form-select status-select">
                            <option value="" disabled selected>Select Status</option>
                            <option value="Onboard">Onboard</option>
                            <option value="To Do">To Do</option>
                            <option value="Lead">Lead</option>
                        </select>
                    </td>
                    <td class="text-center">
                        <button class="btn btn-danger btn-sm btn-circle remove-btn">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `;

        $('#tableBody').append(html);
        updateUI();
    }

    // Add one row initially
    addRow();

    $('#addRowBtn').click(function() {
        addRow();
    });

    // --- 2. REMOVE ROW LOGIC ---
    $(document).on('click', '.remove-btn', function() {
        $(this).closest('tr').remove();
        renumberRows();
        updateUI();
    });

    function renumberRows() {
        $('#tableBody tr').each(function(index) {
            $(this).find('.row-index').text(index + 1);
        });
    }

    function updateUI() {
        let count = $('#tableBody tr').length;
        if(count === 0) {
            $('#emptyState').show();
            $('#submitBtn').prop('disabled', true);
        } else {
            $('#emptyState').hide();
            $('#submitBtn').prop('disabled', false);
        }
    }

    // --- 3. SUBMIT LOGIC (SEND JSON ARRAY) ---
    $('#submitBtn').click(function() {
        let contactList = [];
        let isValid = true;

        // Loop through each table row to build the List
        $('#tableBody tr').each(function() {
            let phone = $(this).find('.phone-input').val().trim();
            let status = $(this).find('.status-select').val();

            if(!phone || !status) {
                isValid = false;
                $(this).addClass('table-danger'); // Highlight invalid row
            } else {
                $(this).removeClass('table-danger');
                // Create DTO Object
                let dto = {
                    phoneNumber: phone,
                    status: status
                };
                contactList.push(dto);
            }
        });

        if(!isValid) {
            Swal.fire({
                icon: 'warning',
                title: 'Missing Information',
                text: 'Please fill in Phone Number and Status for all rows.',
                confirmButtonColor: '#f39c12'
            });
            return;
        }

        if(contactList.length === 0) {
            Swal.fire('Error', 'Please add at least one contact.', 'error');
            return;
        }

        // AJAX POST
        let btn = $(this);
        btn.prop('disabled', true).html('<i class="fa-solid fa-spinner fa-spin"></i> Saving...');

        $.ajax({
            url: '/api/contact/add', // Your RestController Endpoint
            type: 'POST',
            contentType: 'application/json', // IMPORTANT: Sending JSON
            data: JSON.stringify(contactList), // Convert Array to JSON String
            success: function(response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Saved!',
                    text: 'Contacts have been added successfully.',
                    confirmButtonColor: '#28a745'
                }).then(() => {
                    location.reload(); // Refresh page to clear form
                });
            },
            error: function(xhr) {
                let msg = "Unknown Error";
                try { msg = JSON.parse(xhr.responseText).message || xhr.statusText; } catch(e){}

                Swal.fire({
                    icon: 'error',
                    title: 'Failed',
                    text: msg,
                    confirmButtonColor: '#d33'
                });
                btn.prop('disabled', false).html('<i class="fa-solid fa-save"></i> Save All Contacts');
            }
        });
    });

});