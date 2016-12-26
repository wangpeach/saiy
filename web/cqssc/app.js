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

    cqssc.getAll = function(d) {
        var defer = $.Deferred();
        codes = cqssc.session();
        if (!codes) {
            $.post("cqall", { date: d }, function(data) {
                if (!cqssc.session()) {
                    sessionStorage.setItem("codes", JSON.stringify(data.data));
                }
                defer.resolve(data.data);
            }, 'json');
        } else {
            defer.resolve(codes);
        }
        return defer.promise();
    };

    cqssc.gethaoma = function(limit) {
        var defer = $.Deferred();
        $.post("cqhaoma", {
            "limit": limit
        }, function(data) {
            cqssc.session(data.data);
            defer.resolve(data);
        }, 'json');
        return defer.promise();
    };

    cqssc.start = function() {
        var date = new Date();
        var minutes = 0,
            seconds = 0;
        var interval = setInterval(function() {
            if (cqssc.config.curterm == 19) {
                clearInterval(interval);
                // 8小时
            }
            minutes = date.getMinutes(), seconds = date.getSeconds();

            if (minutes.toString().endsWith("1") && seconds % 5 == 0) {
                cqssc.gethaoma().done(function(data) {
                    //surplus throw into ..

                });
            }

            if (minutes.toString().endsWith("8")) {
                // open codes surplus ..
            }

        }, 1000)
    };

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
    cqssc.getAll().done(function(data) {
        var datas = data.reverse();
        var rows = $(".data-row");
        for (var i = 0; i < 120; i++) {
            $(rows[i]).attr("data-inx", i);
        }
        for (var i = 0; i < datas.length; i++) {
            cqssc.fill(datas[i], i);
        }
    });

    /**
     * 切换开奖结果分析
     * @param  {[type]}
     * @param  {[type]}
     * @return {[type]}
     */
    $("#header a.swdate").click(function(event) {
        /* Act on the event */
        $(".data-row > .columns").empty();
        sessionStorage.removeItem("codes");
        if($(this).hasClass("active")) return false;
        $(this).parent().find('a').removeClass('active');
        $(this).addClass('active');
        $(".cus-date").text("自选");
        var date = "", cal = new Calendar();
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
        cqssc.getAll(date).done(function(data) {
            var datas = data.reverse();
            for (var i = 0; i < datas.length; i++) {
                cqssc.fill(datas[i], i);
            }
        });
    });

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
        }
    })
}());
