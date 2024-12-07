package com.ll.framework.ioc;

import com.ll.standard.util.Ut;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class BeanDefinition {
    public enum BeanCreationType {
        COMPONENT, BEAN_FACTORY_METHOD
    }

    @Getter
    private String name;
    @Getter
    private Class<?> cls;
    @Getter
    private Method method;

    public BeanDefinition(Class<?> cls) {
        this.cls = cls;
        this.name = Ut.str.lcFirst(cls.getSimpleName());
    }

    public BeanDefinition(Method method) {
        this.cls = method.getReturnType();
        this.name = method.getName();
        this.method = method;
    }

    public List<String> getParameterNames() {
        return Arrays
                .stream(
                        cls.getConstructors()[0].getParameters()
                )
                .map(Parameter::getName)
                .toList();

    }

    public BeanCreationType getBeanCreationType() {
        return method == null
                ? BeanCreationType.COMPONENT
                : BeanCreationType.BEAN_FACTORY_METHOD;
    }

    public String getConfigurationBeanName() {
        return getBeanCreationType() == BeanCreationType.BEAN_FACTORY_METHOD
                ? Ut.str.lcFirst(method.getDeclaringClass().getSimpleName())
                : null;
    }
}
