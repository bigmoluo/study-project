package com.example.studyprojectbacked.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public interface BaseData {

    default <V> V asViewObject(Class<V> clazz, Consumer<V> consumer){
        V v = this.asViewObject(clazz);
        consumer.accept(v);
        return v;
    }

    default <V> V asViewObject(Class<V> clazz){
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            Constructor<V> constructor = clazz.getConstructor();
            V v = constructor.newInstance();
            for (Field declaredField : declaredFields) {
                this.convert(declaredField,v);
            }
            return v;
        } catch (ReflectiveOperationException exception) {
            Logger logger = LoggerFactory.getLogger(BaseData.class);
            logger.error("在VO与DTO转换时出现了一些错误", exception);
            throw new RuntimeException(exception);
        }
    }

    private void convert(Field field, Object vo){
        try {
            Field source = this.getClass().getDeclaredField(field.getName());
            field.setAccessible(true);
            source.setAccessible(true);
            field.set(vo,source.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
    }
}
