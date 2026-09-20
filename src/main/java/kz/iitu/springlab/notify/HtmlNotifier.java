package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("html")
@Order(3)
public class HtmlNotifier implements Notifier {
    private static final Logger log = LoggerFactory.getLogger(HtmlNotifier.class);

    @PostConstruct
    public void init() {
        log.info("LIFECYCLE >> HtmlNotifier (Variant 8) initialized");
    }

    @Override
    public String send(String message) {
        String escapedMessage = message == null ? "" : message
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");

        String result = "<p>" + escapedMessage + "</p>";
        log.info("HTML >> {}", result);
        return result;
    }

    @Override
    public String channel() {
        return "html";
    }
}