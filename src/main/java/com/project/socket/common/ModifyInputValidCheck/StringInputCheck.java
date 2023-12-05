package com.project.socket.common.ModifyInputValidCheck;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = StringModifyInputCheck.class)
public @interface StringInputCheck {

  String message() default "Invalid string type input value";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
