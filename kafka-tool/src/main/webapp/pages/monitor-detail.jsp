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
                    <h1 class="h3 mb-0 text-gray-800">Group Details (<span style="font-style: italic" id="group-id"></span>)</h1>
                    <div class="switch">
                        <label style="margin-bottom: 0px !important;">
                            <span style="font-size: 14px">Auto refresh (s): </span>
                            <select id="refresh-rate">
                                <option value="-1">None</option>
                                <option value="1000">1</option>
                                <option value="3000">3</option>
                                <option value="5000">5</option>
                                <option value="10000" selected="selected">10</option>
                                <option value="20000">20</option>
                                <option value="50000">50</option>
                            </select>
                        </label>
                    </div>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <div>
                                        <span class="collapse-title" title="Click to show/hide the content" data-toggle="collapse" href="#partitioning-content" role="button" aria-expanded="false">Partitioning</span>
                                        <span title="Assignor" id="group-assignor" class="badge badge-success"></span>
                                        <span title="State" id="group-state" class="badge badge-info"></span>
                                    </div>
                                </h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i title="Sync" class="fa fa-sync fa-sm fa-fw main-icon sync-consumer-group"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body consumer-group-body collapse show" id="partitioning-content">
                                <div class="members-parent container-fluid" style="padding-bottom: 10px">
                                    <div class="row">
                                        <div class="col-xl-2 col-md-2"><span class="group-title">Host</span> <span id="sort-by-host" class="fa fa-sort"></span></div>
                                        <div class="col-xl-7 col-md-7"><span class="group-title">Topic</span> <span id="sort-by-topic" class="fa fa-sort"></span></div>
                                        <div class="col-xl-3 col-md-3"><span class="group-title">Partitions</span></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <span class="collapse-title" title="Click to show/hide the content" data-toggle="collapse" href="#topic-offset-content" role="button" aria-expanded="false">Topic Offset</span>
                                </h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i title="Sync" class="fa fa-sync fa-sm fa-fw main-icon sync-offset" style="display: none"></i>
                                        <i title="Loading" class="spinner-border text-primary spinner-loading-bar sync-offset-loading" role="status"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body consumer-group-body collapse show" id="topic-offset-content">
                                <div class="visualizer-parent container-fluid" style="padding-bottom: 10px">
                                    <div class="row">
                                        <div class="col-xl-6 col-md-6"><span class="group-title">Topic</span></div>
                                        <div class="col-xl-1 col-md-1"><span class="group-title">Partition</span></div>
                                        <div class="col-xl-2 col-md-2"><span class="group-title">Current Offset</span></div>
                                        <div class="col-xl-2 col-md-2"><span class="group-title">Latest Offset</span></div>
                                        <div class="col-xl-1 col-md-1"><span class="group-title">Lag</span></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <span class="collapse-title" title="Click to show/hide the content" data-toggle="collapse" href="#lag-visualizer-content" role="button" aria-expanded="false">Lag Visualizer</span>
                                </h6>
                                <div class="dropdown no-arrow" style="width: 500px">
                                    <select id="topics" class="topic-select"></select>
                                </div>
                            </div>
                            <div class="panel-body collapse show" id="lag-visualizer-content">
                                <div class="chart-area">
                                    <canvas id="lag-chart"></canvas>
                                </div>
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
<script src="/resources/vendor/chart.js/Chart.min.js"></script>

