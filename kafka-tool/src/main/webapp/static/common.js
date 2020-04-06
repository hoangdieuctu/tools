function sendAjax(params, type, success, error) {
    if(!params.disableLoadingBar) {
        showLoading();
    }
    $.ajax({
        url: encodeURI(params.url),
        data: params.data,
        type: type,
        processData: false,
        contentType: 'application/json',
        success: function (data) {
            if(!params.disableLoadingBar) {
                hideLoading();
            }
            success(data);
        },
        error: function (e) {
            console.log('Error: ' + e);
            if(!params.disableLoadingBar) {
                hideLoading();
            }
            error();
        }
    });
}

function showLoading() {
    $('#loading-parent').show();
    $('#loading').show();
}

function hideLoading() {
    $('#loading-parent').hide();
    $('#loading').hide();
}

function toastWarning(message) {
    toastr.warning(message, {}, toastOptions());
}

function toastSuccess(message) {
    toastr.success(message, {}, toastOptions());
}

function toastOptions() {
    return {
        timeOut: 1000,
        preventDuplicates: true
    };
}

function storeLocal(key, value) {
    localStorage.setItem(key, value);
}

function getLocal(key) {
    return localStorage.getItem(key);
}