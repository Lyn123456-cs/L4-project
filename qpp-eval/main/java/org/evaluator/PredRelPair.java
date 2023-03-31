package org.evaluator;


import org.apache.lucene.analysis.standard.StandardAnalyzer;


/**
 * @author debforit
 */
public class PredRelPair {
    String id;
    String pred;
    String rel;

    public PredRelPair(String id, String pred, String rel) {
        this.id = id;
        this.pred = TrecDocIndexer.analyze(new StandardAnalyzer(), pred);
        this.rel = TrecDocIndexer.analyze(new StandardAnalyzer(), rel);
    }

    public float getSim(int ngramSize) {
        DocVector predvec = new DocVector(pred, ngramSize);
        DocVector refvec = new DocVector(rel, ngramSize);

        return ngramSize > 0 ? predvec.cosineSim(refvec) : predvec.jaccard(refvec);
    }

    @Override
    public String toString() {
        return id + "\t" + pred + "\t" + rel;
    }

}

