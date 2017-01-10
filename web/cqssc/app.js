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

    cqssc.session = function(item) {
        var codes = $.parseJSON(sessionStorage.getItem("codes"));
        if (item) {
            codes.push(item);
        }
        return codes;
    }

    /**
     * [获取所有数据]
     * @param  {[type]} d [description]
     * @return {[type]}   [description]
     */
    cqssc.getAllCodes = function(d) {
        var defer = $.Deferred();
        codes = cqssc.session();
        if (!codes) {
            var inx = layer.load(1);
            $.post("cqall", { date: d }, function(data) {
                if (!cqssc.session()) {
                    sessionStorage.setItem("codes", JSON.stringify(data));
                }
                defer.resolve(data);
                layer.close(inx);
            }, 'json');
        } else {
            defer.resolve(codes);
        }
        return defer.promise();
    };

    /**
     * [getCode description]
     * @param  {[type]} limit [description]
     * @return {[type]}       [description]
     */
    cqssc.getCode = function(limit) {
        var defer = $.Deferred();
        $.post("cqhaoma", {
            "limit": limit
        }, function(data) {
            cqssc.session(data);
            defer.resolve(data);
        }, 'json');
        return defer.promise();
    };

    /**
     * 全部数据加载完启动
     * @return {[type]} [description]
     */
    cqssc.start = function() {
        var date = new Date();
        var hour = 0,
            minutes = 0,
            seconds = 0,
            //剩余多少秒获取数据
            surplusSeconds = 0,
            //提示
            prompt = '',
            cal = new Calendar();
        var interval = setInterval(function() {
            //2点后停止（23期是最后一期），早上10点开始
            hour = date.getHours();
            minutes = date.getMinutes();
            seconds = date.getSeconds();
            if (cqssc.config.curterm == 24 && hour <= 9 && minutes <= 58) {
                var cur = new Date();
                var startDate = new Date();
                startDate.setHours(9);
                startDate.setMinutes(58);
                seconds = cal.dateDiff(startDate, cur);
                // 剩余更新秒数
                //
            } else {
                // 凌晨0点到2点 每5分钟请求一次，10点到 24点 每 10.50分钟请求一次
                if (hour > 0 && hour < 2) {

                } else {
                    if (minutes.toString().endsWith("0") && seconds > 50 && seconds % 2 == 0) {
                        prompt = "正在更新数据.."
                        cqssc.getCode().done(function(data) {
                            //surplus throw into ..
                            if (data) {
                                cqssc.fill(data, cqssc.config.curterm);
                            }
                        });
                    }
                    if (minutes.toString().endsWith("8") || (minutes.toString().endsWith("0") && seconds )) {
                        // open codes surplus ..
                        if(surplusSeconds == 0) {
                            surplusSeconds = 150;
                        }
                        prompt = "剩余更新数据" + cal.formatSeconds(surplusSeconds--);
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
        for (var i = 0; i < codes.length; i++) {
            cqssc.fill(codes[i], i);
        }
    }

    /**
     * 填充数据
     * @param  {[type]} codeJson [description]
     * @param  {[type]} i        [description]
     * @return {[type]}          [description]
     */
    cqssc.fill = function(codeJson, i) {
        //指针移向下一期
        cqssc.config.curterm++;
        var col = $("[data-inx='" + i + "']").find(".columns");
        $(col[0]).text(codeJson.expect.substring(codeJson.expect.length - 3));
        var codes = codeJson.opencode.split(",");
        for (var j = 0; j < codes.length; j++) {
            $(col[1]).append('<span>' + codes[j] + '</span>');
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

    // cqssc.start();

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
