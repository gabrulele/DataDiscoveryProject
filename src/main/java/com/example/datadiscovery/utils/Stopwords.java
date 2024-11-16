package com.example.datadiscovery.utils;

import org.apache.lucene.analysis.CharArraySet;
import java.util.Arrays;

public class Stopwords {
    private CharArraySet stopWords = new CharArraySet(Arrays.asList(
            "a", "an", "the", "and", "or", "of", "in", "on", "at", "for", "with", "by", "to", "from",
            "is", "are", "was", "were", "be", "been", "being", "this", "that", "these", "those",
            "it", "its", "such", "as", "if", "when", "where", "which", "who", "whom", "how",
            "about", "between", "both", "either", "neither", "each", "other", "one", "all",
            "some", "many", "most", "few", "several", "any", "no", "none", "only", "own",
            "same", "so", "than", "too", "very", "dataset", "figure", "table", "data", "values",
            "results", "analysis", "experiment", "experiments", "method", "methods",
            "approach", "approaches", "paper", "study", "studies", "proposed", "shown",
            "presented", "example", "examples", "case", "cases", "mean", "standard",
            "deviation", "comparison", "observed", "given", "obtained", "generated",
            "calculated", "estimated", "simulated", "row", "column", "rows", "columns",
            "header", "headers", "cell", "cells", "value", "total", "average", "min", "max",
            "count", "sum", "median", "mode", "percentage", "rate", "ratio", "index",
            "indices", "figure", "fig", "figures", "table", "tables", "described",
            "description", "below", "above", "following", "previous", "respective",
            "respect", "illustrate", "illustrates", "illustrated", "display", "displays",
            "displayed", "represent", "represents", "represented", "corresponding",
            "note", "notes", "reference", "references", "cited", "citation", "cited-in",
            "footnote", "footnotes", "appendix", "appendices", "supplementary",
            "material", "materials", "supporting", "additional", "further", "see",
            "see-also", "author", "authors", "editors", "journal", "paper", "papers",
            "published", "year", "volume", "issue", "page", "pages", "number", "doi",
            "url", "title", "chapter", "book", "proceedings", "conference", "presented",
            "symposium", "workshop", "series", "edition", "has", "have", "having", "into",
            "under", "over", "more", "less", "two", "three", "new", "using", "based",
            "toward", "towards", "through", "via", "their", "our", "but", "done", "we",
            "can", "could", "would", "will", "may", "might", "do", "does", "did", "find",
            "findings", "show", "shows", "effect", "effects", "model", "models", "use",
            "uses", "evidence", "related", "associated", "impact", "impacts", "implications",
            "role", "roles", "perspective", "perspectives", "review", "reviews", "else", "td", "tr", "\n"
    ), true);

    public CharArraySet getStopWords() {
        return stopWords;
    }

    public void setStopWords(CharArraySet stopWords) {
        this.stopWords = stopWords;
    }
}
