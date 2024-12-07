package com.ll.framework.ioc;

import com.ll.framework.ioc.annotations.Bean;
import com.ll.framework.ioc.annotations.Component;
import com.ll.framework.ioc.annotations.Configuration;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext {
    private String basePackage;
    private Reflections reflections;
    private Map<String, BeanDefinition> beanDefinitions;
    private Map<String, Object> beans;

    public ApplicationContext(String basePackage) {
        this.basePackage = basePackage;
    }

    public void init() {
        beans = new HashMap<>();
        reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        initBeanDefinitions();
        createBeans();
    }

    private void createBeans() {
        beanDefinitions.forEach((beanName, beanDefinition) -> {
            createBean(beanDefinition);
        });
    }

    @SneakyThrows
    private void createBean(BeanDefinition beanDefinition) {
        if (beanDefinition.getBeanCreationType() == BeanDefinition.BeanCreationType.COMPONENT) {
            Class<?> cls = beanDefinition.getCls();

            Object bean = cls.getConstructors()[0].newInstance(
                    beanDefinition
                            .getParameterNames()
                            .stream()
                            .map(this::genBean)
                            .toArray()
            );

            beans.put(beanDefinition.getName(), bean);
        } else if (beanDefinition.getBeanCreationType() == BeanDefinition.BeanCreationType.BEAN_FACTORY_METHOD) {
            Method method = beanDefinition.getMethod();

            Object bean = method.invoke(
                    genBean(beanDefinition.getConfigurationBeanName()),
                    Arrays.stream(method.getParameters())
                            .map(parameter -> genBean(parameter.getName()))
                            .toArray()
            );

            beans.put(beanDefinition.getName(), bean);
        }
    }

    public <T> T genBean(String beanName) {
        T bean = (T) beans.get(beanName);

        if (bean != null) {
            return bean;
        }

        createBean(beanDefinitions.get(beanName));

        return (T) beans.get(beanName);
    }

    Set<Class<?>> findComponentClasses() {
        return reflections
                .getTypesAnnotatedWith(Component.class)
                .stream()
                .filter(cls -> !cls.isInterface())
                .collect(Collectors.toSet());
    }

    Set<Method> findBeanFactoryMethods() {
        return reflections
                .getTypesAnnotatedWith(Configuration.class)
                .stream()
                .filter(cls -> !cls.isInterface())
                .flatMap(cls -> Arrays.stream(cls.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

    void initBeanDefinitions() {
        beanDefinitions = Stream
                .concat(
                        findComponentClasses()
                                .stream()
                                .map(cls -> new BeanDefinition(cls)),
                        findBeanFactoryMethods()
                                .stream()
                                .map(method -> new BeanDefinition(method))
                )
                .collect(
                        Collectors.toMap(
                                BeanDefinition::getName,
                                beanDefinition -> beanDefinition
                        )
                );
    }
}