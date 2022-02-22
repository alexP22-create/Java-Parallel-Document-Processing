import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tema2 {
    static int D;
    static int nr_workers;
    static int nr_files;
    static String in_file;
    static String out_file;
    //list of the information about every document fragment
    static ArrayList<InputMapTask> fragments = new ArrayList<>();

    static void print_fragments(){
        for (InputMapTask fragment : fragments) {
            System.out.println(fragment.getFile_name() + " " + fragment.getOffset() + " " + fragment.getD());
        }
    }

    static void write_results(ArrayList<ReduceTaskResponse> responses) throws IOException {
        FileWriter fw = new FileWriter(out_file);

        for(ReduceTaskResponse response : responses) {
            String line = "";

            //eliminate the path before the file name
            String[] words = response.getFile_name().split("/");
            line += words[words.length - 1];
            line += ",";

            String rank = String.format("%.2f", response.getRank());
            line += rank;
            line += ",";
            line += response.getMax_length();
            line += ",";
            line += response.getMax_apparitions();

            line += "\n";
            fw.write(line);
        }
        fw.close();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }

        nr_workers = Integer.parseInt(args[0]);
        in_file = args[1];
        out_file = args[2];

        //split documents in fragments
        Splitter splitter = new Splitter();

        //map
        ExecutorService executorService1 = Executors.newFixedThreadPool(nr_workers);
        Map map = new Map(executorService1, in_file, D);
        HashMap<String, ArrayList<MapTaskResponse>> map_results = map.executeMapTasks();

        //reduce
        ExecutorService executorService2 = Executors.newFixedThreadPool(nr_workers);
        Reduce reduce = new Reduce(map_results, executorService2);
        ArrayList<ReduceTaskResponse> responses = reduce.ExecuteReduceTasks();

        //sort array based on the rank of the elements
        responses.sort(new Comparator<ReduceTaskResponse>() {
            @Override
            public int compare(ReduceTaskResponse t1, ReduceTaskResponse t2) {
                return Double.compare(t2.getRank(), t1.getRank());
            }
        });

        //final
        write_results(responses);
    }
}
