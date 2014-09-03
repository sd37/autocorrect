package Algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spandan on 8/31/14.
 */
public class ComputeProb {
    public static Map<String, Integer> computeBigramFreq(List<String> txt_paths) {

        Map<String, Integer> bigram_freq = new HashMap<>();

        for (int i = 0; i < txt_paths.size(); i++) {

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(txt_paths.get(i)));
            } catch (FileNotFoundException e) {
                System.out.println("ERROR:");
                continue;
            }

            assert br != null;
            String line = null;

            try {
                while ((line = br.readLine()) != null) {
                    String new_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();

                    if (new_line.equals(""))
                        continue;

                    String[] parts = new_line.split("\\s+");

                    for (int j = 1; j < parts.length; j++) {
                        String temp_key = parts[j - 1] + " " + parts[j];
                        if (!bigram_freq.containsKey(temp_key))
                            bigram_freq.put(temp_key, 1);
                        else
                            bigram_freq.put(temp_key, bigram_freq.get(temp_key) + 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bigram_freq;
    }
}
