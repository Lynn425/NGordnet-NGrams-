package ngordnet.main;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.TimeSeries;


import java.sql.Time;
import java.util.HashMap;

/**
 * Created by hug.
 */
public class FileReaderDemo {
    public static void main(String[] args) {
        In in = new In("data/ngrams/very_short.csv");

        /* Every time you call a read method from the In class,
         * it reads the next token from the file, assuming it is
         * of the specified type. The In class thinks of the "next"
         * token as whatever follows whitespace. That whitespace
         * may be spaces, tabs, and/or newlines. */

        /* Compare the calls below to the contents of ./ngordnet/main/example_input_file.txt */

        /*
        int firstItemInFile = in.readInt();
        double secondItemInFile = in.readDouble();
        String thirdItemInFile = in.readString();
        String fourthItemInFile = in.readString();
        double fifthItemInFile = in.readDouble();
         */

        HashMap<String, TimeSeries> map = new HashMap<>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String arr[] = line.split("\t");
            String word = arr[0];
            int year = Integer.parseInt(arr[1]);
            double times = Double.parseDouble(arr[2]);
            if (map.containsKey(word)) {
                map.get(word).put(year, times);
            } else {
                TimeSeries ts = new TimeSeries();
                ts.put(year, times);
                map.put(word,ts);
            }
        }

        In in2 = new In("data/ngrams/total_counts.csv");
        TimeSeries map2=  new TimeSeries();
        while (in2.hasNextLine()) {
            String line = in2.readLine();
            String[] arr = line.split(",");
            int year = Integer.parseInt(arr[0]);
            Double counts = Double.parseDouble(arr[1]);
            map2.put(year, counts);
        }


        System.out.println(map);

    }
}
