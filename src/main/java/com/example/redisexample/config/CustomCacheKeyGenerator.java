package com.example.redisexample.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class CustomCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder keyBuilder = new StringBuilder(method.getName());
        for (Object param : params) {
            keyBuilder.append("_").append(param.toString());
        }
        return keyBuilder.toString();
    }

}
