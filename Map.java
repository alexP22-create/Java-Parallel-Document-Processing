import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Map {
    private final ExecutorService executorService;
    private final String file_in;
    private int D;

    public Map(ExecutorService executorService, String file_in, int D){
        this.file_in = file_in;
        this.executorService = executorService;
        this.D = D;
    }

    public HashMap<String, ArrayList<MapTaskResponse>> executeMapTasks() throws ExecutionException, InterruptedException {
        ArrayList<MapTask> tasks = new ArrayList<MapTask>();

        //create tasks
        for(InputMapTask p : Tema2.fragments) {
            MapTask task = new MapTask(p.getFile_name(), p.getOffset(), p.getD());
            tasks.add(task);
        }

        //dictionary for the results of the map tasks, organised by the file names
        HashMap<String, ArrayList<MapTaskResponse>> responses = new HashMap<String, ArrayList<MapTaskResponse>>();

        //start the threads
        for(MapTask task : tasks) {
            Future<MapTaskResponse> threadResponse = executorService.submit(task);
            MapTaskResponse response = threadResponse.get();

            //add it in the returning dictionary
            if (responses.containsKey(response.file_name)) {
                //add the response to the associated list
                ArrayList<MapTaskResponse> list = responses.get(response.file_name);
                list.add(response);
                responses.replace(response.file_name,  list);
            } else {
                //the list for this text file has one element until now
                ArrayList<MapTaskResponse> list = new ArrayList<MapTaskResponse>();
                list.add(response);
                responses.put(response.file_name, list);
            }
        }

        //close executor service
        shutdown();

        //result
        return responses;
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
