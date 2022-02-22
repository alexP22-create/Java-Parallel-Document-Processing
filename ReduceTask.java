import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReduceTask implements Callable<ReduceTaskResponse> {
    private String file_name;
    private ArrayList<MapTaskResponse> map_responses;
    private HashMap<Integer, Integer> dictionary = new HashMap<Integer, Integer>();
    private double rank;
    private int max_length;
    private int nr_apparitions;

    public ReduceTask(String file_name, ArrayList<MapTaskResponse> map_responses) {
        this.file_name = file_name;
        this.map_responses = map_responses;
    }

    public int getFibonacci(int index, int n1, int n2) {
        if (index == 0)
            return n1;
        if(index>0)
            return getFibonacci(index-1, n2, n1 + n2);

        return -1;
    }

    public double process_rank() {

        double value = 0.0;
        int nr_words = 0;

        for(Map.Entry<Integer,Integer> entry: dictionary.entrySet()) {
            value += getFibonacci(entry.getKey()+1, 0, 1) * entry.getValue();
            nr_words += entry.getValue();

            if (entry.getKey() > max_length) {
                max_length = entry.getKey();
                nr_apparitions = entry.getValue();
            }

        }
        value = (double)value / nr_words;
        return value;
    }

    public void combine_dictionaries() {

        for(MapTaskResponse mapTaskResponse : map_responses) {
            for(Map.Entry<Integer,Integer> entry: mapTaskResponse.dictionary.entrySet()) {

                if (dictionary.containsKey(entry.getKey())) {
                    int nr_aparitions = dictionary.get(entry.getKey());
                    nr_aparitions += entry.getValue();
                    dictionary.replace(entry.getKey(), nr_aparitions);
                } else {
                    dictionary.put(entry.getKey(), entry.getValue());
                }
            }
        }

    }

    public void printDictionary() {

        for(Map.Entry<Integer, Integer> entry : dictionary.entrySet()) {
            System.out.print("{"+entry.getKey()+","+entry.getValue()+"} ");
        }
        System.out.print("\n");
    }

    @Override
    public ReduceTaskResponse call() throws Exception {

        //combine all the dictionaries of a file in just one
        combine_dictionaries();

        //process rank value
        rank = process_rank();

        return new ReduceTaskResponse(rank, max_length, nr_apparitions, file_name);
    }
}
