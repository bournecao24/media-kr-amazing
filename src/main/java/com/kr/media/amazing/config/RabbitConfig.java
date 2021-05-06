package com.kr.media.amazing.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: shenchengyu
 * @Date: 2020/11/10
 * @Description:
 */
@Slf4j
@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean("hddpOrderConnectionFactory")
    public ConnectionFactory hddpContractConnectionFactory(
            @Value("${rabbitmq.order.host}") String host,
            @Value("${rabbitmq.order.port}") String port,
            @Value("${rabbitmq.order.virtual-host}") String vhost,
            @Value("${rabbitmq.order.username}") String userName,
            @Value("${rabbitmq.order.password}") String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host);
        connectionFactory.setPort(Integer.parseInt(port));
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        return connectionFactory;
    }

    @Bean("hddpOrderRabbitTemplate")
    public RabbitTemplate hddpContractRabbitTemplate(
            @Qualifier("hddpOrderConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean("hddpOrderRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory hddpContractRabbitListenerContainerFactory(
            @Qualifier("hddpOrderConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        RetryInterceptorBuilder builder = RetryInterceptorBuilder.stateless();
        builder.maxAttempts(3);
        builder.backOffOptions(1000, 2.0, 1800000);
        factory.setAdviceChain(builder.build());
        return factory;
    }
}
