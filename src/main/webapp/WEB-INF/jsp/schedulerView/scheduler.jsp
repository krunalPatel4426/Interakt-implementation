<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campaign Scheduler</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">

    <style>
        body { background-color: #f4f6f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .card { border: none; box-shadow: 0 4px 15px rgba(0,0,0,0.05); border-radius: 12px; margin-bottom: 20px; }
        .card-header { background: white; border-bottom: 2px solid #f0f0f0; font-weight: bold; padding: 15px 20px; }
        .section-disabled { opacity: 0.6; pointer-events: none; filter: grayscale(100%); transition: all 0.3s ease; }

        /* Action Links */
        .btn-action { cursor: pointer; font-size: 0.9rem; font-weight: 600; text-decoration: none; margin-left: 15px; }
        .btn-add { color: #0d6efd; }
        .btn-add:hover { text-decoration: underline; }
        .btn-remove { color: #dc3545; }
        .btn-remove:hover { text-decoration: underline; }

        .help-text { font-size: 0.85rem; color: #6c757d; margin-top: 2px; }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">

            <h2 class="text-center mb-4 fw-bold text-primary">
                <i class="fa-brands fa-whatsapp"></i> Campaign Scheduler
            </h2>

            <div class="card">
                <div class="card-header text-primary">1. Message Configuration</div>
                <div class="card-body">

                    <div class="row g-3 mb-4">
                        <div class="col-md-8">
                            <label class="form-label fw-bold">Template Name</label>
                            <input type="text" class="form-control" id="templateName" placeholder="e.g. welcome_offer_v2">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-bold">Language</label>
                            <select class="form-select" id="languageCode">
                                <option value="en" selected>English (en)</option>
                                <option value="hi">Hindi (hi)</option>
                                <option value="gu">Gujarati (gu)</option>
                                <option value="mr">Marathi (mr)</option>
                            </select>
                        </div>
                    </div>

                    <hr class="text-muted">

                    <div class="mt-3">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <label class="form-label fw-bold mb-0">Header Variables</label>
                            <div>
                                <span class="btn-action btn-remove" id="removeHeaderBtn"><i class="fa-solid fa-minus"></i> Remove Last</span>
                                <span class="btn-action btn-add" id="addHeaderBtn"><i class="fa-solid fa-plus"></i> Add Value</span>
                            </div>
                        </div>
                        <div id="headerContainer"></div>
                    </div>

                    <div class="mt-4">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <label class="form-label fw-bold mb-0">Message Content Variables</label>
                            <div>
                                <span class="btn-action btn-remove" id="removeBodyBtn"><i class="fa-solid fa-minus"></i> Remove Last</span>
                                <span class="btn-action btn-add" id="addBodyBtn"><i class="fa-solid fa-plus"></i> Add Value</span>
                            </div>
                        </div>
                        <div id="bodyContainer">
                            <div class="input-group mb-2">
                                <span class="input-group-text bg-light border-0 fw-bold text-muted">VAR 1</span>
                                <input type="text" class="form-control dynamic-body" placeholder="Value for {{1}}">
                            </div>
                        </div>
                    </div>

                    <div class="mt-4">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <label class="form-label fw-bold mb-0">Button Variables</label>
                            <div>
                                <span class="btn-action btn-remove" id="removeButtonBtn"><i class="fa-solid fa-minus"></i> Remove Last</span>
                                <span class="btn-action btn-add" id="addButtonBtn"><i class="fa-solid fa-plus"></i> Add Value</span>
                            </div>
                        </div>
                        <div id="buttonContainer"></div>
                    </div>

                </div>
            </div>

            <div class="card border-warning border-start border-4">
                <div class="card-header text-warning fw-bold">2. Send Test Message</div>
                <div class="card-body">
                    <p class="text-muted small mb-3">
                        <i class="fa-solid fa-circle-info"></i> You must verify the template works before scheduling.
                    </p>
                    <div class="input-group">
                        <span class="input-group-text bg-white"><i class="fa-solid fa-phone"></i></span>
                        <input type="text" class="form-control" id="testPhoneNumber" placeholder="Enter YOUR phone number (+91...)">
                        <button class="btn btn-warning px-4 fw-bold" id="btnTest">
                            Send Test <i class="fa-regular fa-paper-plane ms-1"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="card border-success border-start border-4 section-disabled" id="scheduleCard">
                <div class="card-header text-success fw-bold">3. Schedule Campaign</div>
                <div class="card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Target Customer Group</label>
                            <select class="form-select" id="contactStatus">
                                <option value="" disabled selected>Select Status Group</option>
                                <c:forEach var="s" items="${status}">
                                    <option value="${s}">${s}</option>
                                </c:forEach>
                            </select>
                            <div class="help-text">Message will be sent to all contacts with this status.</div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Date & Time</label>
                            <input type="datetime-local" class="form-control" id="scheduledTime">
                        </div>
                    </div>
                    <button class="btn btn-success w-100 mt-4 py-2 fw-bold shadow-sm" id="btnSave">
                        <i class="fa-solid fa-clock"></i> Schedule for Group
                    </button>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="/js/scheduler.js?v=<%= System.currentTimeMillis() %>"></script>

</body>
</html>