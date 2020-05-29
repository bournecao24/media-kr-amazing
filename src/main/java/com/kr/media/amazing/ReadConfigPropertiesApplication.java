package com.kr.media.amazing;

import com.kr.media.amazing.entity.LibraryProperties;
import com.kr.media.amazing.entity.ProfileProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 * 通过@ConfigurationProperties读取并与 bean 绑定
 */
@SpringBootConfiguration
@EnableConfigurationProperties(ProfileProperties.class)
public class ReadConfigPropertiesApplication implements InitializingBean {

    private final LibraryProperties library;
    private final ProfileProperties profileProperties;

    public ReadConfigPropertiesApplication(LibraryProperties library, ProfileProperties profileProperties) {
        this.library = library;
        this.profileProperties = profileProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReadConfigPropertiesApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println(profileProperties.toString());
        System.out.println(library.getLocation());
        System.out.println(library.getBooks());
    }


}
