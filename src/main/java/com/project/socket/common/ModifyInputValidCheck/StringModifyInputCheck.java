package com.project.socket.common.ModifyInputValidCheck;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class StringModifyInputCheck implements ConstraintValidator<StringInputCheck, String> {

  @Override
  public void initialize(StringInputCheck constraintAnnotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.trim().length() > 0;

  }
}
