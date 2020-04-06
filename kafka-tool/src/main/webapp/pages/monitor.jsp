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
        <jsp:include page="common/loading.jsp"/>
        <div id="content">
            <div class="container-fluid" style="padding-top: 15px">
                <!-- Page Heading -->
                <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Consumer Group</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    Groups (<span id="consumer-groups-count">0</span>)
                                </h6>
                                <div class="dropdown no-arrow" style="width: 590px">
                                    <div style="width: 300px; float: left">
                                        <span style="position: absolute; top: 2px; left: 2px">
                                            <svg class="svgIcon-use" width="25" height="25">
                                                <path d="M20.067 18.933l-4.157-4.157a6 6 0 1 0-.884.884l4.157 4.157a.624.624 0 1 0 .884-.884zM6.5 11c0-2.62 2.13-4.75 4.75-4.75S16 8.38 16 11s-2.13 4.75-4.75 4.75S6.5 13.62 6.5 11z"></path>
                                            </svg>
                                        </span>
                                        <input id="search-group" placeholder="Search..." spellcheck="false"/>
                                    </div>
                                    <div style="width: 270px; float: right">
                                        <select id="envs" class="kafka-select">
                                            <c:forEach items="${environments}" var="e">
                                                <option value="${e.name}">${e.name} - ${e.bootstrapServer}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-body consumer-group-body">
                                <div class="list-group list-group-flush consumer-group-parent"></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<!-- Import Common JavaScript -->
<jsp:include page="common/js.jsp"/>

<script>
    var groupItems;

    $(document).ready(function () {
        updateTitle("Consumer Group");

        loadConsumerGroups($('#envs').val());

        $('.kafka-select').select2({
            width: '100%'
        });

        $('#envs').on('select2:select', function (e) {
            loadConsumerGroups(e.params.data.id);
        });

        $('#search-group').keyup(function () {
            searchConsumerGroup();
        });
    });

    function getGroupUrl(group) {
        return '<a href="/consumer-group/detail?env=' + $('#envs').val() + '&groupId=' + group + '">' + group + '</a>';
    }

    function loadConsumerGroups(env) {
        sendAjax({
            'url': '/monitor/consumer-groups/' + env
        }, 'get', function (groups) {
            var element = $('.consumer-group-parent');
            element.empty();

            $('#consumer-groups-count').text(groups.length);

            if(groups.length == 0) {
                element.append('<div class="no-data">No data</div>');
            }

            var cacheSize = 5;
            var cacheGroups = [];
            var cacheMaps = {};

            groups.sort().forEach(function(group) {
                var random = Math.random().toString(36).substring(7);
                var html = '<div id="' + random + '" class="list-group-item state-default">' + (group == '' ? '-' : getGroupUrl(group));
                html += '<span onclick=deleteGroup(\"' + group + '",\"'+ random + '") class="float-right"><i title="Delete" class="fas fa-trash-alt notice-icon action-icon"></i></span>';
                html += '</div>';
                element.append(html);

                if(group != '') {
                    cacheGroups.push(group);
                    cacheMaps[group] = random;
                }

                if(cacheGroups.length == cacheSize) {
                    describeGroup(env, cacheGroups, cacheMaps);
                    cacheGroups = [];
                    cacheMaps = {};
                }
            });

            // for the last one
            if(cacheMaps.length != 0) {
                describeGroup(env, cacheGroups, cacheMaps);
                cacheGroups = [];
                cacheMaps = {};
            }

            groupItems = $('.list-group-item');
            searchConsumerGroup();
        }, function () {
            console.log('Cannot load data');
        })
    }

    function deleteGroup(group, random) {
        if (confirm("Do you want to delete the consumer group?")) {
            sendAjax({
                'url': '/admin/consumer-group?env=' + $('#envs').val() + '&groupId=' + group,
                'disableLoadingBar': false
            }, 'delete', function () {
                toastSuccess("Deleted");
                $('#' + random).remove();
            }, function () {
                toastWarning("Error!");
            })
        }
    }

    function describeGroup(env, groupIds, maps) {
        sendAjax({
            'url': '/monitor/consumer-groups/' + env + '/describe',
            'data': JSON.stringify(groupIds),
            'disableLoadingBar': true
        }, 'post', function (response) {
            var keys = Object.keys(response);
            keys.forEach(function(key) {
                var random = maps[key];

                var data = response[key];
                var state = data.state.toLowerCase();
                if(state != 'stable') {
                    state = 'empty';
                }

                var e = $('#' + random);
                e.removeClass('state-default');
                e.addClass('state-' + state);
            });
        }, function () {
            console.log('Cannot load data');
        })
    }

    function searchConsumerGroup() {
        var text = $('#search-group').val();
        if(text == '') {
            groupItems.show();
            return;
        }

        var search = text.toLowerCase();

        groupItems.each(function () {
            var s = $(this).text();
            if (s.toLowerCase().indexOf(search) != -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
</script>

</body>
</html>
