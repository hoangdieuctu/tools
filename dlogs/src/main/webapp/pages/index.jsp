<!DOCTYPE html>
<html lang="en">


<head>
    <title>KLogs</title>
</head>

<body id="page-top">
<div id="content" class="content">
</div>

<link href="/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">

<script src="/resources/js/jquery.min.js"></script>
<script src="/resources/js/sockjs.min.js"></script>
<script src="/resources/js/stomp.min.js"></script>

<script>
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
            });
        }, function (error) {
            console.log('Error: ' + error);
        });
    }
</script>

</body>
</html>
