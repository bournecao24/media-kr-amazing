//package com.kr.media.amazing.component.es.core;
//
//import com.kr.media.amazing.component.es.constant.ESConfig;
//import com.kr.media.amazing.component.es.constant.ESMappingConstant;
//import org.apache.http.Header;
//import org.apache.http.HeaderElement;
//import org.apache.http.HttpHost;
//import org.apache.http.ParseException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.elasticsearch.action.DocWriteRequest;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.support.WriteRequest;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.text.Text;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.rest.RestStatus;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.util.ObjectUtils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//import java.util.Map;
//
//public class ESClientImpl implements ESClient {
//
//    private final static Logger logger = LogManager.getLogger(ESClientImpl.class);
//    private final int SUCCESS_CODE = 200;
//    private final int UPDATE_SUCCESS_CODE = 200;
//    private final int CREATE_SUCCESS_CODE = 201;
//
//    private static final int CONNECT_TIMEOUT_MILLIS = 2000;
//    private static final int SOCKET_TIMEOUT_MILLIS = 4000;
//    private static final int CONNECTION_REQUEST_TIMEOUT_MILLIS = 2000;
//    private static final int MAX_CONN_PER_ROUTE = 300;
//    private static final int MAX_CONN_TOTAL = 300;
//
//    private ESConfig esConfig;
//    private RestHighLevelClient restHighLevelClient;
//
//    public ESClientImpl(ESConfig esConfig) {
//        this.esConfig = esConfig;
//        restHighLevelClient = RestHighLevelClientBuilder.bulider(esConfig);
//
//    }
//
//    public static class RestHighLevelClientBuilder {
//
//        public static RestHighLevelClient bulider(ESConfig esConfig) {
//
//            Boolean useAuth = esConfig.getUseAuth();
//            String host = esConfig.getHost();
//            Integer port = esConfig.getPort();
//
//            if (useAuth == null || !useAuth) {
//                RestClientBuilder restClientBuilder = initRestClientBuilder(host, port);
//                RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//                return restHighLevelClient;
//            }
//
//            String userName = esConfig.getUserName();
//            String userPassword = esConfig.getUserPassword();
//            RestClientBuilder restClientBuilder = initRestClientBuilder(host, port, userName, userPassword);
//            RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
//            return restHighLevelClient;
//
//        }
//
//
//        private static RestClientBuilder initRestClientBuilder(String host, Integer port, String userName, String userPassword) {
//            RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(host, port, "http"))
//                    .setRequestConfigCallback(initRequestConfigCallback())
//                    .setHttpClientConfigCallback(initHttpClientConfigCallback());
//            if (ObjectUtils.isEmpty(userName) && ObjectUtils.isEmpty(userPassword)) {
//                restClientBuilder.setDefaultHeaders(initDefaultHeaders(userName, userPassword));
//            }
//
//            return restClientBuilder;
//        }
//
//        private static RestClientBuilder initRestClientBuilder(String host, Integer port) {
//
//            return initRestClientBuilder(host, port, null, null);
//        }
//
//
//        private static RestClientBuilder.RequestConfigCallback initRequestConfigCallback() {
//
//            return new RestClientBuilder.RequestConfigCallback() {
//                @Override
//                public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
//                    builder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
//                    builder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
//                    builder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
//                    return builder;
//                }
//            };
//        }
//
//        private static RestClientBuilder.HttpClientConfigCallback initHttpClientConfigCallback() {
//
//            return new RestClientBuilder.HttpClientConfigCallback() {
//                @Override
//                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
//                    httpAsyncClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
//                    httpAsyncClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
//                    return httpAsyncClientBuilder;
//                }
//            };
//        }
//
//        private static Header[] initDefaultHeaders(String userName, String userPassword) {
//            Header[] headers = new Header[1];
//            if (ObjectUtils.isEmpty(userName) || ObjectUtils.isEmpty(userPassword)) {
//                return headers;
//            }
//            Header header = new Header() {
//                @Override
//                public String getName() {
//                    return "Authorization";
//                }
//
//                @Override
//                public String getValue() {
//                    String authString = userName + ":" + userPassword;
//                    byte[] authEncBytes = new byte[0];
//                    try {
//                        authEncBytes = Base64.getEncoder().encode(authString.getBytes("utf-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                    String authStringEnc = new String(authEncBytes);
//                    return "Basic " + authStringEnc;
//                }
//
//                @Override
//                public HeaderElement[] getElements() throws ParseException {
//                    return new HeaderElement[0];
//                }
//            };
//            headers[0] = header;
//            return headers;
//        }
//
//    }
//
//    @Override
//    public Boolean saveOrUpdateDocument(String indexName, String type, String indexId, String JasonContent) {
//
//        IndexRequest indexRequest = new IndexRequest(indexName, type, indexId).source(JasonContent, XContentType.JSON);
//        UpdateRequest updateRequest = new UpdateRequest(indexName, type, indexId);
//
//        indexRequest.timeout(TimeValue.timeValueSeconds(3));
//        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        indexRequest.opType(DocWriteRequest.OpType.CREATE);
//
//        updateRequest.doc(indexRequest);
//        updateRequest.docAsUpsert(true);
//
//        try {
//            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
//            RestStatus restStatus = updateResponse.status();
//
//            if (restStatus == null) {
//                return false;
//            }
//
//            if (restStatus.getStatus() == UPDATE_SUCCESS_CODE || restStatus.getStatus() == CREATE_SUCCESS_CODE) {
//                return true;
//            }
//
//        } catch (IOException e) {
//            logger.error("saveOrUpdateDocument error ,indexName:{},type:{},indexId:{},exception:{}", indexName, type, indexId, e);
//            throw new RuntimeException("saveOrUpdateDocument error", e);
//        }
//        return false;
//    }
//
//    @Override
//    public Boolean deleteDocument(String indexName, String type, String indexId) {
//        DeleteRequest deleteRequest = new DeleteRequest(indexName, type, indexId);
//        deleteRequest.timeout(TimeValue.timeValueSeconds(3));
//        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        try {
//            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
//            RestStatus restStatus = deleteResponse.status();
//
//            if (restStatus == null) {
//                return false;
//            }
//
//            if (restStatus.getStatus() == SUCCESS_CODE) {
//                return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public void close() {
//        try {
//            restHighLevelClient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
