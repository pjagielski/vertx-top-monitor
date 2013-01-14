var eb = new vertx.EventBus('http://localhost:8080/eventbus');

eb.onopen = function() {

    eb.registerHandler('web.client', function(message) {
        $('.tasks').html(message.tasks);
        $('.cpu').html(message.avgCpu);
        $('.memUsed').html(message.memUsed);
        $('.memFree').html(message.memFree);
        $('.swapUsed').html(message.swapUsed);
        $('.swapFree').html(message.swapFree);
        var tds = message.topProc.map(function(elem) {
            return "<tr><td>"+elem.pid+"</td><td>"+elem.user+"</td><td>"+elem.cpu+"</td>" +
                        "<td>"+elem.mem+"</td><td>"+elem.command+"</td></tr>"
        }).join("")
        $('.processes').html(tds)
    });

}