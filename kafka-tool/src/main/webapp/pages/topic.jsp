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
                    <h1 class="h3 mb-0 text-gray-800">Topic</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    Topics (<span id="topics-count">0</span>)
                                </h6>
                                <div class="dropdown no-arrow" style="width: 590px">
                                    <div style="width: 270px; float: right">
                                        <select id="envs" class="kafka-select">
                                            <c:forEach items="${environments}" var="e">
                                                <option value="${e.name}">${e.name} - ${e.bootstrapServer}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body topics-body">
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered" id="topic-table" width="100%" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th>Name</th>
                                                    <th>Partition</th>
                                                </tr>
                                            </thead>
                                            <tbody id="topic-content">
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
    var table;
    $(document).ready(function () {
        updateTitle("Topic");

        table = $('#topic-table');

        table.dataTable({
            columns: [
                {
                    data: 'name', render: function (data, type, full, meta) {
                        return getTopicDetailUrl(data);
                    }
                },
                {
                    data: 'partitions.length'
                }
            ],
            columnDefs: [
                {"width": "80%", "targets": 0}
            ]
        });

        loadTopics($('#envs').val());

        $('.kafka-select').select2({
            width: '100%'
        });

        $('#envs').on('select2:select', function (e) {
            loadTopics(e.params.data.id);
        });
    });

    function getTopicDetailUrl(topic) {
        return '<a href="/topic/detail?env=' + $('#envs').val() + '&topic=' + topic + '">' + topic + '</a>';
    }

    function loadTopics(env) {
        sendAjax({
            'url': '/topic/all?env=' + env
        }, 'get', function (topics) {
            $('#topics-count').text(topics.length);

            table.dataTable().fnClearTable();
            table.dataTable().fnAddData(topics);
        }, function () {
            console.log('Cannot load data');
        })
    }
</script>

</body>
</html>
