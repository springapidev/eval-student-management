package com.rajaul.studentmanagement.annonations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String PHONE_REGEX = "^(\\+)?(\\d{11}|880\\d{11})$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return false; // Null values Not Allowed (you can change this behavior if needed)
        }
        return phone.matches(PHONE_REGEX);
    }
}
