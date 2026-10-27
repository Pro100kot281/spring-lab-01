package kz.iitu.springlab.web.page;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Values every HTML page (including the error page) needs, e.g. the footer. */
@ControllerAdvice
public class GlobalModelAdvice {

    @Value("${app.owner:unknown}")
    private String owner;

    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("appOwner", owner);
    }
}
