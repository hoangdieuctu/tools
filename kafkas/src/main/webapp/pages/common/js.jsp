<!-- Bootstrap core JavaScript-->
<script src="/resources/vendor/jquery/jquery.min.js"></script>
<script src="/resources/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="/resources/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="/resources/js/sb-admin-2.min.js"></script>
<script src="/resources/js/select2.min.js"></script>
<script src="/resources/js/jsoneditor.min.js"></script>
<script src="/resources/js/toastr.min.js"></script>
<script src="/resources/js/moment.js"></script>
<script src="/resources/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="/resources/vendor/datatables/dataTables.bootstrap4.min.js"></script>

<script src="/resources/js/numeral.min.js"></script>

<!-- Custom script -->
<script src="/static/common.js?r=2.0"></script>
<script src="/static/page_load.js?r=1.3"></script>

<script>
    function updateTitle(page, override) {
        if(override) {
            document.title = page;
        } else {
            document.title = 'Kafka Tool | ' + page;
        }
    }
</script>
