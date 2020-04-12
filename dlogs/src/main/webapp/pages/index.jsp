<!DOCTYPE html>
<html lang="en">


<head>
    <title>KLogs</title>
    <style>
        #content {
            overflow-x: hidden;
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
    </style>
</head>

<body id="page-top">
<div id="content" class="content">
    <div class="row">
        <div class="col-xl-12 col-lg-12 col-md-12">
            <div id="data-parent"></div>
        </div>
    </div>
</div>

<link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">

<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/sockjs.min.js"></script>
<script src="/resources/js/stomp.min.js"></script>

<script>
    var currMsg = 0;
    var maxMsg = 500;
    var socket, stomp;
    var pod = 'ridershipmonitoringprocessor-7b47d6d8b4-sm2zc';
    $(document).ready(function () {
        connect();
    });


    function connect() {
        socket = new SockJS('http://localhost:8080/ws');
        stomp = Stomp.over(socket);

        stomp.connect({}, function () {
            stomp.subscribe('/topic/' + pod, function (message) {
                var html = '<div class="data-content">' + currMsg + '.' + message.body + '</html>';
                $('#data-parent').prepend(html);
                if (currMsg >= maxMsg) {
                    $('.data-content').last().remove();
                }
                currMsg++;
            });
        }, function (error) {
            console.log('Error: ' + error);
        });
    }
</script>

</body>
</html>
