//$(function () {
function timerInit(element) {
//    var element = $('.timer-container');
    var timer_obj = element.find('.timer');
    var start_btn = element.find('.start-timer-btn');
    var pause_btn = element.find('.pause-timer-btn');
    var resume_btn = element.find('.resume-timer-btn');
    var remove_btn = element.find('.remove-timer-btn');

    var hasTimer = false;
    // Init timer start
    start_btn.on('click', function () {
        hasTimer = true;
        timer_obj.timer({
            editable: true
        });
        $(this).hide();
        pause_btn.show();
        remove_btn.show();
    });

    // Init timer resume
    resume_btn.on('click', function () {
        timer_obj.timer('resume');
        $(this).hide();
        pause_btn.show();
        remove_btn.show();
    });

    // Init timer pause
    pause_btn.on('click', function () {
        timer_obj.timer('pause');
        $(this).hide();
        resume_btn.show();
    });

    // Remove timer
    remove_btn.on('click', function () {
        if (confirm('Remove timer?')) {
            hasTimer = false;
            timer_obj.timer('remove');
            $(this).hide();
            start_btn.show();
            pause_btn.hide();
            resume_btn.hide();
        }
    });

    // Additional focus event for this demo
    timer_obj.on('focus', function () {
        if (hasTimer) {
            pause_btn.hide();
            resume_btn.show();
        }
    });

    // Additional blur event for this demo
    timer_obj.on('blur', function () {
        if (hasTimer) {
            pause_btn.show();
            resume_btn.hide();
        }
    });
    
    pause_btn.hide();
    resume_btn.hide();
    remove_btn.hide();
//})();
}