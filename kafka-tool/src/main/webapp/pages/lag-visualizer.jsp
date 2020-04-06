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
                    <h1 class="h3 mb-0 text-gray-800">Consumer Group Visualizer</h1>
                </div>

                <!-- Page Body -->
                <div class="row">
                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    Offsets:
                                    <span title="Consumer group" style="color: saddlebrown">${groupId}</span>
                                    >
                                    <span title="Topic" style="color: saddlebrown">${topic}</span>
                                </h6>
                                <div class="dropdown no-arrow">
                                    <a class="dropdown-toggle" style="color: #4e73df">
                                        <i title="Sync" class="fa fa-sync fa-sm fa-fw main-icon sync-visualizer"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="visualizer-parent container-fluid" style="padding-bottom: 10px">
                                    <div class="row">
                                        <div class="col-xl-6 col-md-6"><span class="group-title">Topic</span></div>
                                        <div class="col-xl-1 col-md-1"><span class="group-title">Partition</span></div>
                                        <div class="col-xl-2 col-md-2"><span class="group-title">End Offset</span></div>
                                        <div class="col-xl-2 col-md-2"><span class="group-title">Current Offset</span></div>
                                        <div class="col-xl-1 col-md-1"><span class="group-title">Lag</span></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xl-12 col-md-12 mb-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    Reporter:
                                    <span title="Consumer group" style="color: saddlebrown">${groupId}</span>
                                    >
                                    <span title="Topic" style="color: saddlebrown">${topic}</span>
                                </h6>
                            </div>
                            <div class="card-body">
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
    var chart;
    var env = '${env}';
    var topic = '${topic}';
    var groupId = '${groupId}';
    var labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var data = [0, 10000, 5000, 15000, 10000, 20000, 15000, 25000, 20000, 30000, 25000, 40000];

    $(document).ready(function () {
        updateTitle("Lags Visualizer");

        // loadConsumerLag();
        loadLagReporter();

        $('.sync-visualizer').click(function() {
            loadConsumerLag();
        });
    });

    function updateChart() {
        chart.update();
    }

    function loadConsumerLag() {
        sendAjax({
            'url': '/monitor/consumer-lag?env=' + env + '&topic=' + topic + '&groupId=' + groupId
        }, 'get', function (data) {
            var element = $('.visualizer-parent');

            element.find('.visualizer-info').remove();
            var keys = Object.keys(data);
            keys.forEach(function (k) {
                var obj = data[k];
                var lag = obj.endOffset - obj.currentOffset;
                var html =
                    '<div class="row visualizer-info">' +
                    '<div class="col-xl-6 col-md-6">' + obj.topic + '</div>' +
                    '<div class="col-xl-1 col-md-1 visualizer-partition">' + obj.partition + '</div>' +
                    '<div class="col-xl-2 col-md-2">' + obj.endOffset + '</div>' +
                    '<div class="col-xl-2 col-md-2">' + obj.currentOffset + '</div>' +
                    '<div class="col-xl-1 col-md-1">' + (lag < 0 ? 0 : lag) + '</div>' +
                    '</div>';
                element.append(html);
            });

            sortByPartition();
        }, function () {
            console.log('Cannot load data');
        })
    }

    function sortByPartition() {
        var sortByPartition = function (a, b) {
            var aText = $(a).find('.visualizer-partition').text();
            var bText = $(b).find('.visualizer-partition').text();

            return Number(aText) > Number(bText) ? 1 : -1;
        };

        var list = $('.visualizer-info');
        list.sort(sortByPartition);
        for (var i = 0; i < list.length; i++) {
            list[i].parentNode.appendChild(list[i]);
        }
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
                            maxTicksLimit: 100
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            maxTicksLimit: 100,
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
