package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private Map<String, TimeSeries> wordmap = new HashMap<>();
    private TimeSeries count = new TimeSeries();


    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordin = new In(wordsFilename);
        In countin = new In(countsFilename);

        while (wordin.hasNextLine()) {
            String line = wordin.readLine();
            String[] arr = line.split("\t");
            String word = arr[0];
            int year = Integer.parseInt(arr[1]);
            double times = Double.parseDouble(arr[2]);
            if (wordmap.containsKey(word)) {
                wordmap.get(word).put(year, times);
            } else {
                TimeSeries ts = new TimeSeries();
                ts.put(year, times);
                wordmap.put(word, ts);
            }
        }

        while (countin.hasNextLine()) {
            String line = countin.readLine();
            String[] arr = line.split(",");
            int year = Integer.parseInt(arr[0]);
            double counts = Double.parseDouble(arr[1]);
            count.put(year, counts);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        if (this.wordmap.containsKey(word)) {
            TimeSeries all = this.wordmap.get(word);
            for (int year: all.keySet()) {
                if (year >= startYear && year <= endYear) {
                    ts.put(year, all.get(year));
                }
            }
        }
        return ts;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        TimeSeries ts = new TimeSeries();
        if (this.wordmap.containsKey(word)) {
            ts = this.wordmap.get(word);
        }
        return ts;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries ts = new TimeSeries();
        for (int year: this.count.keySet()) {
            ts.put(year, count.get(year));
        }
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries ts = countHistory(word, startYear, endYear);
        if (ts ==  null) {
            return null;
        }
        return ts.dividedBy(this.count);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        TimeSeries ts = countHistory(word);
        if (ts ==  null) {
            return null;
        }
        return ts.dividedBy(this.count);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries ret = new TimeSeries();
        for (String word: words) {
            TimeSeries ts = weightHistory(word, startYear, endYear);
            if (ts != null) {
                ret = ret.plus(ts);
            }
        }
        return ret;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ret = new TimeSeries();
        for (String word: words) {
            TimeSeries ts = weightHistory(word);
            if (ts != null) {
                ret = ret.plus(ts);
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);
        TimeSeries ts = ngm.countHistory("biscuit",1950, 1990);
    }
}
