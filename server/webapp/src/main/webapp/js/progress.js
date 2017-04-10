function createProgressCircle() {
    var div = $('<div></div>').addClass('progress-circle-main');
    for(var i = 1; i <= 12; i++) {
        var clazz = 'progress-circle-small progress-circle' + i;
        div.append(
            $('<div></div>').addClass(clazz)
        );
    }
    return div;
}