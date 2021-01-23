package com.kr.media.amazing.component.es.core;

import java.util.List;

public interface ESClient {

    public  void close();

    public Boolean saveOrUpdateDocument(String indexName, String type, String indexId, String JasonContent);

    public Boolean deleteDocument(String indexName, String type, String indexId);

    class ESResult{
        private Long totals;
        private List<String> resultString;

        public Long getTotals() {
            return totals;
        }

        public void setTotals(Long totals) {
            this.totals = totals;
        }

        public List<String> getResultString() {
            return resultString;
        }

        public void setResultString(List<String> resultString) {
            this.resultString = resultString;
        }
    }

}
