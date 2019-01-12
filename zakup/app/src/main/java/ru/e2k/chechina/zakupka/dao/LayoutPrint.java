package ru.e2k.chechina.zakupka.dao;

//аннотация что б связать типы из базы и отражение
import java.lang.annotation.*;
//работало некорректно хотела через аннотации сделать настройку

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface LayoutPrint {
    int idLayout();
    String metodCount() default "";
    String metodSetChek() default "";
    String metodSetCount() default "";
}
