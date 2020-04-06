<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <jsp:include page="common/meta.jsp"/>
    <jsp:include page="common/css.jsp"/>
</head>

<body id="page-top">
<div id="wrapper" class="parent">
    <div class="child-right"></div>
    <!-- Left Menu -->
    <jsp:include page="common/left_menu.jsp"/>

    <div id="content-wrapper">
        <jsp:include page="common/loading.jsp"/>
        <div id="content">
            <div class="container-fluid" style="padding-top: 15px">
                <!-- Page Heading -->
                <div class="row">
                    <div class="col-xl-3 col-lg-3 col-md-3 mb-3">
                        <div>Environments</div>
                        <div>
                            <select id="envs" class="kafka-select">
                                <c:forEach items="${environments}" var="e">
                                    <option value="${e.name}">${e.name} - ${e.bootstrapServer}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="col-xl-4 col-lg-4 col-md-4 mb-4">
                        <div>Topics (<span id="topic-count">0</span>)</div>
                        <div>
                            <select id="topics" class="kafka-select">
                                <c:forEach items="${topics}" var="t">
                                    <option value="${t}">${t}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="col-xl-2 col-lg-2 col-md-2 mb-2">
                        <div>Offset</div>
                        <div>
                            <select id="offset" class="kafka-select">
                                <option value="latest">Latest</option>
                                <option value="oldest">Oldest</option>
                                <option value="advance">Advance</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-xl-2 col-lg-2 col-md-2 mb-2">
                        <div>Filter</div>
                        <div>
                            <input placeholder="Press 'enter' to apply" id="filter" class="data-filter" title="Input text and press 'Enter' to apply the filter">
                        </div>
                    </div>

                    <div class="col-xl-1 col-lg-1 col-md-1 mb-1">
                        <div>Actions</div>
                        <div>
                            <a id="play" class="btn btn-light btn-icon-split" title="Connect">
                                <i class="fas fa-play" style="color: green"></i>
                            </a>

                            <a id="pause" class="btn btn-light btn-icon-split" title="Disconnect" style="display: none">
                                <i class="fas fa-pause" style="color: red"></i>
                            </a>

                            <a style="margin-left: 10px" data-toggle="modal" data-target="#setting-modal" id="setting" class="btn btn-light btn-icon-split" title="Settings">
                                <i class="fas fa-cogs" style="color: orange"></i>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-lg-12 col-md-12 mb-12 content-parent">
                        <a id="delete" class="btn btn-light btn-icon-split btn-delete" title="Clear">
                            <i class="fas fa-trash"></i>
                        </a>
                        <div class="data-parent"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="setting-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Settings</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="history-items">History Items</label>
                            <select id="history-items" class="form-control">
                                <option>10</option>
                                <option>20</option>
                                <option>50</option>
                                <option selected="selected">100</option>
                                <option>200</option>
                                <option>500</option>
                                <option>1000</option>
                            </select>
                            <small class="hint form-text text-muted">The number of items will be kept on the main board</small>
                        </div>
                        <div class="form-group">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="show-metadata">
                                <label class="form-check-label" for="show-metadata">
                                    Show metadata
                                </label>
                                <small class="hint form-text text-muted" style="margin-top: 0px">Include: counter, key, timestamp, offset and partition</small>
                            </div>
                        </div>
                        <div class="form-group" id="fav-only-group">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="show-fav-only">
                                <label class="form-check-label" for="show-fav-only">
                                    Show favorite topics only
                                </label>
                                <small class="hint form-text text-muted" style="margin-top: 0px">Contact admin to add more favorite topics</small>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="modal-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-setting" type="button" class="btn btn-primary" data-dismiss="modal">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="custom-message-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Message</h5>
                </div>
                <div class="modal-body">
                    <div id="custom-message-content" class="custom-message-content"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div id="json-view" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">View</h5>
                </div>
                <div class="modal-body" style="padding: 0px !important;">
                    <pre><div id="msg"></div></pre>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div id="advance-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Advance</h5>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="partition-offset">Partition offset</label>
                        <input type="number" id="partition-offset" class="form-control">
                        <small class="hint form-text text-muted">The start offset, keep empty for beginning</small>
                    </div>

                    <div class="form-group">
                        <label for="partition-number">Partition number</label>
                        <input type="number" id="partition-number" class="form-control">
                        <small class="hint form-text text-muted">The specific partition, keep empty for all</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal">Done</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script src="/resources/js/sockjs.min.js"></script>
