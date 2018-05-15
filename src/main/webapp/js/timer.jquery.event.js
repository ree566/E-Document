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
        $(this).addClass('hidden');
        pause_btn.removeClass('hidden');
        remove_btn.removeClass('hidden');
    });

    // Init timer resume
    resume_btn.on('click', function () {
        timer_obj.timer('resume');
        $(this).addClass('hidden');
        pause_btn.removeClass('hidden');
        remove_btn.removeClass('hidden');
    });

    // Init timer pause
    pause_btn.on('click', function () {
        timer_obj.timer('pause');
        $(this).addClass('hidden');
        resume_btn.removeClass('hidden');
    });

    // Remove timer
    remove_btn.on('click', function () {
        if (confirm('Remove timer?')) {
            hasTimer = false;
            timer_obj.timer('remove');
            $(this).addClass('hidden');
            start_btn.removeClass('hidden');
            pause_btn.addClass('hidden');
            resume_btn.addClass('hidden');
        }
    });

    // Additional focus event for this demo
    timer_obj.on('focus', function () {
        if (hasTimer) {
            pause_btn.addClass('hidden');
            resume_btn.removeClass('hidden');
        }
    });

    // Additional blur event for this demo
    timer_obj.on('blur', function () {
        if (hasTimer) {
            pause_btn.removeClass('hidden');
            resume_btn.addClass('hidden');
        }
    });
//})();
}