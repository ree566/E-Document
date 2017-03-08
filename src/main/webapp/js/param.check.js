function checkVal() {
    for (var i = 0; i < arguments.length; i++) {
        var arg = arguments[i];
        if (arg == null || arg == "" || arg == -1) {
            return false;
        }
    }
    return true;
}