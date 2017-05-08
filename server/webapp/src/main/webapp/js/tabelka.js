"use strict";
// wymaga: progress.js i tabelka.css
var util = {};
(function(){
    util.comparatorText = comparatorText;
    util.comparatorNumber = comparatorNumber;
    util.compareNumbers = compareNumbers;
    util.chooseComparator = chooseComparator;
    util.reverseComparator = reverseComparator;

    function comparatorText(key, optionalAscending) {
        return chooseComparator(
            function(a, b) {
                var x = a[key];
                if (x === null) x = '';
                var y = b[key];
                if (y === null) y = '';
                return x.localeCompare(y);
            },
            optionalAscending
        );
    }
    function comparatorNumber(key, optionalAscending) {
        return chooseComparator(
            function(a, b) {
                var x = a[key];
                var y = b[key];
                return x - y;
            },
            optionalAscending
        );
    }
    function compareNumbers(a, b) {
        return a - b;
    }
    function chooseComparator(comparator, optionalAscending) {
        if( (typeof optionalAscending === 'undefined') || optionalAscending ) {
            return comparator;
        }
        else {
            return reverseComparator(comparator);
        }
    }
    function reverseComparator(comparator) {
        return function(x, y) {
            return comparator(y, x);
        }
    }
})();

var tabelka = {};
(function(){
    tabelka.create = create;
    tabelka.builder = builder;

    /**
     * object ColumnDefinition {
     * "label" : string
     * "comparator" : optional function (if not specified, sorting is disabled)
     * "extractor" : string, name of data property holding <td> content
     * "cssClass" : optional string
     * }
     * @param data tabela danych, każdy element będzie pokazany w jednym wierszu
     * @param columnDefinitions, tabela ColumnDefinition, gdzie każdy element opisuje kolejną kolumnę
     * @param optionalPaginationThresold
     * @param optionalPageSizes
     * @returns {*|jQuery|HTMLElement}
     */
    function create(data, columnDefinitions, optionalPaginationThresold, optionalPageSizes) {
        var i, k, tmp, last, sortIndex, sortAscending, sortedData, paginationBar,
            paginationSizeSelect, paginationNavList, pageIndex, pageCapacity, pageSizes,
            colgroup, theadRow, tbody, table, div;
        sortIndex = -1;
        sortAscending = true;
        sortedData = [];
        for(i = 0; i < data.length; i++) {
            sortedData.push(data[i]);
        }
        if(typeof optionalPaginationThresold === 'undefined') {
            optionalPaginationThresold = 10;
        }
        paginationBar = null;
        if(data.length >= optionalPaginationThresold) {
            paginationBar = $('<div class="clearfix" style="padding-bottom: 10px"></div>');
            paginationSizeSelect = $('<select class="form-control" style="display: inline-block; width: 100px"></select>');
            paginationNavList = $('<ul class="pagination pull-right" style="margin: 0"></ul>');
            paginationBar.append(
                $('<div class="pull-left form-inline"></div>').append(
                    $('<div class="form-group"></div>').append(
                        $('<span></span>').text('wyświetlaj po '),
                        paginationSizeSelect
                    )
                ),
                paginationNavList
            );
            paginationSizeSelect.change(function() {
                pageCapacity = parseInt( $(this).val() );
                pageIndex = 0;
                refresh();
            });
        }
        if(typeof optionalPageSizes === 'undefined') {
            optionalPageSizes = [10, 25, 50, 100, 250, 500, 1000];
        }
        tmp = [];
        for(i = 0; i < optionalPageSizes.length; i++) {
            tmp.push(optionalPageSizes[i]);
        }
        tmp.push(data.length);
        pageCapacity = tmp[0];
        pageIndex = 0;
        tmp.sort(util.compareNumbers);
        last = null;
        pageSizes = [];
        for(i = 0; i < tmp.length; i++) {
            k = tmp[i];
            if(k === last)continue;
            if(k < 1)continue;
            if(k > data.length)continue;
            pageSizes.push(k);
            last = k;
        }

        theadRow = $('<tr></tr>');
        tbody = $('<tbody></tbody>');
        colgroup = $('<colgroup></colgroup>');
        colgroup.append( $('<col span="1" class="width-0"/>') );
        for(i = 0; i < columnDefinitions.length; i++) {
            if(typeof columnDefinitions[i].cssClass !== 'undefined') {
                colgroup.append(
                    $('<col span="1"/>').addClass(columnDefinitions[i].cssClass)
                );
            }
            else {
                colgroup.append(
                    $('<col span="1"/>')
                );
            }
        }
        table = $('<table class="table table-striped table-bordered"></table>')
            .append(
                colgroup,
                $('<thead></thead>').append(theadRow),
                tbody
            );
        div = $('<div></div>');
        div.append(table);
        if(paginationBar !== null) {
            div.append(paginationBar);
        }
        refresh();
        return div;

        function refresh() {
            var i, k, arr, last, li, definition, th, span, option, start, end, row, tr, totalPages;
            tbody.empty();
            theadRow.empty();

            theadRow.append(
                $('<th></th>')
            );
            for(i = 0; i < columnDefinitions.length; i++) {
                definition = columnDefinitions[i];
                th = $('<th></th>');
                th.append(
                    $('<span></span>').text(definition.label)
                );
                if(typeof definition.comparator === 'function') {
                    th.attr('style', 'cursor: pointer');
                    span = $('<span class="pull-right glyphicon"></span>');
                    if (i === sortIndex) {
                        th.addClass('active');
                        if (sortAscending) {
                            span.addClass('glyphicon-sort-by-attributes');
                        }
                        else {
                            span.addClass('glyphicon-sort-by-attributes-alt');
                        }
                    }
                    else {
                        span.addClass('glyphicon glyphicon-sort');
                    }
                    th.append(span);
                    th.click(
                        { "columnIndex": i },
                        function(event) {
                            var newSortIndex = event.data.columnIndex;
                            if(newSortIndex === sortIndex) {
                                sortAscending = !sortAscending;
                                sortedData.reverse();
                            }
                            else {
                                sortIndex = newSortIndex;
                                sortAscending = true;
                                sortedData.sort(
                                    util.chooseComparator(
                                        columnDefinitions[sortIndex].comparator
                                    )
                                );
                            }
                            refresh();
                        }
                    );
                }
                theadRow.append(th);
            }

            if(paginationBar !== null) {
                start = pageIndex * pageCapacity;
                end = start + pageCapacity;
                if (end > sortedData.length) end = sortedData.length;
            }
            else {
                start = 0;
                end = sortedData.length;
            }
            for(i = start; i < end; i++) {
                row = sortedData[i];
                tr = $('<tr></tr>');
                tr.append(
                    $('<td></td>').addClass('text-center').text(i+1)
                );
                for(k = 0; k < columnDefinitions.length; k++) {
                    definition = columnDefinitions[k];
                    tr.append(
                        $('<td></td>').append( row[definition.extractor] )
                    );
                }
                tbody.append(tr);
            }
            if(paginationBar !== null) {
                paginationSizeSelect.empty();
                for(i = 0; i < pageSizes.length; i++) {
                    k = pageSizes[i];
                    option = $('<option></option>');
                    option.val(k);
                    option.text(k);
                    if(k === pageCapacity) {
                        option.prop('selected', true);
                    }
                    paginationSizeSelect.append(option);
                }

                paginationNavList.empty();
                totalPages = Math.ceil( sortedData.length / pageCapacity );
                if(totalPages > 1) {
                    arr = [0, pageIndex - 2, pageIndex - 1, pageIndex, pageIndex + 1, pageIndex + 2, totalPages - 1];
                    arr.sort(util.compareNumbers);
                    last = null;
                    for (i = 0; i < arr.length; i++) {
                        k = arr[i];
                        if (last === k)continue;
                        if (k < 0)continue;
                        if (totalPages <= k)continue;
                        span = $('<span class="span-link"></span>');
                        span.text(k + 1);
                        li = $('<li></li>').append(span);
                        if (k === pageIndex) {
                            span.addClass('active');
                            li.addClass('active');
                        }
                        li.click(
                            { "pageIndex": k },
                            function(event) {
                                pageIndex = event.data.pageIndex;
                                refresh();
                            }
                        );
                        paginationNavList.append(li);
                        last = k;
                    }
                }
            }
        }
    }

    /**
     *
     * @param uniquePrefix undefined|string. Prefiks od którego będą się zaczynać pola danych dodane przez buildera.
     * Należy wybrać taki by uniknąć kolizji z innymi polami w danych. Domyślnie '!'.
     * @returns object builder
     */
    function builder(uniquePrefix) {
        var bl = {};
        bl.definitions = [];
        bl.generators = [];
        if(typeof uniquePrefix === 'string') {
            bl.uniquePrefix = uniquePrefix;
        }
        else {
            bl.uniquePrefix = '!';
        }
        bl.futureData = [];
        bl.column = column;
        bl.special = special;
        bl.deviceFrequency = deviceFrequency;
        bl.buttonUnlink = buttonUnlink;
        bl.build = build;
        return bl;

        /**
         * @param label string, column header
         * @param sortType string, 'text' or 'number' or null
         * @param propertyName string
         * @param width16 optional integer|string 0-16 or css class
         * @param columnGenerator function
         * @returns builder
         * function ColumnGenerator(dataRow) : jQuery element
         */
        function column(label, sortType, propertyName, width16, columnGenerator) {
            var comparator, clazz;
            if(typeof width16 === 'string') {
                clazz = width16;
            }
            else if(typeof width16 === 'number') {
                clazz = 'width-' + width16;
            }
            if(sortType === 'text') {
                comparator = util.comparatorText(propertyName);
            }
            else if(sortType === 'number') {
                comparator = util.comparatorNumber(propertyName);
            }
            /*else if(sortType === null) {
                comparator = undefined;
            }*/
            bl.definitions.push( {
                "label" : label,
                "comparator" : comparator,
                "extractor" : bl.uniquePrefix + bl.definitions.length + '_td',
                "cssClass" : clazz
            } );
            bl.generators.push(columnGenerator);
            return bl;
        }

        /**
         * shorter version of column(), without sorting
         * @param label string, column header
         * @param width16 optional integer|string 0-16 or css class
         * @param columnGenerator function
         * @returns builder
         */
        function special(label, width16, columnGenerator) {
            var clazz;
            if(typeof width16 === 'string') {
                clazz = width16;
            }
            else if(typeof width16 === 'number') {
                clazz = 'width-' + width16;
            }
            bl.definitions.push( {
                "label" : label,
                "extractor" : bl.uniquePrefix + bl.definitions.length + '_td',
                "cssClass" : clazz
            } );
            bl.generators.push(columnGenerator);
            return bl;
        }

        /**
         * Only for DeviceDetailsDto
         * @param label string, column header
         * @param mhz integer or string, 2400 or 5000 or '2400' or '5000'
         * @param width16 optional integer|string 0-16 or css class
         * @returns builder
         */
        function deviceFrequency(label, mhz, width16) {
            var cmpProperty = bl.uniquePrefix + bl.definitions.length + '_cmp';
            return bl.column(label, 'number', cmpProperty, width16, function(deviceDetailsDto) {
                var sur, span;
                span = $('<span></span>');
                if(typeof deviceDetailsDto.frequency[mhz] !== 'undefined') {
                    sur = deviceDetailsDto.frequency[mhz];
                    if(sur !== null && sur.enabled) {
                        deviceDetailsDto[cmpProperty] = sur.clients;
                        span.text(sur.clients);
                    }
                    else {
                        deviceDetailsDto[cmpProperty] = -1;
                        span.text('wył.');
                    }
                }
                else {
                    deviceDetailsDto[cmpProperty] = -2;
                    span.text('-');
                }
                return span;
            });
        }

        /**
         * Tylko dla danych gdzie każdy wiersz ma pole 'id'
         * @param label string, nagłówek kolumny
         * @param requestsGenerator function, w stylu RequestsGenerator
         * @param successHandler function, w stylu progress.load()*SuccessHandler
         * @param errorHandler optional function, w stylu progress.load()*ErrorHandler
         * function RequestsGenerator(rowId) : tablica obiektów Request do progress.load()
         */
        function buttonUnlink(label, requestsGenerator, successHandler, errorHandler) {
            var btnProperty = bl.uniquePrefix + bl.definitions.length + '_btn';
            return bl.special(label, 0, function(row) {
                var divLoading, divContainer, span, btn;
                divLoading = $('<div class="pull-right progress-space-xs later"></div>');
                span = $('<span class="glyphicon glyphicon-minus"></span>');
                btn = $('<button class="btn btn-danger btn-xs pull-right"></button>');
                btn.get()[0].addEventListener('click', function() {
                    var i, arr;
                    arr = bl.futureData;
                    for(i = 0; i < arr.length; i++) {
                        arr[i][btnProperty].prop('disabled', true);
                    }
                    progress.load(
                        requestsGenerator(row.id),
                        [divLoading], [], [],
                        function(responses) {
                            var i, arr;
                            arr = bl.futureData;
                            for(i = 0; i < arr.length; i++) {
                                arr[i][btnProperty].prop('disabled', false);
                            }
                            successHandler(responses);
                        },
                        function(responses) {
                            var i, arr;
                            arr = bl.futureData;
                            for(i = 0; i < arr.length; i++) {
                                arr[i][btnProperty].prop('disabled', false);
                            }
                            if(typeof errorHandler === 'function') {
                                errorHandler(responses);
                            }
                        },
                        'xs'
                    );
                });
                divContainer = $('<div class="clearfix" style="width:48px"></div>');
                divContainer.append(
                    btn.append(span),
                    divLoading
                );
                row[btnProperty] = btn;
                return divContainer;
            });
        }

        function build(tabelkaSelector, data) {
            var where, i, k, e, def, gen;
            bl.futureData = data;
            for(i = 0; i < data.length; i++) {
                e = data[i];
                for(k = 0; k < bl.generators.length; k++) {
                    def = bl.definitions[k];
                    gen = bl.generators[k];
                    e[def.extractor] = gen(e);
                }
            }
            where = $(tabelkaSelector);
            where.empty();
            where.append(
                tabelka.create(data, bl.definitions)
            );
        }
    }
})();