package com.kr.media.amazing.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@ConfigurationProperties("my-profile")
@Validated
public class ProfileProperties {
    @NotEmpty
    private String name;

    @Email
    @NotEmpty
    private String email;

    //配置文件中没有读取到的话就用默认值
    private Boolean handsome = Boolean.TRUE;

}
