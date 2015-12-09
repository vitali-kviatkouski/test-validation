package com.vkviatkouski.sample.context;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.lang.annotation.Annotation;

import javax.validation.Validator;

public class ContentMethodValidationPostProcessor extends ProxyConfig implements BeanPostProcessor,
        BeanClassLoaderAware, Ordered, InitializingBean {
    private Class<? extends Annotation> validatedAnnotationType = Validated.class;

    private Validator validator;

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private Advisor advisor;

    /**
     * Set the JSR-303 Validator to delegate to for validating methods.
     * <p>
     * Default is the default ValidatorFactory's default Validator.
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public int getOrder() {
        // This should run after all other post-processors, so that it can just add
        // an advisor to existing proxies rather than double-proxy.
        return HIGHEST_PRECEDENCE;
    }

    public void afterPropertiesSet() {
        Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
        Advice advice = new ContentMethodValidationInterceptor(this.validator);
        this.advisor = new DefaultPointcutAdvisor(pointcut, advice);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (bean instanceof AopInfrastructureBean) {
            // Ignore AOP infrastructure such as scoped proxies.
            return bean;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (AopUtils.canApply(this.advisor, targetClass)) {
            if (bean instanceof Advised) {
                ((Advised) bean).addAdvisor(this.advisor);
                return bean;
            } else {
                ProxyFactory proxyFactory = new ProxyFactory(bean);
                // Copy our properties (proxyTargetClass etc) inherited from ProxyConfig.
                proxyFactory.copyFrom(this);
                proxyFactory.setProxyTargetClass(true);
                proxyFactory.addAdvisor(this.advisor);
                return proxyFactory.getProxy(this.beanClassLoader);
            }
        } else {
            // This is not a repository.
            return bean;
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }
}
