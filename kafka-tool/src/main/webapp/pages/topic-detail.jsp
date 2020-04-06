<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                    <h1 class="h3 mb-0 text-gray-800">Topic Details (<span style="font-style: italic" id="topic-id">${topic}</span>)</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <div>
                                        <span>Partition (${fn:length(details)})</span>
                                    </div>
                                </h6>
                            </div>
                            <div class="panel-body" id="partitioning-content">
                                <div class="members-parent container-fluid" style="padding-bottom: 10px">
                                    <div class="row">
                                        <table class="table table-bordered" id="partition-table" width="100%" cellspacing="0">
                                            <thead>
                                            <tr>
                                                <th>Partition</th>
                                                <th>Leader</th>
                                                <th>Replicas</th>
                                                <th>In-sync Replicas</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${details}" var="detail">
                                                <tr>
                                                    <td>${detail.partition}</td>
                                                    <td>${detail.leader.host}:${detail.leader.port}</td>
                                                    <td>
                                                    <c:forEach items="${detail.replicas}" var="replica">
                                                        <div>${replica.host}:${replica.port}</div>
                                                    </c:forEach>
                                                    </td>
                                                    <td>
                                                        <c:forEach items="${detail.isr}" var="isr">
                                                            <div>${isr.host}:${isr.port}</div>
                                                        </c:forEach>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <span>Configuration (${fn:length(configs)})</span>
                                </h6>
                            </div>
                            <div class="panel-body" id="topic-offset-content">
                                <div class="visualizer-parent container-fluid" style="padding-bottom: 10px">
                                    <div class="row">
                                        <table class="table table-bordered" id="topic-table" width="100%" cellspacing="0">
                                            <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Value</th>
                                                <th>Source</th>
                                                <th>Read Only</th>
                                                <th>Sensitive</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${configs}" var="config">
                                                <tr>
                                                    <td>${config.name}</td>
                                                    <td>${config.value == '' ? '-' : config.value}</td>
                                                    <td>${config.source}</td>
                                                    <td>${config.readOnly}</td>
                                                    <td>${config.sensitive}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
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
        updateTitle("Topic Details");
    });
</script>

</body>
</html>
