package com.project.socket.common.EnumValidCheck;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.TYPE})
@Constraint(validatedBy = ValueOfEnumCheck.class)
public @interface EnumCheck {

  Class<? extends Enum<?>> enumClass();

  Class[] groups() default {};

  String message() default "잘못된 Enum 타입 입니다.";

  Class<? extends Payload>[] payload() default {};

}
