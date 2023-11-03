package com.project.socket.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

    return builder -> {

      // formatter
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

      // deserializers
      builder.deserializers(new LocalDateDeserializer(dateFormatter));
      builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
      builder.deserializers(new LocalTimeDeserializer(timeFormatter));

      // serializers
      builder.serializers(new LocalDateSerializer(dateFormatter));
      builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
      builder.serializers(new LocalTimeSerializer(timeFormatter));
    };
  }
}
