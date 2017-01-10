$(document).foundation();

var cqssc = (function() {
    cqssc.config = {
        peak: {
            AM: [0, 2],
            PM: [10, 12],
            interval: 5
        },
        usually: {
            times: [10, 22],
            interval: 10
        },
        curterm: 0
    };

    function cqssc() {
        if (!(this instanceof cqssc)) {
            return new cqssc();
        }
    }

    /**
     * [获取所有数据]
     * @param  {[type]} d [description]
     * @return {[type]}   [description]
     */
    cqssc.getAllCodes = function(d) {
        var defer = $.Deferred();
        var inx = layer.load(1);
        $.post("cqall", { date: d }, function(data) {
            defer.resolve(data);
            layer.close(inx);
        }, 'json');
        return defer.promise();
    };

    /**
     * [getCode description]
     * @param  {[type]} limit [description]
     * @return {[type]}       [description]
     */
    cqssc.getCode = function(term) {
        var defer = $.Deferred();
        $.post("cqhaoma", {
            "term": term
        }, function(data) {
            defer.resolve(data);
        }, 'json');
        return defer.promise();
    };

    /**
     * 全部数据加载完启动
     * @return {[type]} [description]
     */
    cqssc.start = function() {
        var date = undefined,
            hour = 0,
            minutes = 0,
            seconds = 0,
            //剩余多少秒获取数据
            surplusSeconds = 0,
            //提示
            prompt = '',
            cal = new Calendar();
        var interval = setInterval(function() {
            date = new Date();
            //2点后停止（23期是最后一期），早上10点开始
            hour = date.getHours();
            minutes = date.getMinutes();
            seconds = date.getSeconds();
            //获取当前元素坐标
            var target = $("div[data-inx='" + (cqssc.config.curterm) + "']");
            var crd = $(target).offset();
            //当前提示应该在那一列
            var curColumn = Math.floor(cqssc.config.curterm / 20);

            if (cqssc.config.curterm == 24 && hour <= 9 && minutes <= 58) {
                var startDate = new Date();
                startDate.setHours(9);
                startDate.setMinutes(58);
                seconds = cal.dateDiff(startDate, date);
                // 剩余更新秒数
                //
            } else {
                // 凌晨0点到2点 每5分钟请求一次，10点到 24点 每 10.50分钟请求一次
                if (hour > 0 && hour < 2) {

                } else {
                    if (minutes.toString().endsWith("0") && seconds > 51) {
                        $(".tips[data-inx='" + curColumn + "']").show().text('正在同步数据').animate({
                            top: crd.top
                        });
                        cqssc.getCode(cqssc.config.curterm).done(function(data) {
                            //surplus throw into ..
                            if (data) {
                                cqssc.fill(data, cqssc.config.curterm, true);
                            }
                        });
                    } else {
                        if (minutes.toString().endsWith("9") || (minutes.toString().endsWith("0") && seconds <= 51)) {
                            // open codes surplus ..
                            if (surplusSeconds == 0) {
                                surplusSeconds = 110;
                                //剩余获取数据时间

                                // var stopTime = new Date();
                                // if(minutes >= 59) {
                                //     if(hour > 23) {
                                //         stopTime.setDays();
                                //         stopTime.setHours(0);
                                //     }
                                // }
                            }
                            // $(target).text("剩余开奖时间  " + cal.formatSeconds(surplusSeconds--));
                            $(".tips[data-inx='" + curColumn + "']").show().text('剩余获取数据时间  ' + cal.formatSeconds(surplusSeconds--)).animate({
                                top: crd.top
                            });
                        } else {
                            if (surplusSeconds == 0) {
                                var stopTime = new Date();
                                var min = stopTime.getMinutes().toString();
                                if (min.length == 1) {
                                    min = 9;
                                } else {
                                    min = parseInt(min.charAt(0) + "9");
                                }
                                stopTime.setMinutes(min);
                                stopTime.setSeconds(0);
                                surplusSeconds = cal.dateDiff(stopTime, date);
                            }
                            $(".tips[data-inx='" + curColumn + "']").show().text('剩余时间  ' + cal.formatSeconds(surplusSeconds--)).animate({
                                top: crd.top
                            });
                        }
                    }
                }
            }
        }, 1000);
    };

    /**
     * 迭代数据
     * @param  {[type]} codes [description]
     * @return {[type]}       [description]
     */
    cqssc.iteration = function(codes) {
        codes = codes.reverse();
        var remTips = false;
        for (var i = 0; i < codes.length; i++) {
            if (i == codes.length - 1) {
                remTips = true;
            }
            cqssc.fill(codes[i], i, remTips);
        }
    }

    /**
     * 填充数据
     * @param  {[type]} codeJson [数据]
     * @param  {[type]} i        [当前元素索引]
     * @param  {[type]} remTips  [是否移除前面列的提示面板]
     * @return {[type]}          [description]
     */
    cqssc.fill = function(codeJson, i, remTips) {
        //指针移向下一期
        cqssc.config.curterm++;
        var col = $("[data-inx='" + i + "']").find(".columns");
        $(col[0]).text(codeJson.expect.substring(codeJson.expect.length - 3));
        var codes = codeJson.opencode.split(",");
        for (var j = 0; j < codes.length; j++) {
            $(col[1]).append('<span>' + codes[j] + '</span>');
        }
        // 移除当前列前面的提示列
        if (remTips) {
            var tarcol = Math.floor((cqssc.config.curterm) / 20);
            $(".tips").each(function(inx, item) {
                if (parseInt($(item).data("inx")) < tarcol) {
                    $(item).remove();
                }
            });
        }
    }

    /**
     * 分析数据
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    cqssc.analysis = function(kill, datas) {

    };

    /**
     * 获取数据
     * @param  {[type]}
     * @return {[type]}
     */
    cqssc.getAllCodes().done(function(codes) {
        var rows = $(".data-row");
        for (var i = 0; i < 120; i++) {
            $(rows[i]).attr("data-inx", i);
        }
        cqssc.iteration(codes);
        if (codes.length < 120) {
            cqssc.start();
        }
    });

    /**
     * 获取某日数据
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    $("#header a.swdate").click(function(event) {
        /* Act on the event */
        if (!$(this).hasClass('active')) {
            $(".data-row > .columns").empty();
            sessionStorage.removeItem("codes");
            if ($(this).hasClass("active")) return false;
            $(this).parent().find('a').removeClass('active');
            $(this).addClass('active');
            $(".cus-date").text("自选");
            var date = "",
                cal = new Calendar();
            switch ($(this).data("val")) {
                case 'tod':
                    date = new Date().Format("yyyy-MM-dd");
                    break;
                case 'ysd':
                    date = cal.getCustomDate(-1);
                    break;
                case 'bysd':
                    date = cal.getCustomDate(-2);
                    break;
            }
            cqssc.config.curterm = 0;
            cqssc.getAllCodes(date).done(function(codes) {
                cqssc.iteration(codes);
            });
        }
    });

    /**
     * 切换开奖数据分析
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    $("#header a.killtype").click(function(event) {
        /* Act on the event */
        var target = $("." + $(this).data("for") + "");
        $(target).text($(this).text()).addClass('trigger');
        $(this).parent().find('a').removeClass('active');
        $(this).addClass('active');
        setTimeout(function() {
            $(target).removeClass('trigger');
        }, 800);
    });

    $(".cus-date").click(function(event) {
        /* Act on the event */
        $(".datepicker-here").trigger('focus');
    });

    $('#cusdatebtn').datepicker({
        autoClose: true,
        maxDate: new Date(), // Now can select only dates, which goes after today
        onSelect: function(formattedDate, date, inst) {
            $(".cus-date").text(formattedDate).parent().find('a').removeClass('active');
            $(".cus-date").addClass('active');
            $(".data-row > .columns").empty();
            sessionStorage.removeItem("codes");
            cqssc.getAllCodes(formattedDate).done(function(codes) {
                cqssc.iteration(codes);
            });
        }
    })
}());
