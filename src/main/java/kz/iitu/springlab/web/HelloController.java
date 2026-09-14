package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    @GetMapping("/case")
    public CaseResult convertCase(@RequestParam(defaultValue = "hello world") String text) {
        String upper = text.toUpperCase();
        String lower = text.toLowerCase();

        StringBuilder titleCase = new StringBuilder();
        boolean nextTitle = true;
        for (char c : text.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitle = true;
                titleCase.append(c);
            } else if (nextTitle) {
                titleCase.append(Character.toTitleCase(c));
                nextTitle = false;
            } else {
                titleCase.append(Character.toLowerCase(c));
            }
        }

        return new CaseResult(text, upper, lower, titleCase.toString());
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }
    public record Info(String owner, String javaVersion, int cpuCores) { }
    public record CaseResult(String originalText, String upperCase, String lowerCase, String titleCase) { }
}