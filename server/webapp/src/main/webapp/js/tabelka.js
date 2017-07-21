/*
 This file is part of the NetView open source project
 Copyright (c) 2017 NetView authors
 Licensed under The MIT License
 */
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
    tabelka.builder = builder;

    /**
     * object ColumnDefinition {
     * "label" : string
     * "comparator" : optional function (if not specified, sorting is disabled)
     * "extractor" : string, name of data property holding <td> content
     * "generator" : function generating value of extractor for each data element
     * "cssClass" : optional string
     * }
     * @param columnDefinitions, tabela ColumnDefinition, gdzie każdy element opisuje kolejną kolumnę
     */
    function create(columnDefinitions, uniquePrefix, searchTextGenerator) {
        var sortIndex, sortAscending, sortedData,
            pageIndex, pageSizes, pageCapacity,
            filteredData, filterText, searchTextProperty,
            paginationDiv, theadRow, tbody;
        initModel();
        var result = {
            "jqueryDom": initDom(),
            "resetData": resetData
        };
        refreshDom();
        return result;

        function initModel() {
            sortIndex = 0;
            sortAscending = true;
            sortedData = [];
            filteredData = [];
            filterText = '';
            pageIndex = 0;
            pageSizes = makeSizeOptions(0);
            pageCapacity = pageSizes[0];
            searchTextProperty = uniquePrefix + 'text';
        }

        function initDom() {
            var i, div, table, colgroup;
            div = $('<div></div>');
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
            theadRow = $('<tr></tr>');
            tbody = $('<tbody></tbody>');
            table = $('<table class="table table-striped table-bordered" style="background-color: white;"></table>')
                .append(
                    colgroup,
                    $('<thead></thead>').append(theadRow),
                    tbody
                );
            div.append(table);
            paginationDiv = $('<div class="clearfix"></div>');
            div.append(paginationDiv);
            return div;
        }

        function filterSearch() {
            var i, k, source;
            filteredData = [];
            filterText = filterText.toLocaleLowerCase();
            if(filterText === '') {
                for (i = 0; i < sortedData.length; i++) {
                    filteredData.push(sortedData[i]);
                }
            }
            else {
                for (i = 0; i < sortedData.length; i++) {
                    source = sortedData[i][searchTextProperty];
                    for (k = 0; k < source.length; k++) {
                        if (source[k].indexOf(filterText) > -1) {
                            filteredData.push(sortedData[i]);
                            break;
                        }
                    }
                }
            }
        }

        function refreshDom() {
            tbody.empty();
            theadRow.empty();
            paginationDiv.empty();
            buildThead();
            buildTbody();
            buildPagination();

            function buildThead() {
                var i, definition, th, span;
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
                                    filteredData.reverse();
                                }
                                else {
                                    sortIndex = newSortIndex;
                                    sortAscending = true;
                                    sortedData.sort(
                                        util.chooseComparator(
                                            columnDefinitions[sortIndex].comparator
                                        )
                                    );
                                    filterSearch();
                                }
                                refreshDom();
                            }
                        );
                    }
                    theadRow.append(th);
                }
            }

            function buildTbody() {
                var i, k, start, end, tr;
                start = pageIndex * pageCapacity;
                end = start + pageCapacity;
                if (end > filteredData.length) end = filteredData.length;
                for(i = start; i < end; i++) {
                    tr = $('<tr></tr>');
                    tr.append(
                        $('<td></td>').addClass('text-center').text(i+1)
                    );
                    for(k = 0; k < columnDefinitions.length; k++) {
                        tr.append(
                            $('<td></td>').append(
                                filteredData[i][ columnDefinitions[k].extractor ]
                            )
                        );
                    }
                    tbody.append(tr);
                }
            }

            function buildPagination() {
                var i, k, arr,
                    searchInput, searchBtn, paginationNavList, paginationSizeSelect,
                    span, li, opt;
                var defaultPaginationThresold = 10;
                if(sortedData.length >= defaultPaginationThresold) {
                    paginationSizeSelect = $('<select class="form-control" style="display: inline-block; width: 100px"></select>');
                    paginationNavList = $('<ul class="pagination pull-right" style="margin: 0"></ul>');
                    searchInput = $('<input type="text" class="form-control">').val(filterText);
                    searchBtn = $('<button type="button" class="btn btn-primary"></button>').text('filtruj');
                    paginationDiv.append(
                        $('<div class="row"></div>').append(
                            $('<div class="col-sm-4" style="margin-bottom: 10px"></div>').append(
                                $('<div class="input-group"></div>').append(
                                    searchInput,
                                    $('<span class="input-group-btn"></span>').append(
                                        searchBtn
                                    )
                                )
                            ),
                            $('<div class="col-sm-4 text-center" style="margin-bottom: 10px"></div>').append(
                                $('<div class="form-inline"></div>').append(
                                    $('<div class="form-group"></div>').append(
                                        $('<span></span>').text('wyświetlaj po '),
                                        paginationSizeSelect
                                    )
                                )
                            ),
                            $('<div class="col-sm-4" style="margin-bottom: 10px"></div>').append(
                                paginationNavList
                            )
                        )
                    );
                    searchBtn.click(function() {
                        filterText = searchInput.val();
                        filterSearch();
                        refreshDom();
                        searchBtn.prop('disabled', true);
                    });
                    paginationSizeSelect.change(function() {
                        pageCapacity = parseInt( $(this).val() );
                        if(pageIndex >= getTotalPages()) {
                            pageIndex = getTotalPages() - 1;
                        }
                        refreshDom();
                    });
                    for(i = 0; i < pageSizes.length; i++) {
                        k = pageSizes[i];
                        opt = $('<option></option>');
                        opt.val(k);
                        opt.text(k);
                        if(k === pageCapacity) {
                            opt.prop('selected', true);
                        }
                        paginationSizeSelect.append(opt);
                    }
                    arr = makePageLinks();
                    for (i = 0; i < arr.length; i++) {
                        k = arr[i];
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
                                refreshDom();
                            }
                        );
                        paginationNavList.append(li);
                    }
                }
            }
        }

        function resetData(data) {
            var i, row, k, def, tdContent, arr;
            sortedData = [];
            for(i = 0; i < data.length; i++) {
                row = data[i];
                sortedData.push(row);
                for(k = 0; k < columnDefinitions.length; k++) {
                    def = columnDefinitions[k];
                    tdContent = def.generator(row);
                    if(typeof tdContent === 'string') {
                        tdContent = $('<span></span>').text(tdContent);
                    }
                    row[def.extractor] = tdContent;
                }
            }
            for(i = 0; i < data.length; i++) {
                arr = searchTextGenerator(data[i]);
                for(k = 0; k < arr.length; k++) {
                    arr[k] = arr[k].toLocaleLowerCase();
                }
                data[i][searchTextProperty] = arr;
            }
            sortedData.sort(
                util.chooseComparator(
                    columnDefinitions[sortIndex].comparator,
                    sortAscending
                )
            );
            filterSearch();
            pageSizes = makeSizeOptions(data.length);
            pageCapacity = getClosestNumber(pageSizes, pageCapacity);
            if(pageIndex >= getTotalPages()) {
                pageIndex = getTotalPages() - 1;
            }
            refreshDom();
        }

        // zwraca zawsze tablice z conajmniej jednym elementem
        function makeSizeOptions(dataLength) {
            var arr, last, k, i, result;
            arr = [10, 25, 50, 100, 250, 500, 1000, dataLength];
            arr.sort(util.compareNumbers);
            last = null;
            result = [];
            for(i = 0; i < arr.length; i++) {
                k = arr[i];
                if(k === last)continue;
                if(k > dataLength)continue;
                result.push(k);
                last = k;
            }
            return result;
        }

        // zwraca zawsze tablice z conajmniej jednym elementem
        function makePageLinks() {
            var totalPages, arr, i, k, last, result;
            totalPages = getTotalPages();
            arr = [0,
                1,
                pageIndex - 1,
                pageIndex,
                pageIndex + 1,
                totalPages - 2,
                totalPages - 1];
            arr.sort(util.compareNumbers);
            result = [];
            last = null;
            for (i = 0; i < arr.length; i++) {
                k = arr[i];
                if (last === k)continue;
                if (k < 0)continue;
                if (totalPages <= k)continue;
                result.push(k);
                last = k;
            }
            return result;
        }

        // zawsze conajmniej 1
        function getTotalPages() {
            if(filteredData.length < 1 || pageCapacity < 1) {
                return 1;
            }
            return Math.ceil( filteredData.length / pageCapacity );
        }

        function getClosestNumber(options, target) {
            var i, bestAbs, bestValue, currentValue, currentAbs;
            bestValue = null;
            for(i = 0; i < options.length; i++) {
                currentValue = options[i];
                currentAbs = Math.abs(currentValue - target);
                if(bestValue === null || currentAbs < bestAbs) {
                    bestValue = currentValue;
                    bestAbs = currentAbs;
                }
            }
            return bestValue;
        }
    }

    /**
     * function ColumnGenerator(dataRow) : jQuery element, albo tekst który zostanie wstawiony w kolumnie wiersza
     */

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
        bl.searchGenerator = searchGenerator;
        bl.column = column;
        bl.special = special;
        bl.deviceFrequency = deviceFrequency;
        bl.buttonUnlink = buttonUnlink;
        bl.build = build;
        return bl;

        function searchGenerator(searchTextGenerator) {
            bl.searchTextGenerator = searchTextGenerator;
            return bl;
        }

        /**
         * @param label string, column header
         * @param sortType string, 'text' or 'number' or null
         * @param propertyName string
         * @param width16 optional integer|string 0-16 or css class
         * @param columnGenerator function|string funkcja typu ColumnGenerator,
         * albo nazwa pola które zawiera text do wstawienia
         * @returns builder
         */
        function column(label, sortType, propertyName, width16, columnGenerator) {
            var comparator, clazz, generator;
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
            if(typeof columnGenerator === 'string') {
                generator = function(row) {
                    return $('<span></span>').text(row[columnGenerator]);
                };
            }
            else {
                generator = columnGenerator;
            }
            bl.definitions.push( {
                "label" : label,
                "comparator" : comparator,
                "extractor" : bl.uniquePrefix + bl.definitions.length + '_td',
                "generator" : generator,
                "cssClass" : clazz
            } );
            return bl;
        }

        /**
         * shorter version of column(), without sorting
         * @param label string, column header
         * @param width16 optional integer|string 0-16 or css class
         * @param columnGenerator function|string funkcja typu ColumnGenerator,
         * albo nazwa pola które zawiera text do wstawienia
         * @returns builder
         */
        function special(label, width16, columnGenerator) {
            var clazz, generator;
            if(typeof width16 === 'string') {
                clazz = width16;
            }
            else if(typeof width16 === 'number') {
                clazz = 'width-' + width16;
            }
            if(typeof columnGenerator === 'string') {
                generator = function(row) {
                    return $('<span></span>').text(row[columnGenerator]);
                };
            }
            else {
                generator = columnGenerator;
            }
            bl.definitions.push( {
                "label" : label,
                "extractor" : bl.uniquePrefix + bl.definitions.length + '_td',
                "generator" : generator,
                "cssClass" : clazz
            } );
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
                var sur, span, fq;
                span = $('<span></span>');
                fq = deviceDetailsDto.frequency;
                if((typeof fq[mhz] !== 'undefined') &&
                    (fq[mhz] !== null)) {
                    sur = fq[mhz];
                    span.append(
                        $('<span class="glyphicon glyphicon-check" style="color:#5cb85c"></span>')
                    );
                    if(sur.enabled === false) {
                        deviceDetailsDto[cmpProperty] = -1;
                        span.addClass('label label-danger');
                        span.text('wyłączone');
                    }
                    else if(sur.clients < 1) {
                        deviceDetailsDto[cmpProperty] = sur.clients;
                        span.addClass('label label-warning');
                        span.text(sur.clients);
                    }
                    else {
                        deviceDetailsDto[cmpProperty] = sur.clients;
                        span.addClass('label label-success');
                        span.text(sur.clients);
                    }
                }
                else {
                    deviceDetailsDto[cmpProperty] = -2;
                    span.addClass('label label-default');
                    span.text('nie wspiera');
                    /*span.append(
                        $('<span class="glyphicon glyphicon-unchecked" style="color:#d9534f"></span>'),
                        ' nie wspiera'
                    );*/
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
            var where, result;
            bl.futureData = data;
            where = $(tabelkaSelector);
            where.empty();
            result = create(bl.definitions, bl.uniquePrefix + '#', bl.searchTextGenerator);
            result.resetData(data);
            where.append(result.jqueryDom);
            return result;
        }
    }
})();