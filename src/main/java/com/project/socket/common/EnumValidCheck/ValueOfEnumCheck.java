package com.project.socket.common.EnumValidCheck;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumCheck implements ConstraintValidator<EnumCheck, String> {

  private EnumCheck enumCheck;

  @Override
  public void initialize(EnumCheck constraintAnnotation) {
    this.enumCheck = constraintAnnotation;
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      boolean result = false;
      Enum<?>[] enumValues = this.enumCheck.enumClass().getEnumConstants();
      if (enumValues != null) {
        for (Object enumValue : enumValues) {
          return value.equals(enumCheck.toString());
        }
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

}
