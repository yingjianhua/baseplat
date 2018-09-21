package com.irille.core.commons.SetBeans.SetBean.Annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SetBean {

    boolean NotSet() default false;


    String OriginalField() default "";

//    Class CustomFieldStrategy() default ICustomFieldStrategy.class;

    String GetMethod() default "";

    String SetMethod() default "";
}

