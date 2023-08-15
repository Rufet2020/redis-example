package com.example.redisexample.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer implements RedisSerializer<LocalDateTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public byte[] serialize(LocalDateTime localDateTime) throws SerializationException {
        if (localDateTime == null) {
            return null;
        }
        String formatted = localDateTime.format(formatter);
        return formatted.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public LocalDateTime deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return LocalDateTime.parse(str, formatter);
    }
}
