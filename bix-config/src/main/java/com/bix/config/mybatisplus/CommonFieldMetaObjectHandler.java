package com.bix.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.bix.config.auth.DateTimeTemplate;
import com.bix.config.mybatisplus.annotations.CreateTime;
import com.bix.config.mybatisplus.annotations.UpdateTime;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


@Slf4j
public class CommonFieldMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        final Object originalObject = metaObject.getOriginalObject();

        setValueByAnnoType(CreateTime.class,originalObject, DateTimeTemplate.getCurrentDate(),metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        final Object originalObject = metaObject.getOriginalObject();

        setValueByAnnoType(UpdateTime.class,originalObject, DateTimeTemplate.getCurrentDate(),metaObject);
    }

    /**
     * 根据指定注解装配指定值
     * @param annoType
     * @param targetObj
     */
    private void setValueByAnnoType(Class<? extends Annotation> annoType, Object targetObj,Object newValue,MetaObject metaObject){
        final Class<?> originalObjectClass = targetObj.getClass();
        final Field[] allFields = FieldUtils.getAllFields(originalObjectClass);
        for(Field f : allFields){
            if(f.isAnnotationPresent(annoType)){
                setFieldValByName(f.getName(), newValue, metaObject);
            }
        }
    }
}
