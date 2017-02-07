var utils = (function() {
	'use strict';

	function utils() {
		if (!(this instanceof utils)) {
			return new utils();
		}
	}

	utils.prototype = {

	}

	utils.getParams = function(arg) {
		var returnValue = undefined;
		var url = decodeURI(location.href);
        var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
        var paraObj = {}, j = {};
        for (var i = 0; j = paraString[i]; i++) {
            paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
        }
        if(arg) {
        	returnValue = paraObj[arg.toLowerCase()];	
        	if (typeof(returnValue) == "undefined") {
	            return "";
	        } else {
	            return (unescape(returnValue.replace(/\\u/gi, '%u')));
	        }
        } else {
        	returnValue = paraObj;
        }
        return returnValue;
	}

	return utils;
}());


