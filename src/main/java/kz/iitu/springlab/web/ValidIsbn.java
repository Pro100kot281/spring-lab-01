package kz.iitu.springlab.web;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsbnValidator.class)
public @interface ValidIsbn {
    String message() default "{book.isbn.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}