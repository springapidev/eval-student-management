package com.rajaul.studentmanagement.annonations;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
@JacksonAnnotationsInside
@JsonSerialize(using = ProtectDataSerializer.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskData {
}