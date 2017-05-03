"use strict";
var progress = {};
( function() {
    progress.createCircle = createCircle;
    progress.load = load;
    progress.loadMany = loadMany;
    /**
     * @param method 'get' albo 'post'
     * @param url adres URL z API, serwer musi zwracać JSON-a
     * @param loadingSelectors tablica selektorów jQuery w których pojawi się animacja ładowania,
     * np. ['.progress_loading']
     * @param successSelectors tablica selektorów jQuery które zostaną pokazane przy sukcesie,
     * np. ['#progress_success', '#button_examine']
     * @param errorSelectors tablica selektorów jQuery które pojawią się w przypadku błędu,
     * np. ['#progress_error']
     * @param successHandler funkcja która się wykona po udanym zrealizowaniu zapytania,
     * dostanie 1 parametr - odpowiedź serwera (jakiś obiekt DTO)
     * @param optionalErrorHandler opcjonalna funkcja która się wykona w przypadku błędu
     * @param optionalParams opcjonalny obiekt który zostanie przesłany do serwera jako JSON (tylko dla POST)
     */
    function load(method, url, loadingSelectors, successSelectors, errorSelectors, successHandler, optionalErrorHandler, optionalParams) {
        var config;
        eachSelector(errorSelectors, function(e) {
            e.hide();
        });
        eachSelector(successSelectors, function(e) {
            e.hide();
        });
        eachSelector(loadingSelectors, function(e) {
            e.hide();
            e.empty();
            e.append(createCircle());
            e.fadeIn(200);
        });
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
                eachSelector(errorSelectors, function(e) {
                    e.hide();
                });
                eachSelector(loadingSelectors, function(e) {
                    e.hide();
                });
                eachSelector(successSelectors, function(e) {
                    e.fadeIn(200);
                });
                successHandler(response);
            }
            else {
                eachSelector(successSelectors, function(e) {
                    e.hide();
                });
                eachSelector(loadingSelectors, function(e) {
                    e.hide();
                });
                eachSelector(errorSelectors, function(e) {
                    e.fadeIn(200);
                });
                if (typeof optionalErrorHandler !== 'undefined') {
                    optionalErrorHandler(response);
                }
            }
        };
        config.error = function() {
            eachSelector(successSelectors, function(e) {
                e.hide();
            });
            eachSelector(loadingSelectors, function(e) {
                e.hide();
            });
            eachSelector(errorSelectors, function(e) {
                e.fadeIn(200);
            });
            if (typeof optionalErrorHandler !== 'undefined') {
                optionalErrorHandler(null);
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
    function loadMany(requests, loadingSelectors, successSelectors, errorSelectors, successHandler, optionalErrorHandler) {
        var done, responses, req, config;
        eachSelector(errorSelectors, function(e) {
            e.hide();
        });
        eachSelector(successSelectors, function(e) {
            e.hide();
        });
        eachSelector(loadingSelectors, function(e) {
            e.hide();
            e.empty();
            e.append(createCircle());
            e.fadeIn(200);
        });
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
                        eachSelector(loadingSelectors, function(e) {
                            e.hide();
                        });
                        eachSelector(successSelectors, function(e) {
                            e.hide();
                        });
                        eachSelector(errorSelectors, function(e) {
                            e.fadeIn(200);
                        });
                        if(typeof optionalErrorHandler !== 'undefined') {
                            optionalErrorHandler(result);
                        }
                    }
                };
                config.error = function() {
                    eachSelector(loadingSelectors, function(e) {
                        e.hide();
                    });
                    eachSelector(successSelectors, function(e) {
                        e.hide();
                    });
                    eachSelector(errorSelectors, function(e) {
                        e.fadeIn(200);
                    });
                    if(typeof optionalErrorHandler !== 'undefined') {
                        optionalErrorHandler(null);
                    }
                };
                $.ajax(config);
            }
            else {
                eachSelector(loadingSelectors, function(e) {
                    e.hide();
                });
                eachSelector(errorSelectors, function(e) {
                    e.hide();
                });
                eachSelector(successSelectors, function(e) {
                    e.fadeIn(200);
                });
                successHandler(responses);
            }
        }
    }

    function eachSelector(arr, func) {
        var i;
        for(i = 0; i < arr.length; i++) {
            func($(arr[i]));
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