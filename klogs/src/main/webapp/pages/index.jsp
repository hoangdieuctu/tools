<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <title>KLogs</title>
    <link rel='shortcut icon' href="/resources/images/logo.png" type='image/png'/>
    <style>
        #content {
            overflow-x: hidden;
        }

        #clear {
            margin-left: 20px;
        }

        #pods {
            line-height: 30px;
        }

        .pods-parent {
            position: fixed;
            top: 0;
            width: 100%;
            z-index: 1000;
            background: white;
            padding: 10px;
        }

        #data-parent {
            background: white;
            padding-left: 10px;
            padding-right: 10px;
        }

        .data-content {
            box-sizing: border-box;
            padding-bottom: 15px;
            margin: 0 0 1px 0;
            overflow: visible;
            font-family: monaco, menlo, consolas, 'courier prime', courier, 'courier new', monospace;
            font-size: 12px;
            word-wrap: break-word;
            width: 100%;
            height: 100%;
            border-radius: .25rem;
            border-color: #aaa;
        }

        .data-content:hover {
            background: #eaecf4;
        }

        .select2-selection__rendered {
            line-height: 30px !important;
        }

        .select2-container .select2-selection--single {
            height: 32px !important;
        }

        .select2-selection__arrow {
            height: 30px !important;
        }

        .pid {
            color: #e945df;
        }

        ._class {
            color: #008684;
        }

        .level {
            color: #2ddc44
        }

        .level_WARN {
            color: lightcoral !important;
        }

        .level_ERROR {
            color: red !important;
        }
    </style>
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

<link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="/resources/css/select2.min.css" rel="stylesheet" type="text/css">

<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/select2.min.js"></script>
<script src="/resources/js/sockjs.min.js"></script>
<script src="/resources/js/stomp.min.js"></script>

<script>
    var currMsg = 0;
    var maxMsg = 250;
    var socket, stomp;
    $(document).ready(function () {
        showConnect();

        $('#pods').select2({
            width: '100%'
        });

        $('#connect').click(function () {
            connect();
        });

        $('#disconnect').click(function () {
            disconnect();
        });

        $('#clear').click(function () {
            clear();
        });
    });

    function showConnect() {
        $('#pods').prop('disabled', false);
        $('#connect').show();
        $('#disconnect').hide();
    }

    function showDisconnect() {
        $('#pods').prop('disabled', true);
        $('#disconnect').show();
        $('#connect').hide();
    }

    function disconnect() {
        socket.close();
    }

    function clear() {
        removeContents();
        currMsg = 0;
    }

    function removeContents() {
        $('.data-content').remove();
    }

    function connect() {
        socket = new SockJS('/ws');
        stomp = Stomp.over(socket);
        stomp.connect({}, function () {
            showDisconnect();
            stomp.subscribe('/topic/' + $('#pods').val(), function (message) {
                var html = '<div class="data-content">'+message.body+'</div>';
                $('#data-parent').prepend(html);
                if (++currMsg > maxMsg) {
                    $('.data-content').last().remove();
                }
            });
        }, function () {
            showConnect();
        });
    }
</script>

</body>
</html>
