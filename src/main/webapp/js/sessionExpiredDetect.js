//http://guriddo.net/?topic=redirect-to-logout-on-session-expired
$(document).ajaxError(function (event, jqxhr, settings, exception) {

    if (exception == 'Unauthorized' || exception == 'Forbidden') {

        // Prompt user if they'd like to be redirected to the login page
        var redirect = confirm("表單已經逾期，跳轉到登入畫面?");

        // If the answer is yes
        if (redirect) {

            // Redirect
            window.location = "${root}";
        }
    } 
});

