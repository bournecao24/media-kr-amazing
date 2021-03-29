//package com.kr.media.amazing.component.es.admin;
//
//import com.kr.media.amazing.component.es.constant.ESConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.Header;
//import org.apache.http.HeaderElement;
//import org.apache.http.HttpHost;
//import org.apache.http.ParseException;
//import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
//import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.springframework.util.ObjectUtils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Base64;
//
///**
// * @Author: caozhenlong
// * @Date: 2020-10-30
// * @Description:
// */
//@Slf4j
//public class KrAdminClientImpl implements KrAdminClient {
//
//
//    private Client restHighLevelClient;
//
//    private ESConfig esConfig;
//
//
//
//    @Override
//    public void init() {
//
//        String host = esConfig.getHost();
//        Integer port = esConfig.getPort();
//        Boolean useAuth = esConfig.getUseAuth();
//
//        if (useAuth == null || !useAuth) {
//            restHighLevelClient = new Client(RestClient.builder(new HttpHost(host, port, "http")));
//            return;
//        }
//
//        String userName = esConfig.getUserName();
//        String userPassword = esConfig.getUserPassword();
//
//
//        restHighLevelClient = new RestHighLevelClient(RestClient
//                .builder(new HttpHost(host, port, "http"))
//                .setDefaultHeaders(initDefaultHeaders(userName, userPassword)));
//
//    }
//
//
//
//    @Override
//    public void close() {
//        if (restHighLevelClient != null) {
//            try {
//                restHighLevelClient.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    private Header[] initDefaultHeaders(String userName, String userPassword) {
//
//        Header[] headers = new Header[1];
//
//        if (ObjectUtils.isEmpty(userName) || ObjectUtils.isEmpty(userPassword)) {
//            return headers;
//        }
//
//        Header header = new Header() {
//            @Override
//            public String getName() {
//                return "Authorization";
//            }
//
//            @Override
//            public String getValue() {
//                String authString = userName + ":" + userPassword;
//                byte[] authEncBytes = new byte[0];
//                try {
//                    authEncBytes = Base64.getEncoder().encode(authString.getBytes("utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//                String authStringEnc = new String(authEncBytes);
//                return "Basic " + authStringEnc;
//            }
//
//            @Override
//            public HeaderElement[] getElements() throws ParseException {
//                return new HeaderElement[0];
//            }
//        };
//        headers[0] = header;
//        return headers;
//    }
//
//
//    @Override
//    public void setEsConfig(ESConfig esConfig) {
//
//        this.esConfig = esConfig;
//
//    }
//
//    @Override
//    public Boolean createIndex(String indexName, String type, String mapping) {
//
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
//        createIndexRequest.mapping(type, mapping, XContentType.JSON);
//        Settings settings = Settings.builder()
//                .put("index.number_of_shards", 3)   // 分片数
//                .put("index.number_of_replicas", "2").build();      // 备份数
//        createIndexRequest.settings(settings);
//
//        try {
//            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest);
//            return createIndexResponse.isShardsAcknowledged();
//        } catch (IOException e) {
//            log.error("createindex error ,indexName:{},exception:{}", indexName, e);
//            throw new RuntimeException("createindex error", e);
//        }
//
//    }
//
//
//    @Override
//    public Boolean createMisEntityIndex(String indexName, String type) {
//        return null;
//    }
//
//    @Override
//    public Boolean createMisAuthorIndex(String indexName, String type) {
//        return null;
//    }
//
//    @Override
//    public Boolean createMisAuthorRelateIndex(String indexName, String type) {
//        return null;
//    }
//
//    @Override
//    public Boolean createSnsUserDynamicIndex(String indexName, String type) {
//        return null;
//    }
//
//    @Override
//    public Boolean deleteIndex(String... indexName) {
//        return null;
//    }
//}
