package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CallCounterAspect {

    // Потокобезопасная мапа для хранения счетчиков
    private final Map<String, Integer> methodCounts = new ConcurrentHashMap<>();

    // Выполняется перед каждым методом в сервисном слое
    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceLayer()")
    public void countCall(JoinPoint jp) {
        String methodName = jp.getSignature().toShortString();

        // Увеличиваем счетчик для метода. Если метода еще нет в мапе, ставим 1.
        methodCounts.merge(methodName, 1, Integer::sum);
    }

    // Метод для получения статистики
    public Map<String, Integer> getStatistics() {
        return methodCounts;
    }
}