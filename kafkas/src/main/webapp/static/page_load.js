$(document).ready(function () {
    onPageLoad();
    loadLikeCount();

    $('#like-icon').click(function () {
        onLikePage();
    });
});

function loadLikeCount() {
    sendAjax({
        'url': '/like',
        'disableLoadingBar': true
    }, 'get', function (data) {
        processLikeData(data);
    }, function () {
        console.log('Error while sending like');
    });
}

function onLikePage() {
    sendAjax({
        'url': '/like',
        'disableLoadingBar': true
    }, 'post', function (data) {
        processLikeData(data);
    }, function () {
        console.log('Error while sending like');
    });
}

function processLikeData(data) {
    $('#like-count').text(data.likeCount);
    if (data.liked) {
        $('#like-icon').addClass('red-icon');
    }
}

function onPageLoad() {
    var path = location.pathname.substr(1);

    var index = path.indexOf('/');
    if (index > -1) {
        path = path.substr(0, index);
    }

    if (path != '') {
        $('#' + path).addClass('active');
    }
}