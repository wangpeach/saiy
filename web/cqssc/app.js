$(document).foundation();

jQuery(document).ready(function ($) {
    var cq = {
        config: {
            peak: {
                AM: [0, 2],
                PM: [10, 12],
                interval: 5
            },
            usually: {
                times: [10, 22],
                interval: 10
            }
        },
        //解析结果
        anlycol: undefined,

        //当前期号
        curterm: 0,
        /**
         * 定时器
         * @type {[type]}
         */
        interval: undefined,
        /**
         * 数据
         * @type {[type]}
         */
        openCodes: undefined,

        /**
         * [获取所有数据]
         * @param  {[type]} d [description]
         * @return {[type]}   [description]
         */
        getAllCodes: function (d) {
            var defer = $.Deferred();
            var inx = layer.load(1);
            $.post("cqall", {date: d}, function (data) {
                defer.resolve(data);
                layer.close(inx);
            }, 'json');
            return defer.promise();
        },

        /**
         * [同步数据请求]
         * @param  {[type]} limit [description]
         * @return {[type]}       [description]
         */
        getCode: function (term) {
            var defer = $.Deferred();
            $.post("cqhaoma", {
                "term": term
            }, function (data) {
                defer.resolve(data);
            }, 'json');
            return defer.promise();
        },
        /**
         * [更新提示]
         * @param {[type]} term [description]
         * @param {[type]} text [description]
         */
        setTipsPos: function (term, text) {
            if (term > 120) {
                term = 0;
                $("div[data-row] > .columns").empty().text('');
            }
            var target = $("div[data-inx='" + (term) + "']");
            var top = $(target).offset().top;
            var curColumn = Math.floor(term / 20);
            var tipstar = tipstar = $(".tips[data-inx='" + curColumn + "']");
            // if (term % 20 == 0) {
            //     $(tipstar).hide();
            //     tipstar = $(".tips[data-inx='" + curColumn + "']");
            // } else {
            var oldpx = parseFloat(tipstar.get(0).style.top.replace(/px/i, ''));
            if (oldpx > top) {
                top = oldpx;
            }
            // }
            if (text) {
                $(tipstar).html(text);
            }
            $(tipstar).show().animate({
                top: top
            });
        },

        /**
         * 全部数据加载完启动
         * @return {[type]} [description]
         */
        start: function () {
            var date = undefined,
                hour = 0,
                minutes = 0,
                seconds = 0,
                //剩余多少秒获取数据
                surplusSeconds = 0,
                //提示
                prompt = '',
                // 是否允许请求数据
                loopreq = false,
                cal = new Calendar();
            cq.interval = setInterval(function () {
                date = new Date();
                //2点后停止（23期是最后一期），早上10点开始
                hour = date.getHours();
                minutes = date.getMinutes();
                seconds = date.getSeconds();
                // ------------------ 每小时 58 -59 分钟时 会显示正在同步数据问题 -----------------
                if (cq.curterm == 23 && hour <= 9 && minutes <= 58) {
                    if (surplusSeconds <= 1) {
                        var startDate = new Date();
                        startDate.setHours(9);
                        startDate.setMinutes(58);
                        surplusSeconds = cal.dateDiff(startDate, date);
                    }
                    // 剩余更新秒数
                    cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--));
                } else {
                    // 开始同步数据
                    if (loopreq = (sessionStorage.getItem("loopreq") == "true" ? true : false)) {

                        // 2秒发送一次请求
                        if (seconds % 2 > 0) {
                            cq.setTipsPos(cq.curterm, '正在同步数据..');

                            cq.getCode(cq.curterm).done(function (data) {
                                if (data && !data.warning) {
                                    // 数据已同步，停止请求
                                    loopreq = false;
                                    sessionStorage.setItem("loopreq", false);
                                    cq.setTipsPos(cq.curterm + 1, null);
                                    var timeout = setTimeout(function () {
                                        cq.openCodes.push(data);
                                        cq.fill(data, cq.curterm, true);
                                        clearTimeout(timeout);
                                    }, 1200);
                                }
                            });
                        }
                    } else {
                        var stopTime = new Date();
                        var min = minutes.toString();
                        /**
                         * [0-2] > [10-24]
                         */
                        if ((hour > 22 && hour < 24) || (hour >= 0 && hour < 2)) {
                            if ((min.endsWith("4") || (min.endsWith("5") && seconds <= 51)) || (min.endsWith("9") || min.endsWith("0") && seconds <= 51)) {
                                if (surplusSeconds <= 1) {
                                    //剩余获取数据时间
                                    if (minutes >= 59) {
                                        //当前时间加一小时，下一小时0分52秒开始获取数据
                                        stopTime.setHours(hour + 1);
                                        stopTime.setMinutes(0);
                                        stopTime.setSeconds(50);
                                    } else {
                                        var firstMin = 0,
                                            lastMin = parseInt(min.charAt(min.length - 1));
                                        //说明当前分钟尾数是4或9
                                        if (lastMin % 5 != 0) {
                                            // 当前分钟尾数加1，下分钟50秒开始同步数据
                                            lastMin += 1;
                                            // 如果当前分钟是双位，则得到第一位
                                            if (min.length > 1) {
                                                firstMin = parseInt(min.charAt(0));
                                            }
                                            // 尾数 +1 等于10分钟向前 +1，尾数清 0
                                            if (lastMin == 10) {
                                                firstMin++;
                                                lastMin = 0;
                                            }
                                            //最终同步分钟数
                                            min = firstMin + "" + lastMin;
                                            stopTime.setMinutes(parseInt(min));
                                        }
                                        stopTime.setSeconds(51);
                                        // 剩余同步秒数
                                        surplusSeconds = surplusSeconds = cal.dateDiff(stopTime, date);
                                    }
                                }
                                cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--));
                                //计时完成同步数据
                                if (surplusSeconds <= 1) {
                                    loopreq = true;
                                    sessionStorage.setItem("loopreq", true);
                                }
                            } else {
                                //剩余投注时间
                                if (surplusSeconds <= 1) {
                                    var lastMin = parseInt(min.charAt(min.length - 1));
                                    // 每5分钟一期，投注时间小于4分钟或者9分钟
                                    if (lastMin < 4) {
                                        lastMin = parseInt(min.length == 1 ? 4 : (min.charAt(0) + "4"));
                                    } else {
                                        lastMin = parseInt(min.length == 1 ? 9 : (min.charAt(0) + "9"));
                                        ;
                                    }
                                    stopTime.setMinutes(lastMin);
                                    stopTime.setSeconds(0);
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }
                                cq.setTipsPos(cq.curterm, '剩余投注时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--));
                            }
                        } else {
                            if (min.toString().endsWith("9") || (min.toString().endsWith("0") && seconds <= 52)) {
                                // open codes surplus ..
                                if (surplusSeconds <= 1) {
                                    //剩余获取数据时间
                                    if (minutes >= 59) {
                                        if (hour > 23) {
                                            //当前日期加一天, 明天凌晨0分52秒获取数据
                                            stopTime = cal.getCustomDate(1);
                                            stopTime.setHours(0);
                                            stopTime.setMinutes(0);
                                            stopTime.setSeconds(52);
                                        } else {
                                            //当前时间加一小时，下一小时0分52秒开始获取数据
                                            stopTime.setHours(hour + 1);
                                            stopTime.setMinutes(0);
                                            stopTime.setSeconds(52);
                                        }
                                    } else {
                                        if (min.length == 1) {
                                            stopTime.setMinutes(10);
                                        } else if (min.length > 1 && !min.endsWith("0")) {
                                            var minFirstChar = parseInt(min.charAt(0));
                                            stopTime.setMinutes(parseInt((minFirstChar + 1)) + "0");
                                        }
                                        stopTime.setSeconds(52);
                                    }
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }
                                cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--));
                                //计时完成同步数据
                                if (surplusSeconds <= 1) {
                                    loopreq = true;
                                    sessionStorage.setItem("loopreq", true);
                                }
                            } else {
                                if (surplusSeconds <= 1) {
                                    if (min.length == 1) {
                                        min = 9;
                                    } else {
                                        min = parseInt(min.charAt(0) + "9");
                                    }
                                    stopTime.setMinutes(min);
                                    stopTime.setSeconds(0);
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }
                                cq.setTipsPos(cq.curterm, '剩余投注时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--));
                            }
                        }
                    }
                }
            }, 1000);
        },

        /**
         * 迭代数据
         * @param  {[type]} codes [description]
         * @return {[type]}       [description]
         */
        iteration: function (codes) {
            codes = codes.reverse();
            // 临时保存数据
            cq.openCodes = codes;
            var remTips = false;
            for (var i = 0; i < codes.length; i++) {
                if (i == codes.length - 1) {
                    remTips = true;
                }
                cq.fill(codes[i], i, remTips);
            }
        },

        /**
         * 填充数据
         * @param  {[type]} codeJson [数据]
         * @param  {[type]} i        [当前元素索引]
         * @param  {[type]} remTips  [是否移除前面列的提示面板]
         * @return {[type]}          [description]
         */
        fill: function (codeJson, i, remTips) {
            //指针移向下一期
            cq.curterm++;
            cq.curterm = cq.curterm > 120 ? 0 : cq.curterm;
            var col = $("[data-inx='" + i + "']").find(".columns");
            $(col[0]).text(codeJson.expect.substring(codeJson.expect.length - 3));
            var codes = codeJson.opencode.split(",");
            for (var j = 0; j < codes.length; j++) {
                $(col[1]).append('<span>' + codes[j] + '</span>');
            }
            // 隐藏当前列前面的提示列
            if (remTips) {
                var tarcol = Math.floor((cq.curterm) / 20);
                $(".tips").each(function (inx, item) {
                    if (parseInt($(item).data("inx")) < tarcol) {
                        $(item).hide();
                    }
                });
            }
        },

        /**
         * 分析数据
         * @param  {分析类型}
         * @param  {号码}
         * @return {[]}
         */
        analysis: function (kill, code) {

        }
    }


    /**
     * 获取数据
     * @param  {[type]}
     * @return {[type]}
     */
    cq.getAllCodes().done(function (codes) {
        var rows = $(".data-row");
        for (var i = 0; i < 120; i++) {
            $(rows[i]).attr("data-inx", i);
        }
        cq.iteration(codes);
        if (codes.length < 120) {
            cq.start();
        }
    });

    /**
     * 获取某日数据
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    $("#header a.swdate").click(function (event) {
        /* Act on the event */
        if (!$(this).hasClass('active')) {
            $(".data-row > .columns").empty();
            $(".tips").hide();
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
            cq.curterm = 0;
            clearInterval(cq.interval);
            cq.getAllCodes(date).done(function (codes) {
                cq.iteration(codes);
                if (codes.length < 120) {
                    cq.start();
                }
            });
        }
    });

    /**
     * 切换开奖数据分析
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    $("#header a.killtype").click(function (event) {
        /* Act on the event */
        var target = $("." + $(this).data("for") + "");
        $(target).text($(this).text()).addClass('trigger');
        $(this).parent().find('a').removeClass('active');
        $(this).addClass('active');
        setTimeout(function () {
            $(target).removeClass('trigger');
        }, 800);
    });

    $(".cus-date").click(function (event) {
        /* Act on the event */
        $(".datepicker-here").trigger('focus');
    });

    /**
     * 自选
     */
    $('#cusdatebtn').datepicker({
        autoClose: true,
        maxDate: new Date(), // Now can select only dates, which goes after today
        onSelect: function (formattedDate, date, inst) {
            $(".cus-date").text(formattedDate).parent().find('a').removeClass('active');
            $(".cus-date").addClass('active');
            $(".data-row > .columns").empty();
            $(".tips").hide();
            sessionStorage.removeItem("codes");
            cq.getAllCodes(formattedDate).done(function (codes) {
                cq.iteration(codes);
                if (codes.length < 120) {
                    cq.start();
                }
            });
        }
    });


});
