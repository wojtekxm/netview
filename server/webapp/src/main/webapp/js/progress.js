"use strict";
var progress = {};
( function() {
    progress.createCircle = createCircle;
    progress.loadGet = loadGet;
    progress.load = load;
    progress.loadParallel = loadParallel;

    /**
     * @param url adres URL z API, serwer musi zwracać JSON-a
     * @param loadingSelectors array of strings, jQuery selectors,
     * that will show progress animation during loading
     * @param successSelectors array of strings, jQuery selectors,
     * that will be shown when all requests have finished successfully
     * @param errorSelectors array of strings, jQuery selectors,
     * that will be shown when some request has failed
     * @param successHandler function Handler that will be called after all requests have completed
     * @param errorHandler optional function ErrorHandler that will be called after some request has failed
     * @param sizeLabel optional string, 'xs' or 'lg' or 'md'
     */
    function loadGet(url, loadingSelectors, successSelectors, errorSelectors, successHandler, errorHandler, sizeLabel) {
        load(
            [{
                "url" : url
            }],
            loadingSelectors, successSelectors, errorSelectors,
            function(responses) {
                successHandler(responses[0]);
            },
            errorHandler,
            sizeLabel
        );
    }

    /**
     * object Request {
     * "url" : string,
     * "method" : optional string, 'get' or 'post', default 'get', case insensitive
     * "postData" : optional any object, only for method POST
     * }
     * function SuccessHandler(responses)
     * responses - array of server responses in the same order like parameter requests
     * function ErrorHandler(response)
     * response - server response or null
     * @param requests array of Request objects
     * @param loadingSelectors array of strings, jQuery selectors,
     * that will show progress animation during loading
     * @param successSelectors array of strings, jQuery selectors,
     * that will be shown when all requests have finished successfully
     * @param errorSelectors array of strings, jQuery selectors,
     * that will be shown when some request has failed
     * @param successHandler function Handler, will be called after all requests have completed
     * @param errorHandler optional function ErrorHandler, will be called after some request has failed
     * @param sizeLabel optional string, 'xs' or 'lg' or 'md'
     */
    function load(requests, loadingSelectors, successSelectors, errorSelectors, successHandler, errorHandler, sizeLabel) {
        var done, responses;
        showLoading(loadingSelectors, successSelectors, errorSelectors, sizeLabel);
        done = 0;
        responses = [];
        step();

        function step() {
            var req, config, met;
            if(done < requests.length) {
                req = requests[done];
                config = {};
                config.url = req.url;
                config.dataType = 'json';
                met = 'get';
                if(typeof req.method === 'string') {
                    met = req.method.toLowerCase();
                }
                config.type = met;
                if( (met === 'post') && (typeof req.postData === 'object')) {
                    config.data = JSON.stringify(req.postData);
                    config.contentType = 'application/json';
                }
                config.success = function(result) {
                    if ( (typeof result.success !== 'undefined')
                        && (result.success) ) {
                        responses.push(result);
                        done++;
                        step();
                    }
                    else {
                        showError(loadingSelectors, successSelectors, errorSelectors);
                        if(typeof errorHandler === 'function') {
                            errorHandler(result);
                        }
                    }
                };
                config.error = function() {
                    showError(loadingSelectors, successSelectors, errorSelectors);
                    if(typeof errorHandler === 'function') {
                        errorHandler(null);
                    }
                };
                $.ajax(config);
            }
            else {
                showSuccess(loadingSelectors, successSelectors, errorSelectors);
                successHandler(responses);
            }
        }
    }

    /**
     * object Request {
     * "url" : string,
     * "method" : optional string, 'get' or 'post', default 'get', case insensitive
     * "postData" : optional any object, only for method POST
     * }
     * function SuccessHandler(responses)
     * responses - array of server responses in the same order like parameter requests
     * function ErrorHandler(responses)
     * responses - array where each element is either server response object or null.
     * Responses are ordered by requests.
     * @param requests array of Request objects
     * @param loadingSelectors array of strings, jQuery selectors,
     * that will show progress animation during loading
     * @param successSelectors array of strings, jQuery selectors,
     * that will be shown when all requests have finished successfully
     * @param errorSelectors array of strings, jQuery selectors,
     * that will be shown when some request has failed
     * @param successHandler function Handler that will be called after all requests have completed
     * @param errorHandler optional function ErrorHandler that will be called after some request has failed
     * @param sizeLabel optional string, 'xs' or 'lg' or 'md'
     */
    function loadParallel(requests, loadingSelectors, successSelectors, errorSelectors, successHandler, errorHandler, sizeLabel) {
        var responses, config, met, r, i, doneSuccess, doneError;
        showLoading(loadingSelectors, successSelectors, errorSelectors, sizeLabel);
        doneSuccess = 0;
        doneError = 0;
        responses = [];
        for(i = 0; i < requests.length; i++) {
            responses.push(null);
        }
        for(i = 0; i < requests.length; i++) {
            r = requests[i];
            config = {};
            config.url = r.url;
            config.dataType = 'json';
            met = 'get';
            if(typeof r.method === 'string') {
                met = r.method.toLowerCase();
            }
            config.type = met;
            if( (met === 'post') && (typeof r.postData === 'object')) {
                config.data = JSON.stringify(r.postData);
                config.contentType = 'application/json';
            }
            config.success = buildAjaxSuccess(i);
            config.error = buildAjaxError(i);
            $.ajax(config);
        }

        function buildAjaxSuccess(index) {
            return function(result) {
                if(result.success) {
                    responses[index] = result;
                    doneSuccess++;
                    checkEnd();
                }
                else {
                    responses[index] = result;
                    doneError++;
                    checkEnd();
                }
            };
        }
        function buildAjaxError(index) {
            return function() {
                responses[index] = null;
                doneError++;
                checkEnd();
            };
        }
        function checkEnd() {
            if(doneError + doneSuccess === requests.length) {
                if(doneError > 0) {
                    showError(loadingSelectors, successSelectors, errorSelectors);
                    if(typeof errorHandler === 'function') {
                        errorHandler(responses);
                    }
                }
                else {
                    showSuccess(loadingSelectors, successSelectors, errorSelectors);
                    successHandler(responses);
                }
            }
        }
    }

    function showLoading(loadingSelectors, successSelectors, errorSelectors, sizeLabel) {
        var i, e;
        for(i = 0; i < errorSelectors.length; i++) {
            $(errorSelectors[i]).hide();
        }
        for(i = 0; i < successSelectors.length; i++) {
            $(successSelectors[i]).hide();
        }
        for(i = 0; i < loadingSelectors.length; i++) {
            e = $(loadingSelectors[i]);
            e.hide();
            e.empty();
            if(sizeLabel === 'xs') {
                e.append(createCircle(16));
            }
            else if(sizeLabel === 'lg') {
                e.append(createCircle(64));
            }
            else {
                e.append(createCircle(32));
            }
            e.fadeIn(200);
        }
    }
    function showSuccess(loadingSelectors, successSelectors, errorSelectors) {
        var i;
        for(i = 0; i < loadingSelectors.length; i++) {
            $(loadingSelectors[i]).hide();
        }
        for(i = 0; i < errorSelectors.length; i++) {
            $(errorSelectors[i]).hide();
        }
        for(i = 0; i < successSelectors.length; i++) {
            $(successSelectors[i]).fadeIn(200);
        }
    }
    function showError(loadingSelectors, successSelectors, errorSelectors) {
        var i;
        for(i = 0; i < loadingSelectors.length; i++) {
            $(loadingSelectors[i]).hide();
        }
        for(i = 0; i < successSelectors.length; i++) {
            $(successSelectors[i]).hide();
        }
        for(i = 0; i < errorSelectors.length; i++) {
            $(errorSelectors[i]).fadeIn(200);
        }
    }

    /**
     * @param size integer, width and height in pixels
     * @returns jQuery element
     */
    function createCircle(size) {
        var div = $('<div></div>')
            .addClass('progress-circle-main')
            .css('width', size + 'px')
            .css('height', size + 'px');
        for(var i = 1; i <= 12; i++) {
            var clazz = 'progress-circle-small progress-circle' + i;
            div.append(
                $('<div></div>').addClass(clazz)
            );
        }
        return div;
    }
} )();