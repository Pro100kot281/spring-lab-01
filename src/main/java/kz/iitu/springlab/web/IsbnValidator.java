package kz.iitu.springlab.web;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.trim().isEmpty()) {
            return true;
        }


        String cleanIsbn = value.replaceAll("[\\s-]", "");


        if (cleanIsbn.length() != 13 || !cleanIsbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(cleanIsbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        // Если остаток от деления на 10 равен 0, то ISBN правильный
        return sum % 10 == 0;
    }
}