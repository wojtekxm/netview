"use strict";
var progress = {};
( function() {
    progress.createCircle = createCircle;
    progress.loadGet = loadGet;
    progress.loadPost = loadPost;

    function loadGet(url, jquerySelector, successHandler, optionalErrorHandler, optionalGetParams) {
        var parent = $(jquerySelector);
        var elemLoading = parent.children('.progress-loading');
        var elemSuccess = parent.children('.progress-success');
        var elemError = parent.children('.progress-error');
        elemError.hide();
        elemSuccess.hide();
        elemLoading.show();
        elemLoading.empty();
        elemLoading.append(createCircle());
        var config = {};
        config.url = url;
        config.type = 'get';
        config.dataType = 'json';
        if(typeof optionalGetParams !== 'undefined') {
            config.data = optionalGetParams;
        }
        config.success = function(response) {
            elemLoading.empty();
            elemLoading.hide();
            if ( (typeof response.success !== 'undefined')
                && (response.success) ) {
                elemError.hide();
                elemSuccess.show();
                successHandler(response);
            }
            else {
                elemSuccess.hide();
                elemError.show();
                if (typeof optionalErrorHandler !== 'undefined') {
                    optionalErrorHandler();
                }
            }
        };
        config.error = function() {
            elemLoading.empty();
            elemLoading.hide();
            elemSuccess.hide();
            elemError.show();
            if(typeof optionalErrorHandler !== 'undefined') {
                optionalErrorHandler();
            }
        };
        $.ajax(config);
    }

    function loadPost(url, jquerySelector, successHandler, optionalErrorHandler, optionalPostParams) {
        var parent = $(jquerySelector);
        var elemLoading = parent.find('.progress-loading');
        var elemSuccess = parent.find('.progress-success');
        var elemError = parent.find('.progress-error');
        elemError.hide();
        elemSuccess.hide();
        elemLoading.show();
        elemLoading.empty();
        elemLoading.append(createCircle());
        var config = {};
        config.url = url;
        config.type = 'post';
        config.dataType = 'json';
        if(typeof optionalPostParams !== 'undefined') {
            config.data = optionalPostParams;
            config.contentType = 'application/json';
        }
        config.success = function(response) {
            elemLoading.empty();
            elemLoading.hide();
            if ( (typeof response.success !== 'undefined')
                && (response.success) ) {
                elemError.hide();
                elemSuccess.show();
                successHandler(response);
            }
            else {
                elemSuccess.hide();
                elemError.show();
                if (typeof optionalErrorHandler !== 'undefined') {
                    optionalErrorHandler();
                }
            }
        };
        config.error = function() {
            elemLoading.empty();
            elemLoading.hide();
            elemSuccess.hide();
            elemError.show();
            if(typeof optionalErrorHandler !== 'undefined') {
                optionalErrorHandler();
            }
        };
        $.ajax(config);
    }

    function createCircle() {
        var div = $('<div></div>').addClass('progress-circle-main');
        for(var i = 1; i <= 12; i++) {
            var clazz = 'progress-circle-small progress-circle' + i;
            div.append(
                $('<div></div>').addClass(clazz)
            );
        }
        return div;
    }
} )();