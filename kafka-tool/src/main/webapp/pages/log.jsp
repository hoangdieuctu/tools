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
        <jsp:include page="common/loading.jsp"/>
        <div id="content">
            <div class="container-fluid" style="padding-top: 15px">
                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Log</h1>
                    <i title="Reload" class="fa fa-sync fa-sm fa-fw main-icon sync-logs"></i>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-lg-12 col-md-12 mb-12">
                        <div class="data-parent">
                            <pre id="log-content">${logs}</pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script>
    $(document).ready(function () {
        updateTitle("Logs");

        $('.sync-logs').click(function() {
            sendAjax({
                'url': '/logs/content'
            }, 'get', function(content) {
                var selector = $('#log-content');
                selector.empty();
                selector.text(content);
            }, function() {
                toastWarning('Error!');
            })
        });
    });
</script>
</body>
</html>
