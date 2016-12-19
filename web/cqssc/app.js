$(document).foundation();

var cqssc = (function() {
    var config = {
        peak: {
            AM:[0, 2],
            PM: [10, 12],
            interval: 5
        },
        usually: {
            times: [10, 22],
            interval: 10
        }
    };

    function cqssc() {
        if (!(this instanceof cqssc)) {
            return new cqssc();
        }
    }

    cqssc.getAll = function() {
        var defer = $.Deferred();
        $.post("cqall", {}, function(data) {
            defer.resolve(data);
        });
        return defer.promise();
    };

    cqssc.gethaoma = function(limit) {
        var defer = $.Deferred();
        $.post("cqhaoma", {"limit": limit}, function (data) {
            defer.resolve(data);
        });
        return defer.promise();
    };

    cqssc.getAll().done(function (data) {
        
    });


}());