/**
 * Created by rjora on 2016/12/16 0016.
 */

var app = {
    req_url: 'http://www.cp66607.com/api/cqssc?act=lishikaijianghaoma'
}

$(document).foundation();

$.post(app.req_url, {limit: 1}, function(data) {
    $(".test").text(data);
}, "json");