package com.kr.media.amazing.component.es.admin;


import com.kr.media.amazing.component.es.constant.ESConfig;


/**
 * client 负责连接、初始化、关闭、创建索引
 */
public interface KrAdminClient {

    public void close();

    public void init();

    public void setEsConfig(ESConfig esConfig);

    public Boolean createIndex(String indexName, String type, String mapping);



    //创建内容索引
    public Boolean createMisEntityIndex(String indexName, String type);

    //创建作者索引
    public Boolean createMisAuthorIndex(String indexName, String type);

    //创建内容与作者关系索引
    public Boolean createMisAuthorRelateIndex(String indexName, String type);

    //创建用户动态索引
    public Boolean createSnsUserDynamicIndex(String indexName, String type);

    public Boolean deleteIndex(String... indexName);


}
