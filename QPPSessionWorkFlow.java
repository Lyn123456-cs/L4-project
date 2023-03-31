package org.experiments;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Map;

import com.uwyn.jhighlight.fastutil.Hash;
import org.apache.lucene.search.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.evaluator.Evaluator;
import org.evaluator.Metric;
import org.evaluator.PredRelPair;

import org.evaluator.RetrievedResults;
import org.qpp.*;
import org.trec.QueryResult;
import org.trec.TRECQuery;

public class QPPSessionWorkFlow {
    public static void main(String[] args) throws IOException {
//         {'qid':{,
//   'query: bollywood growth,
//   'doc_list':[d1,d2,d3..d10]
//   'doc_sim':[s1,s2,s3...s10]}}
//        PrintStream ps = new PrintStream("data/AvgIDFdata.txt");
//        System.setOut(ps);
        String current_qid = "s1-q1";
        QueryResult queryResult = new QueryResult(); //Map<String,Map<String, Object>>
        HashMap<String, Object> currentQueryResult = new HashMap<String, Object>();
        // {"query": bollywood, "docList":[d1,d2...], "doc_score":[0.1,0.2...]}
        List<String> docList = new ArrayList<>();
        List<Float> docScore = new ArrayList<>();
        List<String> sessQueryIDList = new ArrayList<>();
        String SessQueryId = "";
        int maxRow = 0;
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("data/sessiontrack2014_full.xlsx"));
            // Read 1st sheet from excel(index:0)
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            // Extract the data,
            maxRow = sheet.getLastRowNum();
            // System.out.println("Total lines: " + maxRow);
            // row start from 1, since the first line is title。
            for (int row = 1; row < maxRow+1; row++) {
                int maxRol = sheet.getRow(row).getLastCellNum();
                XSSFCell qidCell = sheet.getRow(row).getCell(8);
                XSSFCell sessionCell = sheet.getRow(row).getCell(1);
                XSSFCell idCell = sheet.getRow(row).getCell(19);
                XSSFCell titleCell = sheet.getRow(row).getCell(21);
                XSSFCell snippetCell = sheet.getRow(row).getCell(20);
                XSSFCell qCell = sheet.getRow(row).getCell(17);
                // check if the cell is null, if yes then continue,
                if (idCell == null | titleCell == null | snippetCell == null) {
                    continue;
                }
//                change the data from XSSFCell to string, and save to list of string
                String query = qCell.toString();
                String snippet = snippetCell.toString();
                String title = titleCell.toString();
                String cluewebid = idCell.toString();
//              cast string of sessionID and queryID to Integer
                sessionCell.setCellType(CellType.STRING);
                Integer sessionid = Integer.parseInt(sessionCell.toString());
                qidCell.setCellType(CellType.STRING);
                Integer qid = Integer.parseInt(qidCell.toString());
//                session-query ID: s1-q1, means the first query of first session
                if (sessionid > 101) {
                    break;
                } else {
                    SessQueryId = "s" + sessionid + "-" + "q" + qid;

                    if (SessQueryId.equals(current_qid)) {
                        PredRelPair tuple = new PredRelPair(cluewebid, title + snippet, query);
                        float sim = tuple.getSim(3);
                        docScore.add(sim);
                        docList.add(cluewebid);
                        currentQueryResult.put("query", query);
                    } else {
                        currentQueryResult.put("docList", docList);
                        currentQueryResult.put("docScore", docScore);
                        sessQueryIDList.add(current_qid);
                        queryResult.addQueryResult(current_qid, currentQueryResult);
                        current_qid = SessQueryId;
                        currentQueryResult = new HashMap<String, Object>();
                        docList = new ArrayList<>();
                        docScore = new ArrayList<>();
                        PredRelPair tuple = new PredRelPair(cluewebid, title + snippet, query);
                        float sim = tuple.getSim(3);
//                    docScoreList.add(sim);
                        docScore.add(sim);
                        docList.add(cluewebid);
                        currentQueryResult.put("query", query);

//                    queryResult.addQueryResult(queryId, currentQueryResult);
//                    System.out.println("queryoutput:" + queryResult.getQueryResult(queryId));
//                System.out.println("output:" + queryId);
                    }
                    }
                }
                queryResult.addQueryResult(current_qid, currentQueryResult);
//            System.out.println("output:" + queryResult.getQueryResult("s2-q3"));
//            Map<String,Object> results = new HashMap<>();
//            results = queryResult.getQueryResult("s1-q1");
//            String query = (String) results.get("query");
//            System.out.println("query:" + query);

            } catch(IOException e){
                throw new RuntimeException(e);
            }
            // Initialized QPP evaluator to use QPP methods
            Settings.init("init.properties");
            QPPEvaluator qppEvaluator = new QPPEvaluator(Settings.getProp(),
                    Settings.getCorrelationMetric(), Settings.getSearcher(), Settings.getNumWanted());
            QPPMethod[] qppMethods = qppEvaluator.qppMethods();

            List<TRECQuery> queries = new ArrayList<>();
            Map<String, Object> results;
            for (int i = 0; i < sessQueryIDList.size(); i++) {
                results = queryResult.getQueryResult(sessQueryIDList.get(i));
                List<Float> docScoreList = (List<Float>) results.get("docScore");
                List<ScoreDoc> scoreDocs = new ArrayList<>();
                Query q = qppEvaluator.makeQuery((String) results.get("query"));
                queries.add(new TRECQuery(q));
                TotalHits th = new TotalHits(10, TotalHits.Relation.EQUAL_TO);

//            String qids = sessQueryIDList.get(i);
//            if(qids.equals("s2-q3") | qids.equals("s2-q4")| qids.equals("s2-q5") | qids.equals("s2-q6") |qids.equals("s2-q7")){
//                System.out.println(qids);
//                System.out.println(docScoreList);
//            }

                for (int j = 0; j < docScoreList.size(); j++) {
                    scoreDocs.add(new ScoreDoc(j, docScoreList.get(j)));
                }
                ScoreDoc[] scoreDocArray = new ScoreDoc[docScoreList.size()];
                scoreDocArray = scoreDocs.toArray(scoreDocArray);
                TopDocs topDocs = new TopDocs(th, scoreDocArray);
                RetrievedResults rr = new RetrievedResults(sessQueryIDList.get(i), topDocs);

//                WIGSpecificity qppMethod = new WIGSpecificity();
                NQCSpecificity qppMethod = new NQCSpecificity();
                float qppEstimate = (float) qppMethod.computeSpecificity(q, rr, topDocs, 10);
                System.out.println(sessQueryIDList.get(i) + " " + qppEstimate);

            }
//
//        ps.close();
        System.out.println("All results saved in the output file.");

    }
}