<script src="/resources/js/stomp.min.js"></script>

<script>
    var filter;
    var currMsg = 0;
    var keepMsg = 100;
    var socket, stompClient;
    var isShowMetadata = false;
    var isFavOnly = false;
    var customId;

    $(document).ready(function () {
        updateTitle("Consume");

        loadLocalStorage();
        loadTopics($('#envs').val());

        $('.kafka-select').select2({
            width: '100%'
        });

        $('#play').click(function() {
           connect();
        });

        $('#pause').click(function() {
            disconnect();
        });

        $('.content-parent').hover(function() {
           $('#delete').show();
        }, function() {
            $('#delete').hide();
        });

        $('#delete').click(function() {
            $('.data-parent').empty();
            currMsg = 0;
        });

        $('#filter').on('keypress', function (e) {
            if (e.which === 13) {
                filter = $('#filter').val();

                if($('#offset').val() === 'latest') {
                    toastSuccess("Set!");
                } else if (customId) { // apply backend filter for 'latest' or 'advance' type
                    sendAjax({
                        'url': '/consume/filter?randomId=' + customId + '&filter=' + filter
                    }, 'get', function () {
                        toastSuccess("Applied!")
                    }, function () {
                        toastWarning('Error!');
                    });
                }
            }
        });

        $('#save-setting').click(function() {
           keepMsg = $('#history-items').val();
            storeLocal('keepMsg', keepMsg);

            var newFavOnly = $('#show-fav-only').prop('checked');
            if(isFavOnly != newFavOnly) {
                isFavOnly = newFavOnly;
                storeLocal('showFavTopicsOnly', isFavOnly);
                loadTopics($('#envs').val());
            }

           isShowMetadata = $('#show-metadata').prop('checked');
           if(isShowMetadata) {
               showMetadata();
           } else {
               hideMetadata();
           }
           storeLocal('showMetadata', isShowMetadata);
        });

        $('#envs').on('select2:select', function (e) {
            loadTopics(e.params.data.id);
        });

        $("#offset").on('change', function(e) {
            var value = $(e.target).val();
            if(value == 'advance') {
                showAdvanceModal();
            }
        });
    });

    function showAdvanceModal() {
        $('#advance-modal').modal('toggle');
    }

    function showMetadata() {
        $('.metadata-hide').removeClass('metadata-hide');
    }

    function hideMetadata() {
        $('.additional-data').addClass('metadata-hide');
    }

    function connect() {
        showLoading();
        socket = new SockJS('/stream');
        stompClient = Stomp.over(socket);

        var env = $('#envs').val();
        var topic = $('#topics').val();
        var offset = $('#offset').val();

        customId = Math.random().toString(36).substring(7);

        var params = {
            'env': env,
            'topic': topic,
            'offset': (offset == 'advance' ? 'oldest' : offset), // advance is one type of oldest
            'customId': customId
        };

        if (offset == 'advance') {
            var partitionOffset = $('#partition-offset').val();
            if (partitionOffset != '') {
                params['partitionOffset'] = partitionOffset;
            }

            var partitionNumber = $('#partition-number').val();
            if (partitionNumber != '') {
                params['partitionNumber'] = partitionNumber;
            }
        }

        stompClient.connect(params, function () {
            $('#fav-only-group').hide();
            hideLoading();
            toastSuccess('Connected');
            console.log('On connected');
            $('#pause').show();
            $('#play').hide();
            $('#envs').prop('disabled', true);
            $('#topics').prop('disabled', true);
            $('#offset').prop('disabled', true);

            updateTitle('Consume | ' + topic, true);

            var subscribedTopic = '/topic/' + env + '_' + topic;
            if(offset === 'oldest' || offset === 'advance') {
                subscribedTopic += '_' + customId;
            }

            stompClient.subscribe(subscribedTopic, function (payload) {
                var body = payload.body;
                var json = JSON.parse(body);

                var msgType = json.messageType;
                if(msgType == 'CUSTOM') {
                    showCustomMessageModal(json);
                    return;
                }

                var value = json.value;
                if (filter != null && filter != '') {
                    if (value.indexOf(filter) >= 0) {
                        showData(json);
                    }
                } else {
                    showData(json);
                }
            });
        }, function(info) {
            $('#fav-only-group').show();
            toastWarning('Disconnected!');
            console.log('On disconnected');
            console.log(info);
            $('#pause').hide();
            $('#play').show();
            $('#envs').prop('disabled', false);
            $('#topics').prop('disabled', false);
            $('#offset').prop('disabled', false);
        });
    }

    function showData(json) {
        var metadataClass = !isShowMetadata ? 'metadata-hide' : '';

        var html = '<div class="data-content">';
        html += '<div class="additional-data ' + metadataClass + '">counter: ' + currMsg + ', key: ' + json.key + ', timestamp: ' + json.timestamp + ', offset: ' + json.offset + ', partition: ' + json.partition + '</div>';
        html += '<div class="data-json">' + json.value;
        html += '<span class="data-actions"><i onclick="view(this)" title="View" class="action main-icon fas fa-eye"></i></span>';
        html += '</div>';
        html += '</div>';

        $('.data-parent').prepend(html);

        if(currMsg >= keepMsg) {
            $('.data-content').last().remove();
        }

        currMsg++;
    }

    function view(e) {
        var parent = $(e).closest('.data-json');
        var json = parent.text();
        formatJson(json, function() {
            $('#json-view').modal('toggle');
        });
    }

    function formatJson(data, callback) {
        var jsonLine = /^( *)("[\w]+": )?("{.+}"|"[^"]*"|[\w.+-]*)?([,[{])?$/mg;
        var editor = $('#msg');

        if(data == '') {
            return;
        }
        try {
            var json = JSON.parse(data);
            editor.html(JSON.stringify(json, null, 3).replace(jsonLine, replacer));
            callback();
        } catch (e) {
        }
    }

    function replacer(match, pIndent, pKey, pVal, pEnd) {
        var key = '<span class=json-key>';
        var val = '<span class=json-value>';
        var str = '<span class=json-string>';
        var r = pIndent || '';
        if (pKey) {
            r = r + key + pKey.replace(/[: ]/g, '') + '</span>: ';
        }
        if (pVal) {
            r = r + (pVal[0] == '"' ? str : val) + pVal + '</span>';
        }

        return r + (pEnd || '');
    }

    function disconnect() {
        toastWarning('Disconnected!');
        socket.close();
    }

    function loadTopics(env) {
        sendAjax({
            'url': '/consume/topics?env=' + env + '&isFavOnly=' + isFavOnly
        }, 'get', function (topics) {
            $('#topics').empty();
            $('#topic-count').text(topics.length);
            topics.forEach(function (topic) {
                var option = '<option value="' + topic + '">' + topic + '</option>';
                $('#topics').append(option);
            });
        }, function(){
            toastWarning('Error!');
        });
    }

    function showCustomMessageModal(json) {
        $('#custom-message-content').text(json.message);
        $('#custom-message-modal').modal('show');
    }

    function loadLocalStorage() {
        var localKeepMsg = getLocal('keepMsg');
        if (localKeepMsg) {
            keepMsg = Number(localKeepMsg);
            $('#history-items').val(keepMsg);
        }

        var localFavOnly = getLocal('showFavTopicsOnly');
        isFavOnly = localFavOnly ? localFavOnly == 'true' : false;
        $('#show-fav-only').prop('checked', isFavOnly);

        var localShowMetadata = getLocal('showMetadata');
        isShowMetadata = localShowMetadata ? localShowMetadata == 'true' : false;
        $('#show-metadata').prop('checked', isShowMetadata);
    }
</script>

</body>
</html>
