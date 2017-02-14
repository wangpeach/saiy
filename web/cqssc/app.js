$(document).foundation();

jQuery(document).ready(function($) {
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
            },
            notification: false
        },
        /**
         * 解析结果
         */
        anlycol: {
            Q3: { zu6: 0, zu3: 0, baozi: 0 },
            Z3: { zu6: 0, zu3: 0, baozi: 0 },
            H3: { zu6: 0, zu3: 0, baozi: 0 },
            Q2: { duizi: 0, shun: 0 },
            H2: { duizi: 0, shun: 0 },
            '__*': { count: 0, _property_1: 0, _property_2: 0 },
            __0: { count: 0, _property_1: 0, _property_2: 0 },
            __1: { count: 0, _property_1: 0, _property_2: 0 },
            __2: { count: 0, _property_1: 0, _property_2: 0 },
            __3: { count: 0, _property_1: 0, _property_2: 0 },
            __4: { count: 0, _property_1: 0, _property_2: 0 },
            __5: { count: 0, _property_1: 0, _property_2: 0 },
            __6: { count: 0, _property_1: 0, _property_2: 0 },
            __7: { count: 0, _property_1: 0, _property_2: 0 },
            __8: { count: 0, _property_1: 0, _property_2: 0 },
            __9: { count: 0, _property_1: 0, _property_2: 0 },
            _0wqbsg_: {
                '_*': { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _0: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _1: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _2: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _3: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _4: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _5: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _6: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _7: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _8: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _9: { curmiss: 0, maxmiss: 0, lasterm: 0 }
            },
            _1wqbsg_: {
                '_*': { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _0: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _1: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _2: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _3: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _4: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _5: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _6: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _7: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _8: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _9: { curmiss: 0, maxmiss: 0, lasterm: 0 }
            },
            _2wqbsg_: {
                '_*': { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _0: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _1: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _2: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _3: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _4: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _5: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _6: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _7: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _8: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _9: { curmiss: 0, maxmiss: 0, lasterm: 0 }
            },
            _3wqbsg_: {
                '_*': { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _0: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _1: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _2: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _3: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _4: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _5: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _6: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _7: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _8: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _9: { curmiss: 0, maxmiss: 0, lasterm: 0 }
            },
            _4wqbsg_: {
                '_*': { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _0: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _1: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _2: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _3: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _4: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _5: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _6: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _7: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _8: { curmiss: 0, maxmiss: 0, lasterm: 0 },
                _9: { curmiss: 0, maxmiss: 0, lasterm: 0 }
            }
        },

        /**
         * 当前期号，标记号码执行到哪一期，只用于今天
         */
        curterm: 0,
        /**
         * 当前期号，标记号码执行到哪一期, 只用于今天之前
         * @type {Number}
         */
        beforeterm: 0,

        /**
         * 定时器
         * @type {[type]}
         */
        interval: undefined,
        /**
         * 与服务器时间相差多少秒
         * @type {Number}
         */
        syntime: 0,
        /**
         * 计时位置追踪
         * @type {Boolean}
         */
        timerTrace: true,
        /**
         * 数据
         * @type {[type]}
         */
        openCodes: undefined,
        /**
         * 提示音对象
         * @type {[type]}
         */
        notiMedia: undefined,
        /**
         * 是否已经同步数据，
         * 数据同步成功后 20秒之内设置为ture ，不会再同步数据
         * 因为系统设置同步时间为 开奖时间范围50秒，有时开奖会在50秒之前，
         * 可能是由于数据在同一时间返回造成一直显示正在同步数据，
         * 因此添加此属性，在数据同步完成后设置为 true， 20秒之后再次设置为 false
         */
        syned: false,

        lockLittle: function() {
            cq.syned = true;
            setTimeout(function() {
                cq.syned = false;
            }, 10000);
        },

        /**
         * 最后一次点击的分析按钮
         * 最后一次分析结果对应的列
         * 程序自动控制
         */
        lastAnlAction: undefined,
        lastAnlRank: undefined,
        /**
         * 当前分析的类型
         */
        curAnlAction: ['H3', 'H2'],

        /**
         * [获取所有数据]
         * @param  {[type]} d [description]
         * @return {[type]}   [description]
         */
        getAllCodes: function(d) {
            if (!d) {
                d = new Date().Format("yyyy-MM-dd");
            }
            var defer = $.Deferred();
            var inx = layer.load(2);
            $.post("cqall", { date: d, clientTime: new Date().Format("yyyy-MM-dd hh:mm:ss")}, function(data) {
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
        getCode: function(term) {
            var defer = $.Deferred();
            $.post("cqhaoma", {
                "term": term
            }, function(data) {
                defer.resolve(data);
            }, 'json');
            return defer.promise();
        },
        /**
         * [更新提示]
         * @param {[type]} term [description]
         * @param {[type]} text [description]
         */
        setTipsPos: function(term, text, css) {
            if (cq.timerTrace) {
                var target = $("div[data-inx='" + (term) + "']");
                var top = $(target).offset().top;
                var curColumn = Math.floor(term / 20);
                var tipstar = $(".tips[data-inx='" + curColumn + "']").css(css);
                var oldpx = parseFloat(tipstar.get(0).style.top.replace(/px/i, ''));
                if (oldpx > top) {
                    top = oldpx;
                }
                $(tipstar).show().css({ top: top });
                if (text) {
                    $(tipstar).html(text);
                }
                $("#timer").empty();
            } else {
                $("#timer").html(text).css(css);
            }
        },

        /**
         * 全部数据加载完启动
         * @return {[type]} [description]
         */
        start: function() {
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
                cal = new Calendar(),
                stopTime = new Date();
            cq.interval = setInterval(function() {
                date = new Date();
                date.
                //2点后停止（23期是最后一期），早上10点开始
                hour = date.getHours();
                minutes = date.getMinutes();
                seconds = date.getSeconds();
                // ------------------ 每小时 58 -59 分钟时 会显示正在同步数据问题 -----------------
                if (cq.curterm == 23 && hour < 10) {
                    if (surplusSeconds <= 1) {
                        stopTime.setHours(10);
                        stopTime.setMinutes(0);
                        stopTime.setSeconds(46);
                        surplusSeconds = cal.dateDiff(stopTime, date);
                    }
                    // 剩余更新秒数
                    cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--), { 'color': '#f183d3' });
                    //计时完成同步数据
                    if (surplusSeconds <= 1) {
                        loopreq = true;
                        sessionStorage.setItem("loopreq", true);
                    }
                } else {
                    // 开始同步数据
                    if (loopreq = (sessionStorage.getItem("loopreq") == "true" ? true : false)) {
                        // 2秒发送一次请求
                        if (seconds % 2 > 0) {
                            cq.setTipsPos(cq.curterm, '正在同步数据..', { 'color': '#f183d3' });

                            cq.getCode(cq.curterm).done(function(data) {
                                if (data && !data.warning) {
                                    // 数据已同步，停止请求
                                    loopreq = false;
                                    //加锁20秒
                                    cq.lockLittle();
                                    sessionStorage.setItem("loopreq", false);
                                    var nextTerm = cq.curterm + 1,
                                        wating = 300;

                                    if (cq.config.notification) {
                                        if (!cq.notiMedia) {
                                            cq.notiMedia = document.getElementById("audio");
                                        }
                                        cq.notiMedia.play();
                                        var noti = new Notification('巅峰数据: 数据已同步', { body: "期号：" + nextTerm + ", 号码：" + data.opencode, icon: 'cqssc/shiicon.ico' });
                                        noti.onshow = function() {
                                            setTimeout(noti.close.bind(noti), 8000);
                                        }
                                    }

                                    //今天最后一期，数据同步后3秒刷新页面
                                    if (nextTerm == 120) {
                                        wating = 0;
                                        clearInterval(cq.interval);
                                        layer.msg("6秒后自动刷新");
                                        $('.tips').hide();
                                        var renovate = setTimeout(function() {
                                            window.location.reload();
                                        }, 6000);
                                    } else {
                                        cq.setTipsPos(nextTerm, null, { 'color': '#f183d3' });
                                    }

                                    var timeout = setTimeout(function() {
                                        cq.openCodes.push(data);
                                        if (cq.timerTrace) {
                                            cq.fill(data, cq.curterm, true, true, true);
                                        }
                                        clearTimeout(timeout);
                                    }, wating);
                                }
                            });
                        }
                    } else {
                        var min = minutes.toString();
                        /**
                         * [0-2] > [10-24]
                         */
                        if ((hour >= 22 && hour <= 23) || (hour >= 0 && hour < 2)) {
                            if (((min.endsWith("4") || (min.endsWith("5") && seconds <= 50)) && !cq.syned) || ((min.endsWith("9") || min.endsWith("0") && seconds <= 50) && !cq.syned)) {
                                if (surplusSeconds <= 10) {
                                    //剩余获取数据时间
                                    if (minutes >= 55) {
                                        //当前时间加一小时，下一小时0分52秒开始获取数据
                                        stopTime.setHours(hour + 1);
                                        if (stopTime.getHours() > 23) {
                                            //当前日期加一天, 明天凌晨0分50秒获取数据
                                            stopTime = cal.getCustomDate(1);
                                            stopTime.setHours(0);
                                        }
                                        stopTime.setMinutes(0);
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
                                    }
                                    stopTime.setSeconds(46);

                                    console.log(stopTime.getFullYear() + "-" + stopTime.getMonth() + "-" + stopTime.getDay() + " " + stopTime.getHours() + ":" + stopTime.getMinutes() + ":" + stopTime.getSeconds());
                                    // 剩余同步秒数
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }
                                cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--), { 'color': '#f183d3' });
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
                                    }
                                    stopTime.setMinutes(lastMin);
                                    stopTime.setSeconds(0);
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }

                                console.log(stopTime.getFullYear() + "-" + stopTime.getMonth() + "-" + stopTime.getDay() + " " + stopTime.getHours() + ":" + stopTime.getMinutes() + ":" + stopTime.getSeconds());
                                if (surplusSeconds > 240) {
                                    surplusSeconds = 0;
                                    loopreq = true;
                                    sessionStorage.setItem("loopreq", true);
                                } else {
                                    var formatdate = cal.formatSeconds(surplusSeconds--);
                                    cq.setTipsPos(cq.curterm, '剩余投注时间&nbsp;&nbsp;' + formatdate, { 'color': '#ffefa0' });
                                }
                            }
                        } else {
                            if ((min.toString().endsWith("9") && !cq.syned) || ((min.toString().endsWith("0") && seconds <= 50) && !cq.syned)) {
                                // open codes surplus ..
                                if (surplusSeconds <= 10) {
                                    //剩余获取数据时间
                                    if (minutes >= 59) {
                                        if (hour > 23) {
                                            //当前日期加一天, 明天凌晨0分50秒获取数据
                                            stopTime = cal.getCustomDate(1);
                                            stopTime.setHours(0);
                                        } else {
                                            //当前时间加一小时，下一小时0分50秒开始获取数据
                                            stopTime.setHours(hour + 1);
                                        }
                                        stopTime.setMinutes(0);
                                    } else {
                                        if (min.length == 1) {
                                            stopTime.setMinutes(10);
                                        } else if (min.length > 1 && !min.endsWith("0")) {
                                            var minFirstChar = parseInt(min.charAt(0));
                                            stopTime.setMinutes(parseInt((minFirstChar + 1)) + "0");
                                        }
                                    }
                                    stopTime.setSeconds(46);
                                    surplusSeconds = cal.dateDiff(stopTime, date);
                                }

                                console.log(stopTime.getFullYear() + "-" + stopTime.getMonth() + "-" + stopTime.getDay() + " " + stopTime.getHours() + ":" + stopTime.getMinutes() + ":" + stopTime.getSeconds());
                                cq.setTipsPos(cq.curterm, '剩余开奖时间&nbsp;&nbsp;' + cal.formatSeconds(surplusSeconds--), { 'color': '#f183d3' });
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

                                console.log(stopTime.getFullYear() + "-" + stopTime.getMonth() + "-" + stopTime.getDay() + " " + stopTime.getHours() + ":" + stopTime.getMinutes() + ":" + stopTime.getSeconds());
                                if (surplusSeconds > 540) {
                                    surplusSeconds = 0;
                                    loopreq = true;
                                    sessionStorage.setItem("loopreq", true);
                                } else {
                                    var formatdate = cal.formatSeconds(surplusSeconds--);
                                    cq.setTipsPos(cq.curterm, '剩余投注时间&nbsp;&nbsp;' + formatdate, { 'color': '#ffefa0' });
                                }
                            }
                        }
                    }
                }
            }, 1000);
        },

        /**
         * 迭代数据
         * @param  {[type]} codes [description]
         * @param  {[type]} istoy [是否是今天]
         * @return {[type]}       [description]
         */
        iteration: function(codes, istoy) {
            //初始化记录
            cq.beforeterm = 0;
            cq.curterm = 0;
            for (item in cq.anlycol) {
                if (cq.anlycol.hasOwnProperty(item)) {
                    if (item.startsWith("__")) {
                        cq.anlycol[item].count = 0;
                    } else if (item.endsWith("2")) {
                        cq.anlycol[item].duizi = 0,
                        cq.anlycol[item].shun = 0
                    } else if(item.endsWith("wqbsg_")) {
                        cq.anlycol[item]["_*"].curmiss = 0;
                        cq.anlycol[item]["_*"].maxmiss = 0;
                        cq.anlycol[item]["_*"].lasterm = 0;
                        for (var i = 0; i < 10; i++) {
                            cq.anlycol[item]["_" + i].curmiss = 0;
                            cq.anlycol[item]["_" + i].maxmiss = 0;
                            cq.anlycol[item]["_" + i].lasterm = 0;
                        }
                    } else {
                        cq.anlycol[item].zu6 = 0;
                        cq.anlycol[item].zu3 = 0;
                        cq.anlycol[item].baozi = 0;
                    }
                }
            }

            if (codes && codes.length > 0) {
                codes = codes.reverse();
                // 临时保存数据
                cq.openCodes = codes;
                var remTips = false;
                for (var i = 0; i < codes.length; i++) {
                    if (i == codes.length - 1) {
                        remTips = true;
                    }
                    cq.fill(codes[i], i, remTips, false, istoy);
                }
                // cq.cencus(cq.openCodes);
            }

            cq.refPanel();
        },

        /**
         * 填充数据
         * @param  {[type]} codeJson [数据]
         * @param  {[type]} i        [当前元素索引]
         * @param  {[type]} remTips  [是否移除前面列的提示面板]
         * @param  {[type]} [_refpanel] [是否刷新各形态统计]
         * @param  {[type]} [istoy] [是否是今天]
         * @return {[type]}          [description]
         */
        fill: function(codeJson, i, remTips, _refpanel, istoy) {
            //指针移向下一期
            if (cq.timerTrace || istoy) {
                cq.curterm++;
                cq.curterm = cq.curterm > 120 ? 0 : cq.curterm;
            } else {
                cq.beforeterm++;
            }

            var col = $("[data-inx='" + i + "']").find(".columns");
            $(col[0]).text(codeJson.expect.substring(codeJson.expect.length - 3));
            if (codeJson.opencode) {
                var codes = codeJson.opencode.split(",");
                for (var j = 0; j < codes.length; j++) {
                    $(col[1]).append('<span data-val="' + codes[j] + '">' + codes[j] + '</span>');

                    /**
                     * 统计 每个数字出现次数
                     */
                    cq.anlycol["__" + [codes[j]]].count++;

                    /**
                     * 计算遗漏
                     * @type {[type]}
                     */
                    var wqbsg = cq.anlycol["_" + j + "wqbsg_"];
                    for (prop in wqbsg) {
                        if (wqbsg.hasOwnProperty(prop)) {
                            if (prop.endsWith([codes[j]])) {
                                wqbsg[prop].curmiss = 0;
                                wqbsg[prop].lasterm = cq.timerTrace ? cq.curterm : cq.beforeterm;
                            } else {
                                wqbsg[prop].curmiss++;
                                if (wqbsg[prop].curmiss > wqbsg[prop].maxmiss) {
                                    wqbsg[prop].maxmiss = wqbsg[prop].curmiss;
                                }
                                cq.anlycol["_" + j + "wqbsg_"][prop] = wqbsg[prop];
                            }
                        }
                    }

                }

                // 分析已选中形态
                var An1Res = cq.analysis(cq.curAnlAction[0], codes.join(""));
                if (An1Res) {
                    $(col[2]).css({ color: An1Res.color }).text(An1Res.text);
                }
                var An2Res = cq.analysis(cq.curAnlAction[1], codes.join(""));
                if (An2Res) {
                    $(col[3]).css({ color: An2Res.color }).text(An2Res.text);
                }

                // 统计各形态出现次数
                cq.cencus(codeJson);

                //更新统计面板
                if (_refpanel) {
                    cq.refPanel();
                }

                // 隐藏当前列前面的提示列
                if (remTips) {
                    var tarcol = Math.floor((cq.curterm) / 20);
                    $(".tips").each(function(inx, item) {
                        if (parseInt($(item).data("inx")) < tarcol) {
                            $(item).hide();
                        }
                    });
                }
            }
        },

        /**
         * 统计各形态共出现次数
         * @return {[type]} [description]
         */
        cencus: function(code) {
            var killtypes = ["Q3", "Z3", "H3", "Q2", "H2"];
            for (var i = 0; i < killtypes.length; i++) {
                var result = cq.analysis(killtypes[i], code.opencode.split(",").join(""));
                if (result.type) {
                    cq.anlycol[killtypes[i]][result.type]++;
                }
            }
        },

        /**
         * 更新统计面板
         * @return {[type]} [description]
         */
        refPanel: function() {
            /**
             * BEGIN
             * 每个数字出现次数，和进度条比例
             */
            var codearise = new Array(),
                pushedArise = "";
            for (var i = 0; i < 10; i++) {
                codearise.push(cq.anlycol['__' + i].count);
            }
            codearise = codearise.sort(function(a, b) {
                return a - b;
            }).reverse();

            for (var i = 0; i < 10; i++) {
                // 每个数字出现顺序
                for (var j = 0; j < 10; j++) {
                    if (codearise[i] == cq.anlycol['__' + j].count && !pushedArise.includes(j)) {
                        pushedArise += j;
                    }
                }
                //进度条显示
                var ratio = (codearise[i] / codearise[0]) * 100;
                $("._" + i + "progress").attr({
                    'aria-valuenow': ratio
                }).find('.progress-meter').css('width', ratio + '%');
                $("._" + i + "arise").text(codearise[i]);
            }
            //填充 数字
            codearise = pushedArise.split("");
            for (var i = 0; i < codearise.length; i++) {
                $("._" + i + "coderow").data('val', codearise[i])
                $("._" + i + "code").html(codearise[i] + "&nbsp;&nbsp;");
            }
            /**
             * END
             */



            /**
             * BEGIN
             * 更新各形态出现次数
             */
            for (var prop in cq.anlycol) {
                if (cq.anlycol.hasOwnProperty(prop) && !prop.startsWith('_')) {
                    for (var item in cq.anlycol[prop]) {
                        if (cq.anlycol[prop].hasOwnProperty(item)) {
                            $("." + prop + item).text(cq.anlycol[prop][item]);
                        }
                    }
                }
            }
            /**
             * END
             */


            /**
             * BEGIN
             * 更新遗漏
             */
            for (var i = 0; i < 5; i++) {
                var _wqbsg = cq.anlycol["_" + i + "wqbsg_"];
                for (var j = 0; j < 10; j++) {
                    var messcode = _wqbsg["_" + j];
                    var targetParent = $("#_missing_" + i + ">.columns:eq(" + (j + 1) + ")");

                    $(targetParent).find('.has-tip').attr('title', ("最后出现在" + messcode.lasterm + "期, 当前遗漏 " + messcode.curmiss + "期, 最大遗漏 " + messcode.maxmiss + "期"));

                    $(targetParent).find('.curMissing').text(messcode.curmiss);
                    $(targetParent).find('.maxMissing').text(messcode.maxmiss);
                }
            }
            /**
             * END
             */

            console.log(cq.anlycol);
        },

        /**
         * 切换数据分析
         * @param  {[type]}
         * @param  {[type]}
         * @return {[type]}
         */
        swipAnaly: function(kill, _an) {
            for (var i = 0; i < cq.openCodes.length; i++) {
                var code = cq.openCodes[i];
                if (code.opencode) {
                    var anres = cq.analysis(kill, code.opencode.split(",").join(""));
                    var col = $("[data-inx='" + i + "']").find(".columns");
                    if (anres) {
                        $(_an == "AN1" ? col[2] : col[3]).removeAttr('style').text('').css({ color: anres.color }).text(anres.text);
                    }
                }
            }
        },

        /**
         * 分析数据
         * @param  {分析类型}
         * @param  {号码}
         * @return {[]}
         */
        analysis: function(kill, code) {
            var result = undefined;
            switch (kill) {
                case "Q3":
                    result = cq._3Analy(code.substr(0, 3).split(""));
                    break;
                case "Z3":
                    result = cq._3Analy(code.substr(1, 3).split(""));
                    break;
                case "H3":
                    result = cq._3Analy(code.substr(2, 3).split(""));
                    break;
                case "Q2":
                    result = cq._2Analy(code.substr(0, 2).split(""));
                    break;
                case "H2":
                    result = cq._2Analy(code.substr(3, 2).split(""));
                    break;
                case "W":
                    result = cq._1Analy(code.substr(0, 1));
                    break;
                case "Q":
                    result = cq._1Analy(code.substr(1, 1));
                    break;
                case "B":
                    result = cq._1Analy(code.substr(2, 1));
                    break;
                case "S":
                    result = cq._1Analy(code.substr(3, 1));
                    break;
                case "G":
                    result = cq._1Analy(code.substr(4, 1));
                    break;
            }
            return result;
        },
        /**
         * 分析组六
         * @param  {[type]}
         * @return {[type]}
         */
        _3Analy: function(code) {
            var result = {},
                maxcter = 0;
            for (var i = 0; i < code.length; i++) {
                var counter = 0;
                for (var j = 0; j < code.length; j++) {
                    if (i != j) {
                        if (code[i] == code[j]) {
                            counter++;
                            if (counter > maxcter) {
                                maxcter = counter;
                            }
                        }
                    }
                }
                if (maxcter == 1) {
                    result.type = "zu3";
                    result.text = "组三";
                    result.color = "darkorange";
                } else if (maxcter == 2) {
                    result.type = "baozi";
                    result.text = "豹子";
                    result.color = "fuchsia";
                } else {
                    result.type = "zu6";
                    result.text = "组六";
                    result.color = 'chartreuse';
                }
            }
            return result;
        },
        /**
         * 分析前后二
         * @param  {[type]}
         * @return {[type]}
         */
        _2Analy: function(code) {
            var result = {};
            if (code[0] == code[1]) {
                result.type = "duizi";
                result.text = "对子";
                result.color = "hotpink";
            } else {
                var _positive_1_9_ = Math.abs(code[0] - code[1]) || Math.abs(code[1] - code[0]);
                if (_positive_1_9_ == 1 || _positive_1_9_ == 9) {
                    result.type = "shun";
                    result.text = "连号";
                    result.color = "burlywood";
                }
            }
            return result;
        },
        /**
         * 定位分析
         * @param  {[type]}
         * @return {[type]}
         */
        _1Analy: function(code) {
            var result = {}
            code = parseInt(code);
            result.text = (code < 5 ? '小' : '大');
            if (code % 2 == 0) {
                result.color = 'darksalmon';
                result.text += "双"
            } else {
                result.color = 'palegreen';
                result.text += "单"
            }
            return result;
        }
    }



    /**
     * 获取数据
     * @param  {[type]}
     * @return {[type]}
     */
    cq.getAllCodes().done(function(arg) {
        var rows = $(".data-row");
        for (var i = 0; i < 120; i++) {
            $(rows[i]).attr("data-inx", i);
        }

        if (arg.msg) {
            layer.alert(arg.msg);
        } else {
            var seconds = parseInt(arg.syntime), rdnsec = 0;
            if(seconds <= 0 || seconds > 5) {
                rdnsec = Math.random()*5;
            }
            if(seconds > 0) {
                seconds -= rdnsec;
            } else {
                seconds = Math.abs(seconds) + rdnsec;
            }
            cq.syntime = seconds;

            cq.iteration(arg.codes, true);
            if (arg.codes && arg.codes.length < 120) {
                cq.start();
            }
        }

        // 获取近7日开奖号码形态统计
        $.post('cqcencusLast7', {}, function(data, textStatus, xhr) {
            /*optional stuff to do after success */
            for (var i = 0; i < data.length; i++) {
                var target = $(".curcencus .cenlast" + i);
                $(target).find('.calendar').text(data[i].date);
                $(target).find('.zu6').text(data[i].zu6);
                $(target).find('.zu3').text(data[i].zu3);
                $(target).find('.baozi').text(data[i].baozi);
                $(target).find('.duizi').text(data[i].duizi);
                $(target).find('.shun').text(data[i].shun);
            }
        }, 'json');
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
            $(".opened .data-row > .columns").empty();
            $(".tips").hide();
            sessionStorage.removeItem("codes");

            if ($(this).hasClass("active")) return false;
            $(this).parent().find('a').removeClass('active');
            $(this).addClass('active');
            $(".cus-date").text("自选");

            //那天遗漏提示
            $(".missingTitle").text($(this).text());

            var date = "",
                istoy = false,
                cal = new Calendar();
            cq.timerTrace = false;
            switch ($(this).data("val")) {
                case 'tod':
                    cq.curterm = 0;
                    cq.timerTrace = true;
                    istoy = true;
                    date = new Date().Format("yyyy-MM-dd");
                    break;
                case 'ysd':
                    date = cal.getCustomDate(-1);
                    break;
                case 'bysd':
                    date = cal.getCustomDate(-2);
                    break;
            }
            // clearInterval(cq.interval);

            cq.getAllCodes(date).done(function(arg) {
                if (arg.msg) {
                    layer.alert(arg.msg);
                } else {
                    cq.iteration(arg.codes, istoy);
                    if (arg.codes && arg.codes.length > 0 && arg.codes.length < 120 && !cq.interval) {
                        cq.start();
                    }
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
    $("#header a.killtype").click(function(event) {
        /* Act on the event */
        if ($(this).hasClass('active')) {
            return false;
        }

        var actives = $("#header a.killtype.active");

        //删除分析结果显示列选中状态
        if ($("." + cq.lastAnlRank + "").hasClass('active')) {
            var letcol = $("." + cq.lastAnlRank + ":first").text();
            $(actives).each(function(index, el) {
                if ($(el).text() != letcol) {
                    cq.lastAnlAction = el;
                }
            });
            $("." + cq.lastAnlRank + "").removeClass('active');
            cq.lastAnlRank = (cq.lastAnlRank == "AN1" ? "AN2" : "AN1");
        }

        if (cq.lastAnlAction) {
            $(actives).each(function(index, el) {
                if (cq.lastAnlAction != el) {
                    cq.lastAnlAction = el;
                    return false;
                }
            });
            cq.lastAnlRank = (cq.lastAnlRank == "AN1" ? "AN2" : "AN1");
        } else {
            cq.lastAnlAction = $(actives).first();
            if (!cq.lastAnlRank) {
                cq.lastAnlRank = "AN1";
            }
        }

        $(cq.lastAnlAction).removeClass('active');
        $(this).addClass('active');
        cq.lastAnlAction = this;

        cq.curAnlAction[(cq.lastAnlRank == "AN1" ? 0 : 1)] = $(this).data("val");

        /**
         * 点击相应分析按钮 重新分析数据
         */
        cq.swipAnaly($(this).data("val"), cq.lastAnlRank);

        var trigger = $("." + cq.lastAnlRank + "");
        $(trigger).addClass("trigger").text($(this).text());
        setTimeout(function() {
            $(trigger).removeClass('trigger');
        }, 800);
    });

    /**
     * 自选分析结果显示列
     * @param  {[type]}
     * @return {[type]}
     */
    $(".AN1,.AN2").click(function() {
        var an = $(this).hasClass("AN1") ? 'AN1' : 'AN2';
        cq.lastAnlRank = an;
        var reverse = (an == 'AN1' ? 'AN2' : 'AN1');
        $("." + reverse + "").removeClass('active');
        $("." + an + "").addClass('active');
    });

    $(".cus-date").click(function(event) {
        /* Act on the event */
        $(".datepicker-here").trigger('focus');
    });

    /**
     * 自选
     */
    $('#cusdatebtn').datepicker({
        autoClose: true,
        maxDate: new Date(), // Now can select only dates, which goes after today
        onSelect: function(formattedDate, date, inst) {
            $(".cus-date").text(formattedDate).parent().find('a').removeClass('active');
            $(".cus-date").addClass('active');
            $(".opened .data-row > .columns").empty();
            $(".tips").hide();
            sessionStorage.removeItem("codes");

            // clearInterval(cq.interval);
            var istoy = true;
            if (formattedDate != new Date().Format("yyyy-MM-dd")) {
                cq.curterm = 0;
                cq.timerTrace = false;
                istoy = false;
            }
            $(".missingTitle").text(formattedDate + " ");
            cq.getAllCodes(formattedDate).done(function(arg) {
                if (arg.msg) {
                    layer.alert(arg.msg);
                } else {
                    cq.iteration(arg.codes, istoy);
                    if (arg.codes.length < 120 && !cq.interval) {
                        cq.start();
                    }
                }
            });
        }
    });


    /**
     * [是否接受通知]
     */
    $("#usenotification").change(function(event) {
        /* Act on the event */
        var msg = undefined;

        if (this.checked) {
            msg = '已开启通知!';
        } else {
            msg = '已关闭通知!';
        }

        var notifun = function() {
            if (!cq.notiMedia) {
                cq.notiMedia = document.getElementById("audio");
            }
            cq.notiMedia.play();

            var noti = new Notification('巅峰数据', { body: msg, icon: 'cqssc/shiicon.ico' });
            noti.onshow = function() {
                setTimeout(noti.close.bind(noti), 3000);
            }
        }

        if (window.Notification) {
            if (Notification.permission !== "granted") {
                Notification.requestPermission(function(state) {
                    if (Notification.permission !== state) {
                        Notification.permission = state;
                        notifun();
                    }
                });
            } else {
                notifun();
            }
            cq.config.notification = this.checked;
        } else {
            this.checked = false;
            layer.msg("您的浏览器不支持通知");
        }

    });

    var online = function() {
        $.post('cqonline', {}, function(data, textStatus, xhr) {
            /*optional stuff to do after success */
            $("#curOnline").text(data.cur);
            $("#maxOnline").text(data.max);
        }, 'json');
    }

    if (utils.getParams("online")) {
        online();
        setInterval(function() {
            online();
        }, 3000);
    } else {
        $("#online").remove();
    }

    if (utils.getParams("wn")) {
        $(".cencus > .row").mouseenter(function(event) {
            /* Act on the event */
            $('.opened span[data-val="' + $(this).data('val') + '"]').addClass('active');
        }).mouseleave(function(event) {
            /* Act on the event */
            $('.opened span[data-val="' + $(this).data('val') + '"]').removeClass('active');
        });
    }

    // $(window).on('beforeunload', function() {
    //     console.log("cut")
    //     $.post('cqcutOnline', {}, function(data, textStatus, xhr) {
    //         console.log("cuted")
    //     });
    // });
});
