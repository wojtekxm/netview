var notify = {};
(function(){
    notify.success = success;
    notify.danger = danger;

    function success(parentSelector, message) {
        f(parentSelector, message, 'panel-success');
    }
    function danger(parentSelector, message) {
        f(parentSelector, message, 'panel-danger');
    }
    function f(parentSelector, message, panelClass) {
        var d = $('<div class="panel center-block"></div>')
            .addClass(panelClass)
            .append(
                $('<div class="panel-heading"></div>').text(message)
            );
        d.hide();
        d.fadeIn(100, function() {
            d.click(function() {
                d.fadeOut(0, end);
            });
            setTimeout( function() {
                d.fadeOut(1000, end);
            }, 2000 );
        });
        function end() {
            d.remove();
        }
        $(parentSelector).append(d);
    }
})();