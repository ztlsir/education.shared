package com.ztlsir.shared.validation;

import com.ztlsir.shared.validation.Impl.ImageCheckImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageCheckImpl.class)
public @interface ImageCheck {
    String message() default "未知错误";

    Class<?>[] groups() default { };  // 约束注解在验证时所属的组别，可分组验证
    Class<? extends Payload>[] payload() default { }; // 约束注解的有效负载
}