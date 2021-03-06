package main.java.com.djrapitops.plan.systems.webserver.response;

import main.java.com.djrapitops.plan.utilities.html.Html;

/**
 * @author Rsl1122
 * @since 3.5.2
 */
public class InternalErrorResponse extends ErrorResponse {

    public InternalErrorResponse(Throwable e, String cause) {
        super.setHeader("HTTP/1.1 500 Internal Error");

        super.setTitle("500 Internal Error occurred");

        StringBuilder paragraph = new StringBuilder();
        paragraph.append("Please report this issue here: ");
        paragraph.append(Html.LINK.parse("https://github.com/Rsl1122/Plan-PlayerAnalytics/issues", "Issues"));
        paragraph.append("<br><br>");
        paragraph.append(e).append(" | ").append(cause);

        for (StackTraceElement element : e.getStackTrace()) {
            paragraph.append("<br>");
            paragraph.append("&nbsp;&nbsp;").append(element);
        }
        if (e.getCause() != null) {
            appendCause(e.getCause(), paragraph);
        }

        super.setParagraph(paragraph.toString());
        super.replacePlaceholders();
    }

    private void appendCause(Throwable cause, StringBuilder paragraph) {
        paragraph.append("<br>Caused by: ").append(cause);
        for (StackTraceElement element : cause.getStackTrace()) {
            paragraph.append("<br>");
            paragraph.append("&nbsp;&nbsp;").append(element);
        }
        if (cause.getCause() != null) {
            appendCause(cause.getCause(), paragraph);
        }
    }
}
