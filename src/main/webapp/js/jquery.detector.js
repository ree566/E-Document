if (!window.jQuery) {
    document.getElementById("not_detect_jquery").innerHTML =
            "Sorry, this page require jquery plugin\
, please check your system environment or contact system administrator.";
    inputs = document.querySelectorAll('input, select, button, a');
    for (index = 0; index < inputs.length; ++index) {
        inputs[index].disabled = true;
    }
}
  