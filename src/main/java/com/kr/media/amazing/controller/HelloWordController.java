package com.kr.media.amazing.controller;


import com.kr.media.amazing.config.MQManager;
import com.kr.media.amazing.model.MQMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("test")
public class HelloWordController {

    @Autowired
    private MQManager mqManager;

    @GetMapping("hello")
    public String sayHello() {

        MQMessageDTO messageDTO = MQMessageDTO.builder().sendTime(new Date()).data("test").build();


        mqManager.sendOnData("EX_zrpdw_order_change", "datasync.order.change", messageDTO);

        return "Hello World";
    }

}
