<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Contact Details</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">

    <style>
        body { background-color: #f8f9fa; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .card { border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.1); border-radius: 10px; }
        .card-header { background: #fff; border-bottom: 2px solid #e9ecef; padding: 15px 20px; }
        .table thead th { border-top: none; background-color: #f1f3f5; color: #495057; }
        .btn-circle { width: 35px; height: 35px; padding: 6px 0px; border-radius: 50%; text-align: center; font-size: 12px; line-height: 1.42857; }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-lg-10">

            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h4 class="mb-0 text-primary"><i class="fa-solid fa-address-book"></i> Add Contacts</h4>
                    <button class="btn btn-outline-primary btn-sm" id="addRowBtn">
                        <i class="fa-solid fa-plus"></i> Add New Row
                    </button>
                </div>

                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle" id="contactTable">
                            <thead>
                            <tr>
                                <th style="width: 5%">#</th>
                                <th style="width: 45%">Phone Number</th>
                                <th style="width: 40%">Status</th>
                                <th style="width: 10%" class="text-center">Action</th>
                            </tr>
                            </thead>
                            <tbody id="tableBody">
                            </tbody>
                        </table>
                    </div>

                    <div id="emptyState" class="text-center py-4 text-muted">
                        <i class="fa-solid fa-layer-group fa-2x mb-2"></i>
                        <p>No contacts added yet. Click "Add New Row" to start.</p>
                    </div>

                    <div class="d-grid gap-2 mt-4">
                        <button class="btn btn-success btn-lg" id="submitBtn" disabled>
                            <i class="fa-solid fa-save"></i> Save All Contacts
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="/js/addContact.js">
</script>

</body>
</html>