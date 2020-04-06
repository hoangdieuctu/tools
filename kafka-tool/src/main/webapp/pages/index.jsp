<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <jsp:include page="common/meta.jsp"/>
    <jsp:include page="common/css.jsp"/>
</head>

<body id="page-top">
<div id="wrapper">
    <!-- Left Menu -->
    <jsp:include page="common/left_menu.jsp"/>

    <div id="content-wrapper">
        <div id="content">
            <div class="container-fluid">
                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12" style="padding-top: 20px;">
                        <div class="group-title">
                            <p>Operation</p>
                        </div>
                        <a href="consume" style="text-decoration: none; padding-right: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-download fa-2x card-icon"></div>
                                    <div class="card-text-content">Consume</div>
                                </div>
                            </div>
                        </a>

                        <a href="produce" style="text-decoration: none; padding-left: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-upload fa-2x card-icon"></div>
                                    <div class="card-text-content">Produce</div>
                                </div>
                            </div>
                        </a>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12" style="padding-top: 30px;">
                        <div class="group-title">
                            <p>Management</p>
                        </div>
                        <a href="consumer-group" style="text-decoration: none; padding-right: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-eye fa-2x card-icon"></div>
                                    <div class="card-text-content">Group</div>
                                </div>
                            </div>
                        </a>
                        <a href="topic" style="text-decoration: none; padding-right: 10px; padding-left: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-comment fa-2x card-icon"></div>
                                    <div class="card-text-content">Topic</div>
                                </div>
                            </div>
                        </a>
                        <a href="/admin" style="text-decoration: none; padding-left: 10px; padding-right: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-lock fa-2x card-icon"></div>
                                    <div class="card-text-content">Administrator</div>
                                </div>
                            </div>
                        </a>

                        <%--<a href="/alert-rule" style="text-decoration: none; padding-left: 10px">--%>
                            <%--<div class="card-parent">--%>
                                <%--<div>--%>
                                    <%--<div class="fas fa-fw fa-bell fa-2x card-icon"></div>--%>
                                    <%--<div class="card-text-content">Alert</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</a>--%>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12" style="padding-top: 30px; padding-bottom: 20px">
                        <div class="group-title">
                            <p>Control</p>
                        </div>
                        <a style="text-decoration: none; padding-right: 10px">
                            <div id="notification-validate" class="card-parent" data-toggle="modal" data-target="#sys-check-modal">
                                <div>
                                    <div class="fas fa-fw fa-check fa-2x card-icon"></div>
                                    <div class="card-text-content">Sys Check</div>
                                </div>
                            </div>
                        </a>
                        <a style="text-decoration: none; padding-right: 10px; padding-left: 10px">
                            <div id="flush-cache" class="card-parent" data-toggle="modal" data-target="#flush-cache-modal">
                                <div>
                                    <div class="fas fa-fw fa-trash fa-2x card-icon"></div>
                                    <div class="card-text-content">Flush Cache</div>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div id="sys-check-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm</h5>
                </div>
                <div class="modal-body">
                    <p>Do you want to perform system check?</p>
                    <p style="margin-bottom: 0px">This include: </p>
                    <ul>
                        <li class="li-detail">Send ridership notification</li>
                        <li class="li-detail">Send geofence notification</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button id="sys-check-modal-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="sys-check-modal-ok" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div id="flush-cache-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm</h5>
                </div>
                <div class="modal-body">
                    <p>Do you want to flush all cache?</p>
                    <p style="margin-bottom: 0px">This include: </p>
                    <ul>
                        <li class="li-detail">Remove kafka topics cache</li>
                        <li class="li-detail">Remove topic exclusion setting cache</li>
                        <li class="li-detail">All changed settings will apply immediately</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button id="modal-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="modal-ok" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>
<script>
    $(document).ready(function () {
       $('#modal-ok').click(function() {
            sendAjax({
                'url': '/cache/flush'
            }, 'get', function() {
                toastSuccess('Flushed');
            }, function() {
                toastWarning('Error!');
            })
       });

        $('#sys-check-modal-ok').click(function() {
            sendAjax({
                'url': '/notification/check'
            }, 'get', function() {
                toastSuccess('Checked!');
            }, function() {
                toastWarning('Error!');
            })
        });
    });
</script>

</body>
</html>
