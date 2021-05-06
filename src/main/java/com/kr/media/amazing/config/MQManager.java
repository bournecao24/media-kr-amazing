package com.kr.media.amazing.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kr.media.amazing.model.MQMessageDTO;
import com.kr.media.amazing.util.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: shenchengyu
 * @Date: 2020/11/19
 * @Description:
 */
@Slf4j
@Component
public class MQManager {

    @Autowired
    @Qualifier("hddpOrderRabbitTemplate")
    private RabbitTemplate rabbitTemplate;


    public void send (String exchange, String routingKey, Object message) {
        log.info("send message: exchange:{}, routingKey:{}, message:{}",
                exchange, routingKey, JSONObject.toJSONString(message, SerializerFeature.WriteMapNullValue));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendOnData (String exchange, String routingKey, Object message) {
        log.info("send message: exchange:{}, routingKey:{}, sendTime={}, type={}, message:{}",
                exchange, routingKey, LocalDateTime.now().toString(), exchange, JSONObject.toJSONString(message, SerializerFeature.WriteMapNullValue));
        rabbitTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSON(MQMessageDTO.builder()
                .data(message)
                .sendTime(LocalDateUtils.currDateTime())
                .type(exchange).build()));
    }

    public void sendOnDataAndContentType (String exchange, String routingKey, Object message) {
        log.info("send message: exchange:{}, routingKey:{}, message:{}",
                exchange, routingKey, JSONObject.toJSONString(message, SerializerFeature.WriteMapNullValue));
        rabbitTemplate.convertAndSend(exchange, routingKey, JSONObject.toJSON(MQMessageDTO.builder()
                        .data(message)
                        .sendTime(LocalDateUtils.currDateTime())
                        .type(exchange).build())
                , message1 -> {
                    message1.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                    return message1;
                });
    }

}
