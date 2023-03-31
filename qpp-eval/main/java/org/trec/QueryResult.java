package org.trec;

import com.uwyn.jhighlight.fastutil.Hash;
import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;
import java.util.*;

public class QueryResult {
    private HashMap<String, HashMap<String, Object>> data;

    public QueryResult(){
        this.data = new HashMap<>();
    }
    public void addQueryResult(String queryId, String query, List<String> docList, List<Double> docScore) {
        HashMap<String, Object> queryResult = new HashMap<>();
        queryResult.put("query", query);
        queryResult.put("doc_list", docList);
        queryResult.put("doc_score", docScore);
        data.put(queryId, queryResult);
    }
    public void addQueryResult(String queryId, HashMap<String, Object> queryResult ) {
        data.put(queryId, queryResult);
    }


    public Map<String, Object> getQueryResult(String queryId) {
        return data.get(queryId);
    }
//public String getQueryText(String sessQueryID){
//        Map<String, Object> queryResult = data.get(sessQueryID);
//        return queryResult.get(query);
//    }
//    public List<String> getKeyOfQueryresult(HashMap<String, HashMap<String, Object>> map){
//        List<String> queryTextList = new ArrayList<>();
//        for(String sessQueryID : map.keySet()){
//            getQueryText(sessQueryID);
//        }
//    }


    }

