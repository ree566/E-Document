var cookie_disabled_message = 
        "For full functionality of this page it is necessary to enable Cookies.\n" +
        "Here are the instructions how to enable JavaScript in your web browser\n" +
        "http://www.whatarecookies.com/enable.asp";
function are_cookies_enabled() {
    $.cookie('test_cookie', 'cookie_value', {path: '/'});
    return $.cookie('test_cookie') === 'cookie_value';
}