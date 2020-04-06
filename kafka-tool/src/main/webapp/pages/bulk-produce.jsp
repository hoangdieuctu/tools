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
                    <h1 class="h3 mb-0 text-gray-800">Bulk Produce</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Job</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i data-toggle="modal" data-target="#adding-bulk-produce-modal" class="fas fa-plus fa-sm fa-fw"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body bulk-produce-body">
                                <div id="requested-items" class="list-group list-group-flush"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Queue</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i id="cancel-running-queue" title="Cancel running tasks" class="fa fa-exclamation-triangle fa-sm fa-fw red-icon" style="padding-right: 20px"></i>
                                        <i id="refresh-queue" title="Refresh" class="fa fa-sync fa-sm fa-fw main-icon"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body bulk-produce-body">
                                <div id="queue-items" class="list-group list-group-flush"></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div id="adding-bulk-produce-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Bulk Job</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input class="form-control" type="text" id="name">
                            <small class="form-text name-rules">Not empty and maximum is 50 characters</small>
                        </div>

                        <div class="row">
                            <div class="col-xl-4 col-md-4 mb-4">
                                <label for="envs">Environment</label>
                                <select id="envs" class="kafka-select">
                                    <c:forEach items="${environments}" var="e">
                                        <option value="${e.name}">${e.name} - ${e.bootstrapServer}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-xl-8 col-md-8 mb-8">
                                <div class="form-group">
                                    <label for="envs">Topic</label>
                                    <select id="topics" class="kafka-select">
                                        <c:forEach items="${topics}" var="t">
                                            <option value="${t}">${t}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <hr />

                        <div class="row">
                            <div class="form-group col-xl-4 col-md-4 mb-4">
                                <label for="thread">Threads</label>
                                <select id="thread" class="kafka-select">
                                    <c:forEach items="${threads}" var="e">
                                        <option value="${e}">${e}</option>
                                    </c:forEach>
                                </select>
                                <small class="form-text">How fast of sending the messages</small>
                            </div>

                            <div class="form-group col-xl-4 col-md-4 mb-4">
                                <label for="element">Elements</label>
                                <select id="element" class="kafka-select">
                                    <c:forEach items="${elements}" var="e">
                                        <option value="${e}">${e}</option>
                                    </c:forEach>
                                </select>
                                <small class="form-text">How many messages a round</small>
                            </div>

                            <div class="col-xl-4 col-md-4 mb-4">
                                <label for="sleep">Sleep (ms)</label>
                                <input value="0" style="height: 28px; border: 1px solid #AAAAAA" id="sleep"
                                       type="number" class="form-control">
                                <small class="form-text">How long that we wait for each message</small>
                            </div>
                        </div>

                        <div style="padding-bottom: 20px" class="form-group">
                            <div class="row">
                                <div class="col-xl-8 col-md-8 mb-4">
                                    <label>Message(s)</label>
                                </div>
                                <div class="col-xl-4 col-md-4 mb-4">
                                    <label>Key(s)</label>
                                </div>
                            </div>
                            <div id="msgs" class="row" style="margin-top: -20px">
                                <div class="form-group col-xl-8 col-md-8 mb-4">
                                    <textarea class="data-content" rows="5"></textarea>
                                </div>
                                <div class="form-group col-xl-4 col-md-4 mb-4">
                                    <input style="height: 28px; border: 1px solid #AAAAAA" type="text" class="form-control data-key">
                                </div>
                            </div>
                            <i id="add-msg-field" class="fas fa-plus float-right"></i>
                        </div>

                        <hr />

                        <div class="row">
                            <div class="col-xl-12 col-md-12 mb-12">
                                <div class="row">
                                    <div class="form-group col-xl-4 col-md-4 mb-4">
                                        <label for="loopCount">Loop Times</label>
                                        <input value="1" style="height: 28px; border: 1px solid #AAAAAA" id="loopCount" type="number" class="form-control">
                                        <small class="form-text">How many rounds</small>
                                    </div>

                                    <div class="form-group col-xl-4 col-md-4 mb-4">
                                        <label for="loopSleep">Loop Interval (s)</label>
                                        <input value="0" style="height: 28px; border: 1px solid #AAAAAA" id="loopSleep" type="number" class="form-control">
                                        <small class="form-text">How long that we wait each round</small>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <hr />

                        <div class="row">
                            <div class="col-xl-12 col-md-12 mb-12">
                                <div>
                                    <span style="font-weight: bold">Summary: </span>Total messages will be sent is <span id="total-msg" style="color: green; font-weight: bold">0</span> in <span id="total-seconds" style="color: green; font-weight: bold">0 second</span>
                                    <i onclick="calculateSummary()" id="update-summary" title="Calculate" class="fa fa-sync fa-sm fa-fw main-icon"></i>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save" type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="deleting-bulk-produce-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm</h5>
                </div>
                <div class="modal-body">
                    <p>Do you want to delete?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="confirm-delete" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div id="executing-bulk-produce-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm</h5>
                </div>
                <div class="modal-body">
                    <div>Slaves: <span id="slaves"></span></div>
                    <div>
                        <label>Run on: </label>
                        <select id="run_on" style="padding-left: 5px; width: 200px">
                            <option value="master">Master Only</option>
                            <option value="master_slave">Master & Slaves</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="confirm-execute" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div id="cancel-running-task-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm</h5>
                </div>
                <div class="modal-body">
                    <p>Do you want to cancel all running tasks?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="confirm-cancel" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script>
    var selectedName;
    var selectedTopic;

    $(document).ready(function() {
        updateTitle("Bulk Produce");

        loadRequestedItems();
        loadQueueItems(function() {
            startTimerPullQueue();
        });

        $('.kafka-select').select2({
            width: '100%'
        });

        $('#envs').on('select2:select', function (e) {
            loadTopics(e.params.data.id);
        });

        $('#executing-bulk-produce-modal').on('shown.bs.modal', function () {
            loadSlaves();
        });

        $('#adding-bulk-produce-modal').on('shown.bs.modal', function () {
            loadTopics($('#envs').val(), function() {
                if(selectedTopic) {
                    $('#topics').val(selectedTopic);
                }
            });
            $('#name').focus();
        });

        $('#cancel-running-queue').click(function() {
            $('#cancel-running-task-modal').modal('toggle');
        });

        $('#confirm-cancel').click(function () {
            sendAjax({
                'url': '/bulk-produce/queue/running'
            }, 'delete', function () {
                loadQueueItems(function () {
                    toastSuccess('Cancled');
                });
            }, function () {
                toastWarning('Error!');
            });
        });

        $('#save').click(function() {
            var name = $('#name').val();
            if(name == '') {
                $('#name').focus();
                return;
            }

            if(name.length > 50) {
                $('#name').focus();
                return;
            }

            var msgs = [];
            var msgElements = $('.data-content');
            for (var i=0; i<msgElements.length; i++) {
                var value = msgElements[i].value;
                if(value.length != 0) {
                    msgs.push(value);
                }
            }

            if(msgs.length == 0) {
                $(msgElements[0]).focus();
                return;
            }

            var keys = [];
            var keyElements = $('.data-key');
            for (var j=0; j<keyElements.length; j++) {
                var key = keyElements[j].value;
                keys.push(key);
            }

            var env = $('#envs').val();
            var topic = $('#topics').val();
            var sleep = $('#sleep').val();
            var thread = $('#thread').val();
            var element = $('#element').val();
            var loopCount = $('#loopCount').val();
            var loopSleep = $('#loopSleep').val();

            var data = {
                'name': name,
                'env': env,
                'threadCount': thread,
                'elementCount': element,
                'loopCount': loopCount,
                'loopSleep': loopSleep,
                'topic': topic,
                'sleep': sleep,
                'body': msgs,
                'keys': keys
            };

            sendAjax({
                'url': '/bulk-produce/request',
                'data': JSON.stringify(data)
            }, 'post', function () {
                toastSuccess('Saved');
                console.log('Save bulk request success');
                $('#adding-bulk-produce-modal').modal('toggle');

                loadRequestedItems();
            }, function () {
                toastWarning('Error!');
            });
        });

        $('#confirm-delete').click(function() {
            sendAjax({
                'url': '/bulk-produce/request/' + selectedName
            }, 'delete', function() {
                toastSuccess('Deleted');
                loadRequestedItems();
            }, function() {
                toastWarning('Error!');
            });
        });

        $('#confirm-execute').click(function () {
            var runOn = $('#run_on').val();
            var slaveCount = 0;
            if(runOn == 'master_slave') {
                slaveCount = $('#slaves').text();
            }

            sendAjax({
                'url': '/bulk-produce/request/' + selectedName + '/execute?slaves=' + slaveCount
            }, 'post', function () {
                toastSuccess('Scheduled');
                loadQueueItems();
            }, function () {
                toastWarning('Error!');
            });
        });

        $('#refresh-queue').click(function() {
            loadQueueItems(function() {
                toastSuccess('Synced');
            });
        });

        $('#add-msg-field').click(function() {
            var html =
                '<div class="form-group col-xl-8 col-md-8 mb-4">' +
                    '<textarea class="data-content" rows="5"></textarea>' +
                '</div>' +
                '<div class="form-group col-xl-4 col-md-4 mb-4">' +
                    '<input style="height: 28px; border: 1px solid #AAAAAA" type="text" class="form-control data-key">' +
                '</div>';

            $('#msgs').append(html);
        })
    });

    function loadSlaves() {
        var elements = $('#slaves');
        sendAjax({
            'url': '/distribute/slaves'
        }, 'get', function (items) {
            elements.text(items.length);
        }, function () {
            elements.text('0');
        });
    }

    function loadRequestedItems(callback) {
        sendAjax({
            'url': '/bulk-produce/request'
        }, 'get', function (items) {
            var elements = $('#requested-items');
            elements.empty();

            if(items.length == 0) {
                elements.append('<div style="text-align: center">No data</div>');
                return;
            }

            items.forEach(function (name) {
                var html = '<div class="list-group-item">' + name
                            + '<span class="float-right">'
                                + '<i onclick="onPlay(\''+name+'\')" title="Execute" class="fa fa-paper-plane green-color action-icon"></i> '
                                + '<i onclick="onEdit(\''+name+'\')" title="Edit" class="fa fa-edit main-icon action-icon"></i> '
                                + '<i onclick="onDelete(\''+name+'\')" title="Delete" class="fa fa-trash-alt notice-icon action-icon"></i> '
                            + '</span>\n'
                        + '</div>';
                elements.append(html);
            });

            if(callback) {
                callback();
            }
        }, function () {
            toastWarning('Error!');
        });
    }

    function loadTopics(env, callback) {
        sendAjax({
            'url': '/produce/topics?env=' + env,
            'disableLoadingBar': false
        }, 'get', function (topics) {
            $('#topics').empty();
            $('#topic-count').text(topics.length);
            topics.forEach(function (topic) {
                var option = '<option value="' + topic + '">' + topic + '</option>';
                $('#topics').append(option);
            });

            if(callback) {
                callback();
            }
        }, function () {
            toastWarning('Error!');
        });
    }

    function loadQueueItems(callback) {
        sendAjax({
            'url': '/bulk-produce/queue',
            'disableLoadingBar': true
        }, 'get', function (items) {
            $('#queue-items').empty();

            if (items.running.length == 0 && items.scheduled.length == 0) {
                $('#queue-items').append('<div style="text-align: center">No data</div>');
            }

            items.running.forEach(function (item) {
                var html = '<div class="list-group-item">' + item.name + '<span class="badge badge-success float-right">Running (' + item.percent + '%)</span></div>';
                $('#queue-items').append(html);
            });

            items.scheduled.forEach(function (item) {
                var html = '<div class="list-group-item">' + item.name + '<span class="badge badge-primary float-right">Scheduled</span></div>';
                $('#queue-items').append(html);
            });

            if(callback) {
                callback();
            }
        }, function () {
            toastWarning('Error!');
        });
    }

    function startTimerPullQueue() {
        setInterval(function () {
            loadQueueItems();
        }, 5000);
    }

    function onEdit(name) {
        selectedName = name;

        sendAjax({
            'url': '/bulk-produce/request/' + name,
            'disableLoadingBar': true
        }, 'get', function(data) {
            selectedTopic = data.topic;

            $('#name').val(data.name);
            $('#envs').val(data.env);
            $('#thread').val(data.threadCount);
            $('#element').val(data.elementCount);
            $('#topics').val(data.topic);
            $('#sleep').val(data.sleep);
            $('#loopCount').val(data.loopCount);
            $('#loopSleep').val(data.loopSleep);

            var parent = $('#msgs');
            parent.empty();
            data.body.forEach(function (body, index) {
                var key = "";
                if (data.keys && data.keys.length != 0) {
                    key = data.keys[index];
                }

                var html =
                    '<div class="form-group col-xl-8 col-md-8 mb-4">' +
                        '<textarea class="data-content" rows="5">' + body + '</textarea>' +
                    '</div>' +
                    '<div class="form-group col-xl-4 col-md-4 mb-4">' +
                        '<input value="' + key + '" style="height: 28px; border: 1px solid #AAAAAA" type="text" class="form-control data-key">' +
                    '</div>';

                parent.append(html);

            });

            $('#envs').trigger('change');
            $('#thread').trigger('change');
            $('#element').trigger('change');
            $('#topics').trigger('change');

            $('#adding-bulk-produce-modal').modal('toggle');
        }, function() {
            toastWarning('Error!');
        })
    }

    function onDelete(name) {
        selectedName = name;
        $('#deleting-bulk-produce-modal').modal('toggle');
    }

    function onPlay(name) {
        selectedName = name;
        $('#executing-bulk-produce-modal').modal('toggle');
    }

    function calculateSummary() {
        var element = $('#element').val();
        var loopCount = $('#loopCount').val();

        var messageCount = 0;
        var msgElements = $('.data-content');
        for (var i=0; i<msgElements.length; i++) {
            var value = msgElements[i].value;
            if(value.length != 0) {
                messageCount++;
            }
        }

        var totalMsg = element * messageCount * loopCount;
        if (totalMsg <= 0) {
            $('#total-msg').text('0');
        } else {
            $('#total-msg').text(numeral(totalMsg).format('0,0'));
        }

        var loopSleep = $('#loopSleep').val();
        var totalSeconds = loopCount * loopSleep;

        if(totalSeconds <= 0) {
            $('#total-seconds').text('0 second');
        } else {
            $('#total-seconds').text(secondsToHms(totalSeconds));
        }
    }

    function secondsToHms(d) {
        d = Number(d);
        var h = Math.floor(d / 3600);
        var m = Math.floor(d % 3600 / 60);
        var s = Math.floor(d % 3600 % 60);

        var hDisplay = h > 0 ? h + (h == 1 ? " hour " : " hours ") : "";
        var mDisplay = m > 0 ? + m + (m == 1 ? " minute " : " minutes ") : "";
        var sDisplay = s > 0 ? + s + (s == 1 ? " second" : " seconds") : "";
        return hDisplay + mDisplay + sDisplay;
    }
</script>

</body>
</html>