<script>
    var env = '${env}';
    var groupId = '${groupId}';

    var chartSize = 50;
    var hasTopicSelect = false;

    var loading = false;

    var chart;
    var labels = [];
    var data = [];

    var lags = {};

    var interval = 10000;

    var timer;

    $(document).ready(function () {
        updateTitle("Group Details");

        loadGroupDetail(env, groupId);
        loadConsumerLag(env, groupId);
        loadLagReporter();

        timer = setInterval(autoRefresh, interval, interval);

        function autoRefresh() {
            loadGroupDetail(env, groupId, true);
            if (!loading) {
                loadConsumerLag(env, groupId);
            }
        }

        $('#refresh-rate').change(function(e) {
            var value = $(e.target).val();
            clearInterval(timer);

            if(value != '-1') {
                timer = setInterval(autoRefresh, parseInt(value), parseInt(value));
            }
        });

        $('.topic-select').select2({
            width: '100%'
        });

        $('.topic-select').on('select2:select', function (e) {
            updateLagChart();
        });

        $('.sync-consumer-group').click(function() {
            loadGroupDetail(env, groupId, false, function() {
                toastSuccess('Synced');
            });
        });

        $('.sync-offset').click(function() {
           loadConsumerLag(env, groupId, function() {
               toastSuccess('Synced');
           });
        });

        $('#sort-by-host').click(function() {
            sortByHost();
        });

        $('#sort-by-topic').click(function() {
            sortByTopic();
        });
    });

    function loadGroupDetail(env, groupId, hideLoading, callback) {
        var disableLoading = !hideLoading ? false : true;
        sendAjax({
            'url': '/monitor/consumer-groups/' + env + '/describe/' + groupId,
            'disableLoadingBar': disableLoading
        }, 'get', function (data) {
            $('#group-id').text(data.groupId);
            $('#group-assignor').text(data.partitionAssignor == '' ? 'unknown' : data.partitionAssignor);
            $('#group-state').text(data.state.toLowerCase());

            var members = data.members;
            var elements = $('.members-parent');

            if (members.length == 0) {
                return;
            }

            elements.find('.group-info').remove();
            members.forEach(function(m) {
                var html =
                    '<div class="row group-info">' +
                        '<div class="col-xl-2 col-md-2 host">' + m.host + '</div>' +
                        '<div class="col-xl-7 col-md-7">' + buildTopicsHtml(m.assignment.partitions) + '</div>' +
                        '<div class="col-xl-3 col-md-3">' + buildPartitionsHtml(m.assignment.partitions) + '</div>' +
                    '</div>';
                elements.append(html);
            });

            sortByTopic();

            if(callback) {
                callback();
            }
        }, function () {
            console.log('Cannot load data');
        })
    }

    function buildTopicsHtml(partitions) {
        var html = '<div class="topic-wrapper">';
        if (partitions == null || partitions.length == 0) {
            html += '-';
        } else {
            partitions.forEach(function (p) {
                html += '<div title="' + p.topic + '" class="group-topic">' + p.topic + '</div>';
            });
        }
        html += '</div>';
        return html;
    }

    function buildPartitionsHtml(partitions) {
        var html = '<div class="partition-wrapper">';
        if (partitions == null || partitions.length == 0) {
            html += '-';
        } else {
            partitions.forEach(function (p) {
                html += '<div>';
                p.partitions.sort(function (a, b) {
                    return a > b ? 1 : -1
                }).forEach(function (n) {
                    html += '<span class="badge badge-info partition-badge">' + n + '</span>' ;
                });
                html += '</div>';
            });
        }
        html += '</div>';
        return html;
    }

    function sortByTopic() {
        var sortByTopic = function (a, b) {
            var aText = $(a).find('.group-topic').text();
            var bText = $(b).find('.group-topic').text();

            return aText.localeCompare(bText);
        };

        var list = $('.group-info');
        list.sort(sortByTopic);
        for (var i = 0; i < list.length; i++) {
            list[i].parentNode.appendChild(list[i]);
        }
    }

    function sortByHost() {
        var sortByHost = function (a, b) {
            var aText = $(a).find('.host').text();
            var bText = $(b).find('.host').text();

            return aText.localeCompare(bText);
        };

        var list = $('.group-info');
        list.sort(sortByHost);
        for (var i = 0; i < list.length; i++) {
            list[i].parentNode.appendChild(list[i]);
        }
    }

    function loadConsumerLag(env, groupId, callback) {
        loading = true;

        showSyncOffsetLoading();
        sendAjax({
            'url': '/monitor/consumer-lag?env=' + env + '&groupId=' + groupId,
            'disableLoadingBar': true
        }, 'get', function (items) {
            loading = false;

            var element = $('.visualizer-parent');

            element.find('.visualizer-info').remove();
            items.forEach(function (obj) {
                storeLags(obj); // for visualizer

                var built = buildElementHtml(obj);
                var html =
                    '<div class="row visualizer-info">' +
                    '<div class="col-xl-6 col-md-6"><div class="group-topic" title="' + obj.topicName + '">' + obj.topicName + '</div></div>' +
                    '<div class="col-xl-1 col-md-1">' + built.partition + '</div>' +
                    '<div class="col-xl-2 col-md-2">' + built.currentOffset + '</div>' +
                    '<div class="col-xl-2 col-md-2">' + built.latestOffset + '</div>' +
                    '<div class="col-xl-1 col-md-1">' + built.lag + '</div>' +
                    '</div>';
                element.append(html);
            });

            showSyncOffset();
            if(!hasTopicSelect) {
                updateVisualizerSelect(items);
            }

            if(callback) {
                callback();
            }

            updateLagChart();
        }, function () {
            loading = false;
            showSyncOffset();
            console.log('Cannot load data');
        })
    }

    function updateVisualizerSelect(items) {
        hasTopicSelect = true;

        var topic = $('.topic-select');
        items.forEach(function (item){
            var html = '<option value="' + item.topicName + '">' + item.topicName + '</option>';
            topic.append(html);
        });
    }

    function updateLagChart() {
        var selectedTopic = $('.topic-select').val();
        var items = lags[selectedTopic];

        clear(labels);
        clear(data);
        items.forEach(function(item) {
            labels.push(item.time);
            data.push(item.totalLag);
        });

        chart.update();
    }

    function clear(list) {
        while (list.length) {
            list.pop();
        }
    }

    function storeLags(obj) {
        var time = moment().format('HH:mm:ss');
        var lag = {
            'totalLag': obj.totalLag,
            'time': time
        };

        var lagHistories = lags[obj.topicName];
        if (lagHistories) {
            if(lagHistories.length == chartSize) {
                lagHistories.shift();
            }
            lagHistories.push(lag);
        } else {
            lags[obj.topicName] = [lag];
        }
    }

    function showSyncOffset() {
        $('.sync-offset').show();
        $('.sync-offset-loading').hide();
    }

    function showSyncOffsetLoading() {
        $('.sync-offset').hide();
        $('.sync-offset-loading').show();
    }

    function buildElementHtml(obj) {
        var offsets = obj.latestTopicOffsets;

        var keys = Object.keys(offsets);

        var partitionHtml = '<div class="partition-wrapper">';
        if (keys == null || keys.length == 0) {
            partitionHtml += '-';
        } else {
            keys.forEach(function (partition) {
                partitionHtml += '<div><span class="badge badge-info partition-badge">' + partition + '</span></div>';
            });
        }
        partitionHtml += '</div>';

        var currentOffsetHtml = '<div class="current-offset-wrapper">';
        if (keys == null || keys.length == 0) {
            currentOffsetHtml += '-';
        } else {
            keys.forEach(function (k) {
                currentOffsetHtml += '<div>' + obj.latestConsumerOffsets[k] + '</div>';
            });
        }
        currentOffsetHtml += '</div>';

        var latestOffsetHtml = '<div class="latest-offset-wrapper">';
        if (keys == null || keys.length == 0) {
            latestOffsetHtml += '-';
        } else {
            keys.forEach(function (k) {
                latestOffsetHtml += '<div>' + obj.latestTopicOffsets[k] + '</div>';
            });
        }
        latestOffsetHtml += '</div>';

        var lagHtml = '<div class="lag-wrapper">';
        if (keys == null || keys.length == 0) {
            lagHtml += '-';
        } else {
            keys.forEach(function (k) {
                lagHtml += '<div>' + obj.lagPerPartition[k] + '</div>';
            });
        }
        lagHtml += '</div>';

        var result = {
            'partition': partitionHtml,
            'currentOffset': currentOffsetHtml,
            'latestOffset': latestOffsetHtml,
            'lag': lagHtml
        };

        return result;
    }

    function loadLagReporter() {
        var ctx = document.getElementById("lag-chart");
        chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: "Lag",
                    lineTension: 0.3,
                    backgroundColor: "rgba(78, 115, 223, 0.05)",
                    borderColor: "rgba(78, 115, 223, 1)",
                    pointRadius: 3,
                    pointBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointBorderColor: "rgba(78, 115, 223, 1)",
                    pointHoverRadius: 3,
                    pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 2,
                    data: data
                }]
            },
            options: {
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                    }
                },
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'date'
                        },
                        gridLines: {
                            display: false,
                            drawBorder: false
                        },
                        ticks: {
                            maxTicksLimit: 50
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            maxTicksLimit: 50,
                            padding: 10,
                            callback: function (value, index, values) {
                                return value;
                            }
                        },
                        gridLines: {
                            color: "rgb(234, 236, 244)",
                            zeroLineColor: "rgb(234, 236, 244)",
                            drawBorder: false,
                            borderDash: [2],
                            zeroLineBorderDash: [2]
                        }
                    }]
                },
                legend: {
                    display: false
                },
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    titleMarginBottom: 10,
                    titleFontColor: '#6e707e',
                    titleFontSize: 14,
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    intersect: false,
                    mode: 'index',
                    caretPadding: 10,
                    callbacks: {
                        label: function (tooltipItem, chart) {
                            return 'Lag: ' + tooltipItem.yLabel;
                        }
                    }
                }
            }
        });
    }
</script>

</body>
</html>
