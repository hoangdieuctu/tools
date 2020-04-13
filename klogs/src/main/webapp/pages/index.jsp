<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>KLogs</title>
    <link rel='shortcut icon' href="/resources/images/logo.png" type='image/png'/>
</head>

<body id="page-top">
<div id="content" class="content">
    <div class="row">
        <div class="col-xl-12 col-lg-12 col-md-12">
            <div class="pods-parent">
                <div class="row">
                    <div class="col-xl-4 col-lg-4 col-md-4">
                        <select id="pods">
                            <c:forEach items="${pods}" var="pod">
                                <option value="${pod}">${pod}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-xl-4 col-lg-4 col-md-4">
                        <button id="connect" class="btn btn-sm btn-primary">Connect</button>
                        <button id="disconnect" class="btn btn-sm btn-danger">Disconnect</button>
                    </div>
                    <div class="col-xl-4 col-lg-4 col-md-4" style="text-align: right">
                        <button id="clear" class="btn btn-sm btn-secondary">Clear</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-xl-12 col-lg-12 col-md-12" style="padding-top: 50px">
            <div id="data-parent"></div>
        </div>
    </div>
</div>

<div class="modal fade" id="setting-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Setting</h5>
            </div>
            <div class="modal-body">
                <p>Log display: </p>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="time">
                    <label class="form-check-label" for="time">Date & time</label>
                </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="level">
                    <label class="form-check-label" for="level">Level</label>
                </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="pid">
                    <label class="form-check-label" for="pid">PID</label>
                </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="thread">
                    <label class="form-check-label" for="thread">Thread</label>
                </div>

                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="_class">
                    <label class="form-check-label" for="_class">Class & method</label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/resources/css/select2.min.css" rel="stylesheet" type="text/css">

<link href="/resources/css/index.css?r=1" rel="stylesheet" type="text/css">

<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/select2.min.js"></script>
<script src="/resources/js/sockjs.min.js"></script>
<script src="/resources/js/stomp.min.js"></script>

<script src="/resources/js/index.js?r=1"></script>

</body>
</html>
