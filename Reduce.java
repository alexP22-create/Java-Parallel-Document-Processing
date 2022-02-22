import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Reduce {
    HashMap<String, ArrayList<MapTaskResponse>> map_responses;
    ExecutorService executorService;

    public Reduce(HashMap<String, ArrayList<MapTaskResponse>> map_responses,
                  ExecutorService executorService) {
        this.map_responses = map_responses;
        this.executorService = executorService;
    }

    public ArrayList<ReduceTaskResponse> ExecuteReduceTasks() throws ExecutionException, InterruptedException {
        ArrayList<ReduceTask> tasks = new ArrayList<ReduceTask>();
        ArrayList<ReduceTaskResponse> responses = new ArrayList<ReduceTaskResponse>();

        //create the tasks
        for(Map.Entry<String, ArrayList<MapTaskResponse>> entry : map_responses.entrySet()) {
            ReduceTask reduceTask = new ReduceTask(entry.getKey(), entry.getValue());
            tasks.add(reduceTask);
        }

        //start threads
        for(ReduceTask task : tasks) {
            Future<ReduceTaskResponse> threadResponse = executorService.submit(task);
            ReduceTaskResponse response = threadResponse.get();
            responses.add(response);
        }

        //close executor service
        shutdown();

        return responses;
    }

    public void shutdown() throws InterruptedException {
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}
