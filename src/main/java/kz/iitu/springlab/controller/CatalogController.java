package kz.iitu.springlab.controller;

import kz.iitu.springlab.service.CatalogService;
import org.springframework.aop.support.AopUtils;
import org.springframework.web.bind.annotation.*;
import kz.iitu.springlab.aspect.CallCounterAspect;
import java.util.List;
import java.util.Map;






@RestController
public class CatalogController {

    private final CatalogService catalogService;
    private final CallCounterAspect callCounterAspect;

    public CatalogController(CatalogService catalogService, CallCounterAspect callCounterAspect) {
        this.catalogService = catalogService;
        this.callCounterAspect = callCounterAspect;
    }

    // Эндпоинт для проверки прокси (Task 4.1)
    @GetMapping("/api/lab4/proxy")
    public Map<String, String> proxyInfo() {
        return Map.of(
                "className", catalogService.getClass().getName(),
                "superClass", catalogService.getClass().getSuperclass().getSimpleName(),
                "isAopProxy", String.valueOf(AopUtils.isAopProxy(catalogService)),
                "isCglib", String.valueOf(AopUtils.isCglibProxy(catalogService))
        );
    }

    @GetMapping("/api/lab4/item/{id}")
    public String getItemById(@PathVariable long id) {
        return catalogService.findById(id);
    }

    @GetMapping("/api/lab4/items")
    public List<String> getItems(@RequestParam(defaultValue = "5") int limit) {
        return catalogService.findAll(limit);
    }

    @DeleteMapping("/api/lab4/item/{id}")
    public String deleteItem(@PathVariable long id) {
        return catalogService.remove(id);
    }

    // Эндпоинт для теста self-invocation (Task 4.3)
    @GetMapping("/api/lab4/remove-twice/{id}")
    public String removeTwice(@PathVariable long id) {
        return catalogService.removeTwice(id);
    }

    @GetMapping("/api/lab4/stats")
    public Map<String, Integer> getMethodStatistics() {
        return callCounterAspect.getStatistics();
    }
}