package com.kr.media.amazing.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * MQ发送消息封装
 */
@Data
@Builder
public class MQMessageDTO {

    private String type;

    private Object data;

    private Date sendTime;

}
