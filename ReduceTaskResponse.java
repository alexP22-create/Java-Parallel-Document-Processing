//class to encapsulate the info given by a ReduceTask
public class ReduceTaskResponse {
    private final double rank;
    private final int max_length;
    private final int max_apparitions;
    private final String file_name;

    public String getFile_name() {
        return file_name;
    }

    public ReduceTaskResponse(double rank, int max_length, int max_apparitions, String file_name) {
        this.rank = rank;
        this.max_length = max_length;
        this.max_apparitions = max_apparitions;
        this.file_name = file_name;
    }

    public double getRank() {
        return rank;
    }

    public int getMax_length() {
        return max_length;
    }

    public int getMax_apparitions() {
        return max_apparitions;
    }
}
