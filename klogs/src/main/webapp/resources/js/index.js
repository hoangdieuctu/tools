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

function hideDateTime() {
    $('.time').css('display', 'none');
}

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
            var html = '<div class="data-content">' + message.body + '</div>';
            $('#data-parent').prepend(html);
            if (++currMsg > maxMsg) {
                $('.data-content').last().remove();
            }
        });
    }, function () {
        showConnect();
    });
}