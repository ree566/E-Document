<%@ page language="java" pageEncoding="UTF-8"%>
<script>
    if (!window.jQuery) {
        inputs = document.querySelectorAll('input, select, button, a');
        for (index = 0; index < inputs.length; ++index) {
            inputs[index].disabled = true;
        }
    }

    window.onload = function () {
        var d = new Date();
        var n = d.getFullYear();
        document.getElementById("endYear").innerHTML = n;
    };
</script>
<div id="footer" style="color:#777;text-align:center">
    <p>研華科技 版權所有 © 1983-<font id="endYear"></font>Advantech Co., Ltd. All Rights Reserved</p> 
</div>