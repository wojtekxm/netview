"use strict";
var progress = {};
( function() {
    progress.createCircle = createCircle;
    progress.load = load;
    progress.loadMany = loadMany;

    function load(method, url, jquerySelector, successHandler, optionalErrorHandler, optionalParams) {
        var parent, elemLoading, elemSuccess, elemError, config;
        parent = $(jquerySelector);
        elemLoading = parent.find('.progress-loading');
        elemSuccess = parent.find('.progress-success');
        elemError = parent.find('.progress-error');
        elemError.hide();
        elemSuccess.hide();
        elemLoading.hide();
        elemLoading.empty();
        elemLoading.append(createCircle());
        elemLoading.fadeIn(200);
        config = {};
        config.url = url;
        config.dataType = 'json';
        config.type = method;
        if(method.toLowerCase() === 'post') {
            if (typeof optionalParams !== 'undefined') {
                config.data = JSON.stringify(optionalParams);
                config.contentType = 'application/json';
            }
        }
        config.success = function(response) {
            if ( (typeof response.success !== 'undefined')
                && (response.success) ) {
                elemError.hide();
                elemLoading.hide();
                elemSuccess.fadeIn(200);
                successHandler(response);
            }
            else {
                elemSuccess.hide();
                elemLoading.hide();
                elemError.fadeIn(200);
                if (typeof optionalErrorHandler !== 'undefined') {
                    optionalErrorHandler();
                }
            }
        };
        config.error = function() {
            elemSuccess.hide();
            elemLoading.hide();
            elemError.fadeIn(200);
            if (typeof optionalErrorHandler !== 'undefined') {
                optionalErrorHandler();
            }
        };
        $.ajax(config);
    }

    /*
    Request: {
        "url" : '/api/examine/all'
        "optionalPostData" : false
    }
    */
    function loadMany(requests, jquerySelector, successHandler, optionalErrorHandler) {
        var done, responses, req, parent, elemLoading, elemSuccess, elemError, config;
        parent = $(jquerySelector);
        elemLoading = parent.find('.progress-loading');
        elemSuccess = parent.find('.progress-success');
        elemError = parent.find('.progress-error');
        elemError.hide();
        elemSuccess.hide();
        elemLoading.hide();
        elemLoading.empty();
        elemLoading.append(createCircle());
        elemLoading.fadeIn(200);
        done = 0;
        responses = [];
        step();

        function step() {
            if(done < requests.length) {
                req = requests[done];
                config = {};
                config.url = req.url;
                config.dataType = 'json';
                if(typeof req.optionalPostData !== 'undefined') {
                    config.type = 'post';
                    if(req.optionalPostData !== false) {
                        config.data = JSON.stringify(req.optionalPostData);
                        config.contentType = 'application/json';
                    }
                }
                else {
                    config.type = 'get';
                }
                config.success = function(result) {
                    if ( (typeof result.success !== 'undefined')
                        && (result.success) ) {
                        responses.push(result);
                        done++;
                        step();
                    }
                    else {
                        err();
                    }
                };
                config.error = err;
                $.ajax(config);
            }
            else {
                elemLoading.hide();
                elemError.hide();
                elemSuccess.fadeIn(200);
                successHandler(responses);
            }
            function err() {
                elemLoading.hide();
                elemSuccess.hide();
                elemError.fadeIn(200);
                if(typeof optionalErrorHandler !== 'undefined') {
                    optionalErrorHandler();
                }
            }
        }
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