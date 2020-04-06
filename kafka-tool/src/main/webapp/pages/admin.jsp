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
                    <h1 class="h3 mb-0 text-gray-800">Administrator</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Topics Exclusion (<span id="topic-exclusion-count">0</span>)</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i data-toggle="modal" data-target="#adding-topic-exclusion-modal" class="fas fa-plus fa-sm fa-fw"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body admin-body">
                                <div class="body-title">The topics that match with the patterns below will not be shown in anywhere.</div>
                                <ul class="topic-exclude-parent"></ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Client Consumers (<span id="client-consumer-count">0</span>)</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <%--<i title="Send a message" class="fa fa-bell fa-sm fa-fw notice-icon notice-all-clients"></i>--%>
                                        <i title="Sync consumers" class="fa fa-sync fa-sm fa-fw main-icon sync-client-consumers"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body admin-body">
                                <div class="list-group list-group-flush client-consumers"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Favorite Topics (<span id="fav-topic-count">0</span>)</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i data-toggle="modal" data-target="#adding-fav-topic-modal" class="fas fa-plus fa-sm fa-fw"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body admin-body">
                                <div class="list-group list-group-flush favorite-topics"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Produce Storage (<span id="produce-storage-count">0</span>)</h6>
                            </div>
                            <div class="panel-body admin-body">
                                <div class="list-group list-group-flush produce-storage"></div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-6 col-md-6 mb-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Produce Folder (<span id="produce-folder-count">0</span>)</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i data-toggle="modal" data-target="#adding-produce-folder-modal" class="fas fa-plus fa-sm fa-fw"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body admin-body">
                                <div class="list-group list-group-flush produce-folder"></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div id="adding-topic-exclusion-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Exclude</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="topic">Topic pattern</label>
                            <input class="form-control" type="text" id="topic">
                            <small class="form-text name-rules">The topics that match with this pattern will not be shown anywhere</small>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="modal-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-exclusion" type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="custom-message-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Send Message (<span class="consumer-id" id="session-id"></span>)</h5>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="msg-content">Content</label>
                        <textarea rows="5" class="form-control" id="msg-content"></textarea>
                        <small>The content will be displayed in the consumer client screen</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="send-custom-message" type="button" class="btn btn-primary">Send</button>
                </div>
            </div>
        </div>
    </div>

    <div id="deleting-favorite-topic-modal" class="modal fade" tabindex="-1" role="dialog">
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
                    <button id="confirm-delete-fav-topic" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div id="deleting-produce-storage-modal" class="modal fade" tabindex="-1" role="dialog">
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
                    <button id="confirm-delete-produce-storage" type="button" class="btn btn-primary" data-dismiss="modal">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div id="adding-fav-topic-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="fav-topic">Topic</label>
                            <input class="form-control" type="text" id="fav-topic">
                            <small class="form-text name-rules">Multiple topic names split by comma</small>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-fav-topic" type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="adding-produce-folder-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="fav-topic">Folder</label>
                            <input class="form-control" type="text" id="folder">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-folder" type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script>
    var selectedFavTopic;
    var selectedJsonName;
    var selectedFolder;

    $(document).ready(function () {
        loadTopicExclusion();
        loadClientConsumers();
        loadFavoriteTopics();
        loadProduceStorage();
        loadProduceFolders();

        $('#adding-topic-exclusion-modal').on('shown.bs.modal', function () {
            $('#topic').focus();
        });

        $('#adding-produce-folder-modal').on('shown.bs.modal', function () {
            $('#folder').focus();
        });

        $('#custom-message-modal').on('shown.bs.modal', function () {
            $('#msg-content').focus();
        });

        $('#adding-fav-topic-modal').on('shown.bs.modal', function () {
            $('#fav-topic').focus();
        });

        $('#save-exclusion').click(function() {
            var element = $('#topic');
            var topic = element.val();
            if(topic == '') {
                element.focus();
                return;
            }

            sendAjax({
                'url': '/admin/topic-exclusion',
                'data': topic
            }, 'post', function() {
                element.val('');
                hideAddingExclusionModal();
                toastSuccess('Saved');

                loadTopicExclusion();
            }, function() {
                toastWarning('Error!');
            })
        });

        $('.sync-client-consumers').click(function () {
            loadClientConsumers(function () {
                toastSuccess('Synced');
            });
        });

        $('#send-custom-message').click(function() {
            var element = $('#msg-content');
            var content = element.val();
            if(content == '') {
                element.focus();
                return;
            }

            var sessionId = $('#session-id').text();

            var url = '/admin/send-custom-message';
            if (sessionId != 'All') {
                url += '/' + sessionId;
            }

            sendAjax({
                'url': url,
                'data': content
            }, 'post', function () {
                $('#custom-message-modal').modal('toggle');
                toastSuccess('Sent');
                element.val('');
            }, function () {
                toastWarning('Error!');
                console.log('Send custom message error');
            })
        });

        $('.notice-all-clients').click(function() {
            showCustomMsgModal('All');
        });

        $('#confirm-delete-fav-topic').click(function() {
            sendAjax({
                'url': '/admin/favorite-topics/' + selectedFavTopic
            }, 'delete', function () {
                toastSuccess('Deleted');
                loadFavoriteTopics();
            }, function () {
                console.log('Cannot delete a favorite topic');
            })
        });

        $('#confirm-delete-produce-storage').click(function () {
            sendAjax({
                'url': '/produce/json/' + selectedFolder + '/' + selectedJsonName
            }, 'delete', function () {
                toastSuccess('Deleted');
                loadProduceStorage();
            }, function () {
                console.log('Cannot delete a favorite topic');
            })
        });

        $('#save-fav-topic').click(function() {
            var element = $('#fav-topic');
            var value = element.val();
            if(value == '') {
                element.focus();
                return;
            }

            sendAjax({
                'url': '/admin/favorite-topics/' + value
            }, 'post', function () {
                $('#adding-fav-topic-modal').modal('toggle');
                toastSuccess("Saved");
                loadFavoriteTopics();
                element.val('');
            }, function () {
                console.log('Cannot add a favorite topic');
            })
        });

        $('#save-folder').click(function() {
            var element = $('#folder');
            var value = element.val();
            if(value == '') {
                element.focus();
                return;
            }

            sendAjax({
                'url': '/admin/produce-folder/' + value
            }, 'post', function () {
                $('#adding-produce-folder-modal').modal('toggle');
                toastSuccess("Saved");
                loadProduceFolders();
                element.val('');
            }, function () {
                console.log('Cannot add a produce folder');
            })
        });
    });

    function loadClientConsumers(callback) {
        sendAjax({
            'url': '/admin/client-consumers'
        }, 'get', function (clients) {
            if(callback) {
                callback();
            }

            var elements = $('.client-consumers');
            elements.empty();

            var keys = Object.keys(clients);

            $('#client-consumer-count').text(keys.length);

            if (keys.length == 0) {
                elements.append('<div style="text-align: center">No client</div>');
                return;
            }

            keys.forEach(function(key) {
                var html = '<div class="list-group-item">' + clients[key].topic + ' (<span onclick="showCustomMsgModal(\''+key+'\')" title="Send a custom message" class="consumer-id">'+ key + '</span>)'
                                + '<span class="float-right">'
                                    + '<small> '
                                        + moment().diff(clients[key].date, 'minutes')  + 'm'
                                    + '</small> '
                                + '</span>'
                        + '</div>';
                elements.append(html);
            });
        }, function () {
            console.log('Cannot load client consumers');
        })
    }

    function hideAddingExclusionModal() {
        $('#adding-topic-exclusion-modal').modal('toggle');
    }

    function loadTopicExclusion(callback) {
        sendAjax({
            'url': '/admin/topic-exclusion'
        }, 'get', function (data) {
            var topics = data.topics;

            var element = $('.topic-exclude-parent');
            element.empty();

            $('#topic-exclusion-count').text(topics.length);

            if(topics.length == 0) {
                element.append('<li class="no-data">No data</li>');
            }

            topics.forEach(function (t) {
                var html = '<li class="topic-exclude">' + t + ' <span onclick="remove(\''+t+'\')" class="fas fa-times fa-sm fa-fw hidden-icon"></span></li>';
                element.append(html);
            });

            if(callback) {
                callback();
            }
        }, function () {
            console.log('Cannot load data');
        })
    }

    function remove(exclusion) {
        sendAjax({
            'url': '/admin/topic-exclusion',
            'data': exclusion
        }, 'delete', function() {
            toastSuccess('Deleted');
            loadTopicExclusion();
        }, function() {
            toastWarning('Error!');
        })
    }

    function showCustomMsgModal(sessionId) {
        $('#session-id').text(sessionId);
        $('#custom-message-modal').modal('toggle');
    }

    function loadFavoriteTopics(callback) {
        sendAjax({
            'url': '/admin/favorite-topics'
        }, 'get', function (data) {
            var elements = $('.favorite-topics');
            elements.empty();

            var topics = data.topics;

            $('#fav-topic-count').text(topics.length);

            if (topics.length == 0) {
                elements.append('<div style="text-align: center">No data</div>');
                return;
            }

            topics.sort().forEach(function(topic) {
                var html = '<div class="list-group-item">' + topic;
                    html += '<span class="float-right"><i onclick="deleteFavTopic(\''+topic+'\')" title="Delete" class="fa fa-trash-alt notice-icon action-icon"></i></span>';
                    html += '</div>';
                elements.append(html);
            });

            if(callback) {
                callback();
            }
        }, function () {
            console.log('Cannot load data');
        })
    }

    function loadProduceFolders(callback) {
        sendAjax({
            'url': '/admin/produce-folder'
        }, 'get', function (data) {
            var elements = $('.produce-folder');
            elements.empty();

            $('#produce-folder-count').text(data.length);

            if (data.length == 0) {
                elements.append('<div style="text-align: center">No data</div>');
                return;
            }

            data.sort().forEach(function(folder) {
                var html = '<div class="list-group-item">' + folder;
                html += '</div>';
                elements.append(html);
            });

            if(callback) {
                callback();
            }
        }, function () {
            console.log('Cannot load data');
        })
    }

    function deleteFavTopic(topic) {
        selectedFavTopic = topic;
        $('#deleting-favorite-topic-modal').modal('toggle');
    }

    function loadProduceStorage() {
        sendAjax({
            'url': '/produce/json'
        }, 'get', function (items) {
            var elements = $('.produce-storage');
            elements.empty();

            $('#produce-storage-count').text(items.length);

            if (items.length == 0) {
                elements.append('<div style="text-align: center">No data</div>');
                return;
            }

            items.sort(function (a, b) {
                return a.createdTime < b.createdTime ? 1 : -1
            }).forEach(function (item) {
                var html = '<div class="list-group-item">' + item.name + '<span class="saved-date">' + moment(item.createdTime).format("HH:mm DD/MM/YYYY") + '</span>';
                html += '<span class="float-right"><i onclick="deleteProduceStorage(\''+item.folder+'\',\''+item.fullName+'\')" title="Delete" class="fa fa-trash-alt notice-icon action-icon"></i></span>';
                html += '</div>';
                elements.append(html);
            });
        }, function () {
            console.log('Cannot load data');
        })
    }

    function deleteProduceStorage(folder, name) {
        selectedJsonName = name;
        selectedFolder = folder;
        $('#deleting-produce-storage-modal').modal('toggle');
    }
</script>
</body>
</html>
