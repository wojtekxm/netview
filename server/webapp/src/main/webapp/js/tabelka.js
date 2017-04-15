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

/*
ColumnDefinition: {
    "label" : 'nazwa',
    "comparator" : util.comparatorText('name'),
    "extractor" : function(row) {
        return $('<span></span>').text(row.name);
        }
}
 */
var tabelka = {};
(function(){
    tabelka.create = create;
    function create(data, columnDefinitions, optionalPaginationThresold, optionalPageSizes) {
        var i, k, sortIndex, sortAscending, sortedData, paginationBar, paginationSizeSelect,
            paginationNavList, pageIndex, pageCapacity, pageSizes,
            theadRow, tbody, table, div;
        sortIndex = 0;
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
            paginationBar = $('<div class="row"></div>');
            paginationSizeSelect = $('<select class="form-control input-sm" style="width: 75px"></select>');
            paginationNavList = $('<ul class="pagination pull-right" style="margin-top: 0; margin-right: 0"></ul>');
            paginationBar.append(
                $('<div class="col-sm-6 form-inline"></div>').append(
                    $('<span></span>').text('wyświetlaj po '),
                    paginationSizeSelect
                ),
                $('<div class="col-sm-6"></div>').append(
                    $('<nav></nav>').append(paginationNavList)
                )
            );
            paginationSizeSelect.change(function() {
                pageCapacity = parseInt( $(this).val() );
                refresh();
            });
        }
        if(typeof optionalPageSizes === 'undefined') {
            optionalPageSizes = [10, 25, 50, 100, 250, 500, 1000];
        }
        pageCapacity = optionalPageSizes[0];
        pageIndex = 0;
        pageSizes = [];
        for(i = 0; i < optionalPageSizes.length; i++) {
            k = optionalPageSizes[i];
            if(k < 1 || k > data.length)continue;
            pageSizes.push(k);
        }
        if(pageSizes.length === 0) {
            pageSizes.push(data.length);
        }
        pageSizes.sort(util.compareNumbers);

        theadRow = $('<tr></tr>');
        tbody = $('<tbody></tbody>');
        table = $('<table class="table table-striped table-bordered"></table>')
            .append(
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
                th = $('<th style="cursor: pointer"></th>');
                th.append(
                    $('<span></span>').text(definition.label)
                );
                span = $('<span class="pull-right glyphicon"></span>');
                if(i === sortIndex) {
                    th.addClass('active');
                    if(sortAscending) {
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
                        console.log('event.data', event.data);
                        if(event.data.columnIndex === sortIndex) {
                            sortAscending = !sortAscending;
                        }
                        else {
                            sortIndex = event.data.columnIndex;
                            sortAscending = true;
                        }
                        refresh();
                    }
                );
                theadRow.append(th);
            }

            sortedData.sort(
                util.chooseComparator(
                    columnDefinitions[sortIndex].comparator,
                    sortAscending
                )
            );

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
                        $('<td></td>').append( definition.extractor(row) )
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
})();