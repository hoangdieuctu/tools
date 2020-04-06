<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<head>
    <jsp:include page="common/meta.jsp"/>
    <jsp:include page="common/css.jsp"/>
</head>

<body id="page-top">
<div id="wrapper">
    <!-- Left Menu -->
    <jsp:include page="common/left_menu.jsp"/>

    <div id="content-wrapper">
        <div id="content">
            <div class="container-fluid">
                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12" style="padding-top: 20px;">
                        <div class="group-title">
                            <p>Operation</p>
                        </div>
                        <a href="consume" style="text-decoration: none; padding-right: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-download fa-2x card-icon"></div>
                                    <div class="card-text-content">Consume</div>
                                </div>
                            </div>
                        </a>

                        <a href="produce" style="text-decoration: none; padding-left: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-upload fa-2x card-icon"></div>
                                    <div class="card-text-content">Produce</div>
                                </div>
                            </div>
                        </a>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12" style="padding-top: 30px;">
                        <div class="group-title">
                            <p>Management</p>
                        </div>
                        <a href="/admin" style="text-decoration: none; padding-left: 10px; padding-right: 10px">
                            <div class="card-parent">
                                <div>
                                    <div class="fas fa-fw fa-lock fa-2x card-icon"></div>
                                    <div class="card-text-content">Administrator</div>
                                </div>
                            </div>
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

</body>
</html>
