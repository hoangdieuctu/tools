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

function showLoading() {
    $('#loading-parent').show();
    $('#loading').show();
}

function hideLoading() {
    $('#loading-parent').hide();
    $('#loading').hide();
}

function connect() {
    showLoading();

    socket = new SockJS('/ws');
    stomp = Stomp.over(socket);
    stomp.connect({}, function () {
        showDisconnect();
        hideLoading();
        stomp.subscribe('/topic/' + $('#pods').val(), function (message) {
            var html = '<div class="data-content">' + message.body + '</div>';
            $('#data-parent').prepend(html);
            if (++currMsg > maxMsg) {
                $('.data-content').last().remove();
            }
        });
    }, function () {
        showConnect();
        hideLoading();
    });
}