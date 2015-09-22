package com.example.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数不允许为空时，请添加此注释
 */
@SuppressWarnings("unused")
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD,ElementType.PARAMETER,ElementType.LOCAL_VARIABLE,ElementType.FIELD})
@interface NotNull {}
