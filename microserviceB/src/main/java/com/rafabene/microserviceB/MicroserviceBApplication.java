package com.rafabene.microserviceb;

import java.util.HashMap;
import java.util.Map;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.spring.TracingProducerFactory;
import io.opentracing.util.GlobalTracer;

@SpringBootApplication
public class MicroserviceBApplication {


    @Value("${kafka_URL}")
    private String kafkaURL;

	public static void main(final String[] args) {
		SpringApplication.run(MicroserviceBApplication.class, args);
	}

	public Tracer tracer() {
		return GlobalTracer.get();
	}

	public ProducerFactory<String, String> producerFactory() {
		return new TracingProducerFactory<>(new DefaultKafkaProducerFactory<>(producerConfigs()), tracer());
	}

	public Map<String, Object> producerConfigs() {
		final Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaURL);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// See https://kafka.apache.org/documentation/#producerconfigs for more properties
		return props;
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<String, String>(producerFactory());
	}

}
	