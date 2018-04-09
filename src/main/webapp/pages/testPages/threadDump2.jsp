<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintStream,
         java.lang.management.ManagementFactory,
         java.lang.management.ThreadInfo,
         java.lang.management.ThreadMXBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quick and Dirty JSP Thread Report</title>
    </head>
    <body>
        <%!
            private static ThreadMXBean thMxBean
                    = ManagementFactory.getThreadMXBean();

            private static String getTaskName(long id, String name) {
                if (name == null) {
                    return Long.toString(id);
                }
                return id + " (" + name + ")<br>";
            }
        %><%
            final int STACK_DEPTH = 20;
            boolean contention = thMxBean.isThreadContentionMonitoringEnabled();
            long[] threadIds = thMxBean.getAllThreadIds();
            out.println("Process Thread Dump: <br/>");
            out.println(threadIds.length + " active threads<br/>");
            for (long tid : threadIds) {
                ThreadInfo info = thMxBean.getThreadInfo(tid, STACK_DEPTH);
                if (info == null) {
                    out.println("  Inactive");
                    continue;
                }
                out.println(" <br/><br/>Thread "
                        + getTaskName(info.getThreadId(), info.getThreadName()));
                Thread.State state = info.getThreadState();
                out.println("  State: " + state);
                out.println("  <br/>Blocked count: " + info.getBlockedCount());
                out.println("  <br/>Waited count: " + info.getWaitedCount());
                if (contention) {
                    out.println("  <br/>Blocked time: " + info.getBlockedTime());
                    out.println(" <br/> Waited time: " + info.getWaitedTime());
                }
                if (state == Thread.State.WAITING) {
                    out.println("  Waiting on " + info.getLockName());
                } else if (state == Thread.State.BLOCKED) {
                    out.println("  Blocked on " + info.getLockName());
                    out.println("  Blocked by <br/>"
                            + getTaskName(info.getLockOwnerId(),
                                    info.getLockOwnerName()));
                }
                out.println("  <br/>Stack: ");
                for (StackTraceElement frame : info.getStackTrace()) {
                    out.println("    " + frame.toString());
                }
            }%>
    </body>
</html>
