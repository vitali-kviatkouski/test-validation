package com.vkviatkouski.sample.context;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodValidator;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import com.vkviatkouski.sample.rest.RestBusinessException;

import java.util.Set;

import javax.validation.Validator;

public class ContentMethodValidationInterceptor extends MethodValidationInterceptor {
    private MethodValidator validator;

    /**
     * Create a new MethodValidationInterceptor using the given JSR-303 Validator.
     * @param validatorFactory the JSR-303 Validator to use
     */
    public ContentMethodValidationInterceptor(Validator validator) {
        super(validator);
        this.validator = validator.unwrap(MethodValidator.class);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class[] groups = determineValidationGroups(invocation);
        Set<MethodConstraintViolation<Object>> result = this.validator.validateAllParameters(
                        invocation.getThis(), invocation.getMethod(), invocation.getArguments(), groups);
        if (!result.isEmpty()) {
            MethodConstraintViolation<Object> vio = result.iterator().next();
            throw new RestBusinessException("STATUS_CODE", "Some message");
        }
        return invocation.proceed();
    }
}
