function initStopWatch() {
    var AppStopwatch = (function () {
        var counter = 1, resetNum = 1,
                $stopwatch = {
                    el: document.getElementById("stopwatch"),
                    container: document.getElementById("time-container"),
                    startControl: document.getElementById("stopwatch-start"),
                    stopControl: document.getElementById("stopwatch-stop"),
                    resetControl: document.getElementById("stopwatch-reset")
                };

        var runClock;

        function displayTime() {
            $stopwatch.container.innerHTML = moment().hour(0).minute(0).second(counter++).format('HH : mm : ss');
        }

        function startWatch() {
            runClock = setInterval(displayTime, 1000);
        }

        function stopWatch() {
            clearInterval(runClock);
        }

        function resetWatch() {
            if (confirm("Reset timer?")) {
                counter = resetNum;
                $stopwatch.container.innerHTML = moment().hour(0).minute(0).second(0).format('HH : mm : ss');
            }
        }

        return {
            startClock: startWatch,
            stopClock: stopWatch,
            resetClock: resetWatch,
            $start: $stopwatch.startControl,
            $stop: $stopwatch.stopControl,
            $reset: $stopwatch.resetControl
        };
    })();

    AppStopwatch.$start.addEventListener("click", function (e) {
        e.preventDefault();
        this.disabled = true;
        AppStopwatch.$stop.disabled = false;
        AppStopwatch.$reset.disabled = true;
        AppStopwatch.startClock();
    }, false);

    AppStopwatch.$stop.addEventListener("click", function (e) {
        e.preventDefault();
        this.disabled = true;
        AppStopwatch.$start.disabled = false;
        AppStopwatch.$reset.disabled = false;
        AppStopwatch.stopClock();
    }, false);

    AppStopwatch.$reset.addEventListener("click", AppStopwatch.resetClock, false);

    AppStopwatch.$stop.disabled = true;

}