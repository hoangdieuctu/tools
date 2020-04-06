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
                    <div class="col-xl-3 col-lg-3 col-md-3 mb-4">
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

                    <div class="col-xl-2 col-lg-2 col-md-2 mb-4">
                        <div>Partitions (<span id="partition-count">0</span>)</div>
                        <div>
                            <select id="partitions" class="kafka-select">
                                <option value="-1">Auto</option>
                            </select>
                        </div>
                    </div>

                    <div class="col-xl-3 col-lg-3 col-md-3 mb-4">
                        <div>Actions</div>
                        <div>
                            <a style="margin-left: 10px" data-toggle="modal" data-target="#setting-modal" id="setting" class="btn btn-light btn-icon-split" title="Settings">
                                <i class="fas fa-cogs" style="color: orange"></i>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-9 col-lg-9 col-md-9 mb-9 content-parent">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">Editor</h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i id="send" title="Send" class="fas fa-paper-plane" style="color: green; padding-right: 5px"></i>
                                        <i style="padding-left: 5px; padding-right: 5px" id="save" title="Save" class="fas fa-save main-icon" data-toggle="modal" data-target="#saving-modal"></i>
                                        <i style="padding-left: 5px; color: darkorchid" title="Format" id="format" class="fas fa-check"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body">
                                <i style="position: absolute; right: 5px; top: 60px;" title="Help" class="fa fa-question-circle main-icon" data-toggle="modal" data-target="#help-modal"></i>
                                <pre><div id="msg" contenteditable="true" spellcheck="false"></div></pre>
                            </div>
                        </div>
                        <div id="response-data" style="display: none">
                            <p class="data-text-content">Timestamp: <span id="timestamp"></span>, Partition: <span id="partition"></span>, Offset: <span id="offset"></span></p>
                        </div>
                    </div>
                    <div class="col-xl-3 col-lg-3 col-md-3">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 id="storage-title" class="m-0 font-weight-bold text-primary">Storage</h6>
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <span id="dropdown" class="fa fa-caret-down" style="cursor: pointer"></span>
                                    <div id="dropdown-content" style="display: none; position: absolute; z-index: 999; right: 15px; width: 150px; background: white; border: 1px solid #ced4da; border-radius: .25em; box-shadow: 0 4px 5px 3px rgba(0, 0, 0, 0.2);">
                                        <div style="font-size: 12px; margin-bottom: 10px; margin-top: 10px;">
                                            <div id="show-save" class="el-hover">
                                                <span class="fa fa-save" style="color: #3a3b45"></span>&nbsp; <span class="el-item">Storage</span>
                                            </div>
                                            <div id="show-history" class="el-hover">
                                                <span class="fa fa-history" style="color: #3a3b45"></span>&nbsp; <span class="el-item">History</span>
                                            </div>
                                        </div>
                                    </div>
                                </h6>
                            </div>
                            <div class="panel-body">
                                <div class="col-xl-12 col-lg-12 col-md-12 content-parent saved-json histories"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="saving-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Save</h5>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label for="file-name">Name</label>
                            <input class="form-control" type="text" id="file-name">
                            <small class="form-text name-rules">Not empty and maximum is 50 characters</small>
                        </div>

                        <div class="form-group">
                            <label for="file-name">Folder</label>
                            <select class="form-control storage-folder">
                                <c:forEach items="${folders}" var="f">
                                    <option value="${f}">${f}</option>
                                </c:forEach>
                            </select>
                            <small class="form-text">Contact admin to add more folder</small>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="modal-cancel" type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-file" type="button" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="help-modal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Help</h5>
                </div>
                <div class="modal-body">
                    <h5>Generator</h5>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Type</th>
                            <th scope="col">Params</th>
                            <th scope="col">Use</th>
                            <th scope="col">Return Type</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <th scope="row">random_int</th>
                            <td>Range: min, max</td>
                            <td>{{random_int(2,5)}}</td>
                            <td>Integer number</td>
                        </tr>
                        <tr>
                            <th scope="row">random_double</th>
                            <td>Range: min, max</td>
                            <td>{{random_double(2.4,5.3)}}</td>
                            <td>Double number</td>
                        </tr>
                        <tr>
                            <th scope="row">random_alphabetic</th>
                            <td>Text length</td>
                            <td>{{random_alphabetic(3)}}</td>
                            <td>Alphabetic string</td>
                        </tr>
                        <tr>
                            <th scope="row">random_alphanumeric</th>
                            <td>Text length</td>
                            <td>{{random_alphanumeric(3)}}</td>
                            <td>Alphabenumeric string</td>
                        </tr>
                        <tr>
                            <th scope="row">random_uuid</th>
                            <td>-</td>
                            <td>{{random_uuid}}</td>
                            <td>UUID string</td>
                        </tr>
                        <tr>
                            <th scope="row">current_millis</th>
                            <td>-</td>
                            <td>{{current_millis}}</td>
                            <td>Long number 13 characters</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
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
                            <label for="storage-folder">Working Folder</label>
                            <select id="storage-folder" class="form-control storage-folder">
                                <c:forEach items="${folders}" var="f">
                                    <option value="${f}">${f}</option>
                                </c:forEach>
                            </select>
                            <small class="hint form-text text-muted">Only show the files in selected folder</small>
                        </div>
                        <div class="form-group">
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
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                    <button id="save-setting" type="button" class="btn btn-primary" data-dismiss="modal">Save</button>
                </div>
            </div>
        </div>
    </div>

    <div id="delete-confirmation-modal" class="modal fade" tabindex="-1" role="dialog">
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

    <div class="menu">
        <div class="menu-options">
            <div style="font-size: 12px; margin-bottom: 10px; margin-top: 10px;">
                <div id="delete-file" class="el-hover" data-toggle="modal" data-target="#delete-confirmation-modal">
                    <span class="fa fa-trash" style="color: #3a3b45"></span>&nbsp; <span class="el-item">Delete</span>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script>
    var folder = 'Root';
    var isFavOnly = false;
    var isShowHistory = false;
    var startHistory = 0;
    var sizeHistory = 50;
    var loadMore = false;

    var ctrlDown = false;
    var ctrlKey = 17;
    var cmdKey = 91;
    var vKey = 86;

    var showContext = false;
    var contextFileName;

    $(document).ready(function () {
        window.addEventListener('click', function() {
            $('.menu').hide();
        });

        $(document).keydown(function (e) {
            if (e.keyCode == ctrlKey || e.keyCode == cmdKey) {
                ctrlDown = true;
            }
        }).keyup(function (e) {
            if (e.keyCode == ctrlKey || e.keyCode == cmdKey) {
                ctrlDown = false;
            }
        });

        $('#confirm-delete').click(function() {
            var folder = $('#storage-folder').val();
            sendAjax({
                'url': '/produce/json/' + folder + '/' + contextFileName
            }, 'delete', function () {
                toastSuccess('Deleted');
                loadJsonFiles();
            }, function () {
                console.log('Cannot delete a favorite topic');
            })
        });

        $('#msg').keydown(function (e) {
            if (ctrlDown && e.keyCode == vKey) {
                setTimeout(function() {
                    formatJsonEditor();
                }, 100);
            }
        });

        loadStorageFolder();
        loadLocalStorage();
        loadTopics($('#envs').val(), function() {
            loadJsonFiles();
        });

        $('.kafka-select').select2({
            width: '100%'
        });

        $('#envs').on('select2:select', function (e) {
            loadTopics(e.params.data.id);
        });

        $('#topics').on('select2:select', function (e) {
            loadPartitions(e.params.data.id);
        });

        $('#send').click(function () {
            var env = $('#envs').val();
            var topic = $('#topics').val();
            var partition = $('#partitions').val();
            var msg = $('#msg').text();
            if (msg != '') {
                try {
                    var json = JSON.parse(msg);
                    msg = JSON.stringify(json);
                } catch (e) {
                    console.log('Message is not a json format, keep data');
                }

                sendAjax({
                    'url': '/produce?env=' + env + '&topic=' + topic + '&partition=' + partition,
                    'data': msg
                }, 'post', function (data) {
                    toastSuccess('Sent');
                    console.log('Send kafka message success');
                    $('#timestamp').html(data.timestamp);
                    $('#partition').html(data.partition);
                    $('#offset').html(data.offset);

                    $('#response-data').show();

                    if(isShowHistory && !loadMore) {
                        startHistory = 0;
                        loadHistories();
                    }
                }, function () {
                    toastWarning('Error!');
                });
            }
        });

        $('#save-file').click(function () {
            var fileName = $('#file-name').val();
            if(fileName == '') {
                showNameRulesError();
                return;
            }

            if(fileName.length > 50) {
                showNameRulesError();
                return;
            }

            var json = $('#msg').text();
            var env = $('#envs').val();
            var topic = $('#topics').val();

            var content = {
                'json': json,
                'env': env,
                'topic': topic
            };

            var data = {
                'name': fileName,
                'content': content,
                'folder': folder
            };

            sendAjax({
                'url': '/produce/json',
                'data': JSON.stringify(data)
            }, 'post', function () {
                toastSuccess('Saved');
                console.log('Save file success');

                hideModal();
                resetModal();
                showSave();
                loadJsonFiles();
            }, function () {
                hideModal();
                toastWarning('Error!');
            });
        });

        $('#saving-modal').on('shown.bs.modal', function () {
            $('#file-name').focus();
        });

        $('#format').click(function () {
            formatJsonEditor(true);
        });

        $('#save-setting').click(function(){
            var newFavOnly = $('#show-fav-only').prop('checked');
            if(isFavOnly != newFavOnly) {
                isFavOnly = newFavOnly;
                storeLocal('showFavTopicsOnly', isFavOnly);
                loadTopics($('#envs').val());
            }

            if(!isShowHistory) {
                loadJsonFiles();
            }
        });

        $('#file-name').keyup(function () {
            var name = $('#file-name').val();
            if(name.length > 50) {
                showNameRulesError();
            } else {
                hideNameRulesError();
            }
        });

        $('#show-history').click(function() {
            showHistory();
        });

        $('#show-save').click(function() {
            showSave();
        });

        $('#dropdown').click(function () {
            var content = $('#dropdown-content');
            if (content.is(":hidden")) {
                content.show();
            } else {
                content.hide();
            }
        });

        $('.storage-folder').change(function(e) {
            var value = $(e.target).val();
            folder = value;
            storeLocal('storage-folder', value);
            $('.storage-folder').val(value); // update for all
        });
    });

    function loadPartitions(topic) {
        sendAjax({
            'url': '/produce/topic/details?env=' + $('#envs').val() + '&topicName=' + topic
        }, 'get', function (partitions) {
            $('#partitions').empty();
            $('#partition-count').text(partitions.length);
            partitions.forEach(function (p) {
                var option = '<option value="' + p.partition + '">' + p.partition + '</option>';
                $('#partitions').append(option);
            });

            var option = '<option value="-1">Auto</option>';
            $('#partitions').prepend(option);
            $('#partitions').val('-1');
        }, function () {
            toastWarning('Error!');
        });
    }

    function loadStorageFolder() {
        var storageFolder = getLocal('storage-folder');
        if(storageFolder) {
            folder = storageFolder;
            $('.storage-folder').val(storageFolder);
        }
    }

    function showHistory() {
        $('#dropdown-content').hide();
        $('#storage-title').text('History');
        isShowHistory = true;
        startHistory = 0;
        loadMore = false;
        loadHistories();
    }

    function showSave() {
        $('#dropdown-content').hide();
        $('#storage-title').text('Storage');
        isShowHistory = false;
        loadJsonFiles();
    }

    function formatJsonEditor(showWarning) {
        var jsonLine = /^( *)("[\w]+": )?("{.+}"|"[^"]*"|[\w.+-]*)?([,[{])?$/mg;
        var editor = $('#msg');
        var data = editor.text();

        if(data == '') {
            return;
        }
        try {
            var json = JSON.parse(data);
            editor.html(JSON.stringify(json, null, 3).replace(jsonLine, replacer));

            formatTimestamp();
        } catch (e) {
            console.log('Cannot parse json');
            if(showWarning) {
                toastWarning('Invalid json');
            }
        }
    }

    function formatTimestamp() {
        var jsonValues = $('.json-value');
        jsonValues.each(function (i, j) {
            var e = $(j);
            var text = e.text();
            if (/^\d{13}$/.test(text)) { // timestamp format
                var format = 'MMM DD YYYY, HH:mm:ss';
                var date = new Date(Number(text));
                var title = 'Local time: ' + moment(date).format(format) + '\nUTC time: ' + moment.utc(date).format(format);

                e.addClass('timestamp');
                e.prop('title', title);

                var inside = e.find('.current_timestamp');
                if (inside.length == 0) {
                    var insideHtml = '<span title="Current time" onclick="useCurrentTimestamp(this)" class="current_timestamp"><i class="fa fa-clock"></i></span>';
                    e.append(insideHtml);
                }
            }
        });
    }

    function useCurrentTimestamp(e) {
        $(e).parent().text(new Date().getTime());
        formatTimestamp();
    }

    function showNameRulesError() {
        $('.name-rules').css("color", "red");
    }

    function hideNameRulesError() {
        $('.name-rules').css("color", "#858796");
    }

    function resetModal() {
        $('.name-rules').css("color", "#858796");
        $('#file-name').val('');
    }

    function hideModal() {
        $('#saving-modal').modal('toggle');
    }

    function loadFile(f, fileName) {
        sendAjax({
            'url': '/produce/json/' + f + '/' + fileName
        }, 'get', function (data) {
            $('#msg').text(data.content.json);

            $('#envs').val(data.content.env);
            $('#envs').trigger('change');

            loadTopics(data.content.env, function() {
                $('#topics').val(data.content.topic);
                $('#topics').trigger('change');
            });
            formatJsonEditor();
        }, function () {
            console.log('Cannot load file');
            toastWarning('Error!');
        });
    }

    function loadJsonFiles() {
        sendAjax({
            'url': '/produce/json?folder=' + folder
        }, 'get', function (jsonFiles) {
            $('.saved-json').empty();

            jsonFiles.forEach(function (jsonFile) {
                var tooltip = jsonFile.name;
                var html =
                    '<div title="' + tooltip + '" class="saved-item">' +
                    '<div oncontextmenu="onRightClick(\''+ jsonFile.fullName + '\')" onclick="loadFile(\''+jsonFile.folder+'\',\''+jsonFile.fullName+'\')" class="file-name text-overflow"><i class="fas fa-save first-icon"></i>' + jsonFile.name + '</div>' +
                    '<span title="Create time" class="saved-date">' + moment(jsonFile.createdTime).format("HH:mm DD/MM/YYYY") + '</span>' +
                    '</div>';
                $('.saved-json').append(html);
            });
        }, function () {
            console.log('Cannot load json files');
        });
    }

    function onRightClick(fileName) {
        event.preventDefault();
        showContext = true;
        contextFileName = fileName;

        var screenX = event.pageX;
        var screenY = event.pageY;

        $('.menu').css('left', screenX);
        $('.menu').css('top', screenY);
        $('.menu').show();
    }

    function showMoreHistories() {
        loadMore = true;
        loadHistories();
    }

    function loadHistories() {
        sendAjax({
            'url': '/produce/histories?from=' + startHistory + '&size=' + sizeHistory
        }, 'get', function (histories) {
            startHistory += histories.length;

            if(!loadMore) {
                $('.histories').empty();
            }

            histories.forEach(function (history) {
                var tooltip = 'Partition: ' + history.response.partition
                    + '\nOffset: ' + history.response.offset
                    + '\nTimestamp: ' + history.response.timestamp
                    + '\nHost: ' + history.host;
                var html =
                    '<div class="saved-item tool-tip" data-toggle="tooltip" data-placement="auto" title="' + tooltip + '">' +
                    '<div onclick="loadHistory(\'' + history.id + '\')" class="file-name"><i class="fas fa-history first-icon"></i>' + truncate(history.content.topic) + '</div>' +
                    '<span class="saved-date">' + moment(history.timestamp).format("HH:mm DD/MM/YYYY") + '</span>' +
                    '</div>';
                $('.histories').append(html);
            });

            $('#show-more').remove();

            if(histories.length == sizeHistory) {
                var showMore = '<div id="show-more" class="file-name" onclick="showMoreHistories()">';
                showMore += '<span>Load more...</span>';
                showMore += '</div>';
                $('.histories').append(showMore);
            }
        }, function () {
            console.log('Cannot load histories');
        });
    }

    function loadHistory(id) {
        sendAjax({
            'url': '/produce/histories/' + id
        }, 'get', function (history) {
            $('#msg').text(history.content.json);

            $('#envs').val(history.content.env);
            $('#envs').trigger('change');

            loadTopics(history.content.env, function () {
                $('#topics').val(history.content.topic);
                $('#topics').trigger('change');
            });
            formatJsonEditor();
        }, function () {
            console.log('Cannot load history');
            toastWarning('Error!');
        });
    }

    function loadTopics(env, callback) {
        sendAjax({
            'url': '/produce/topics?env=' + env + '&isFavOnly=' + isFavOnly
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

            loadPartitions($('#topics').val());
        }, function () {
            toastWarning('Error!');
        });
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

    function loadLocalStorage() {
        var localFavOnly = getLocal('showFavTopicsOnly');
        isFavOnly = localFavOnly ? localFavOnly == 'true' : false;
        $('#show-fav-only').prop('checked', isFavOnly);
    }

    // keep length is ($('.histories').width()-36)/7 characters: if size =30 -> first 17, ..., last 10
    function truncate(text) {
        var maxLength = ($('.histories').width() - 36) / 7;
        if (text.length > maxLength) {
            var first = text.substring(0, maxLength - 13);
            var last = text.substring(text.length - 10, text.length);
            return first + '...' + last;
        }

        return text;
    }
</script>

</body>
</html>
