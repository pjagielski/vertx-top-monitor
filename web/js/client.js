function incrementValue(knob, value, destValue) {
    setTimeout(function(){
        knob.val(value).trigger('change')
        if (value < destValue) {
            incrementValue(knob, value + 1, destValue)
        }
    }, 10);
}
function decrementValue(knob, value, destValue) {
    setTimeout(function(){
        knob.val(value).trigger('change')
        if (value > destValue) {
            decrementValue(knob, value - 1, destValue)
        }
    }, 10);
}
function animateKnobToNewValue(knob, newVal) {
    var oldVal = parseInt(knob.val(), 10);
    newVal = parseInt(newVal, 10);
    if (oldVal < newVal) {
       incrementValue(knob, oldVal + 1, newVal);
    } else {
       decrementValue(knob, oldVal - 1, newVal);
    }
}

$(document).ready(function() {

    var statsSrc = $("#stats-template").html();
    var statsTpl = Handlebars.compile(statsSrc);
    var processSrc = $("#process-template").html();
    var processTpl = Handlebars.compile(processSrc);

    $('.meter').knob();

    var eb = new vertx.EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');

    eb.onopen = function() {

        eb.registerHandler('web.client', function(message) {
            console.log(message)
            var statsHtml = statsTpl(message)
            var processHtml = processTpl(message)
            $('#stats').html(statsHtml)
            $('#processes').html(processHtml)
            animateKnobToNewValue($('.memPerc'), message.memPerc);
            animateKnobToNewValue($('.cpuPerc'), message.avgCpu);
        });

    }

})
