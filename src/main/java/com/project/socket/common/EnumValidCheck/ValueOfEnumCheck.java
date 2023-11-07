package com.project.socket.common.EnumValidCheck;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ValueOfEnumCheck implements ConstraintValidator<EnumCheck, Enum> {

  private EnumCheck enumCheck;

  @Override
  public void initialize(EnumCheck constraintAnnotation) {
    this.enumCheck = constraintAnnotation;
  }

  @Override
  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    boolean result = false;
    Object[] enumValues = this.enumCheck.enumClass().getEnumConstants();
    if (enumValues != null) {
      for (Object enumValue : enumValues) {
        if (value == enumValue) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}