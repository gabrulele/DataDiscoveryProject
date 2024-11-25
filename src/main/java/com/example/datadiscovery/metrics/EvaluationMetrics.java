package com.example.datadiscovery.metrics;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import java.util.*;

public class EvaluationMetrics {

    /*
     * Combiniamo rilevanza graduata e percentili, usando i percentili
     * per definire livelli di soglia come:
     *
     * Top 10% → rilevanza 2, 10-20% → rilevanza 1, resto → rilevanza 0.
     */
    public Map<Integer, Integer> assignRelevances(TopDocs topDocs){

        // Estrarre e ordinare gli score
        double[] scores = Arrays.stream(topDocs.scoreDocs)
                .mapToDouble(scoreDoc -> scoreDoc.score)
                .toArray();
        Arrays.sort(scores);

        // Calcolare le soglie per i percentili
        int top10Index = (int) Math.ceil(0.1 * scores.length) - 1; // Top 10%
        int top20Index = (int) Math.ceil(0.2 * scores.length) - 1; // Top 20%

        double top10Threshold = scores[top10Index];
        double top20Threshold = scores[top20Index];

        // Assegna rilevanze graduate in base ai percentili
        Map<Integer, Integer> relevanceMap = new HashMap<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int relevance;
            if (scoreDoc.score >= top10Threshold) {
                relevance = 2; // Molto rilevante
            } else if (scoreDoc.score >= top20Threshold) {
                relevance = 1; // Rilevante
            } else {
                relevance = 0; // Non rilevante
            }
            relevanceMap.put(scoreDoc.doc, relevance);
        }

        // Calcola DCG e IDCG
        return relevanceMap;
    }

    public float averageScore(ScoreDoc[] scoreDocs) {
        List<ScoreDoc> scoreList = List.of(scoreDocs);
        float sum = 0;

        for (ScoreDoc scoreDoc : scoreList) {
            sum += scoreDoc.score;
        }
        return sum / scoreList.size();
    }


    // Calcolo di MRR basato su Feedback Utente
    public float calculateMRR(List<Integer> relevanceRankings, float queryCounter) {
        float sum = 0;

        for(float rank : relevanceRankings){
            sum += 1/rank;
        }

        return (float)(sum / queryCounter);
    }

    // Calcolo di NDCG
    public double calculateNDCG(Map<Integer, Integer> relevanceMap, TopDocs topDocs) {
        double dcg = 0.0;
        double idcg = 0.0;

        // Calcolo DCG
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = topDocs.scoreDocs[i];
            int relevance = relevanceMap.getOrDefault(scoreDoc.doc, 0); // Rilevanza del documento
            dcg += (Math.pow(2, relevance) - 1) / (Math.log(i + 2) / Math.log(2)); // Formula DCG
        }

        // Calcolo IDCG (Ideal DCG) - Ordina le rilevanze in ordine decrescente
        List<Integer> sortedRelevances = new ArrayList<>(relevanceMap.values());
        sortedRelevances.sort(Collections.reverseOrder()); // Ordine decrescente

        for (int i = 0; i < sortedRelevances.size(); i++) {
            int relevance = sortedRelevances.get(i);
            idcg += (Math.pow(2, relevance) - 1) / (Math.log(i + 2) / Math.log(2)); // Formula DCG
        }

        // Normalizzazione: se IDCG è 0, ritorna 0
        return idcg == 0.0 ? 0.0 : dcg / idcg;
    }
}

