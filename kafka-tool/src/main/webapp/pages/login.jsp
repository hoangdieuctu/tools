<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <jsp:include page="common/meta.jsp"/>
    <jsp:include page="common/css.jsp"/>
</head>

<body class="bg-gradient-primary">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-xl-5 col-lg-6 col-md-4">
            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Login</h1>
                                </div>
                                <form class="user" action="auth" method="post">
                                    <div class="form-group">
                                        <input name="username" type="text"
                                               class="form-control form-control-user" placeholder="User Name">
                                    </div>
                                    <div class="form-group">
                                        <input name="password" type="password"
                                               class="form-control form-control-user" placeholder="Password">
                                    </div>
                                    <div id="error" class="form-group" style="text-align: center; color: orange; display: none">
                                        <p class="small">Invalid user name or password!</p>
                                    </div>
                                    <input value="Login" type="submit" href="admin"
                                           class="btn btn-primary btn-user btn-block"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>
<script>
    $(document).ready(function () {
        updateTitle("Login");

        var href = window.location.href;
        if(href.indexOf('error') > 0) {
            $('#error').show();
        }
    });
</script>

</body>
</html>
