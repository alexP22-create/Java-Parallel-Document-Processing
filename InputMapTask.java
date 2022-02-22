//object to encapsulate the info needed for a MapTask
public class InputMapTask {
    private String file_name;
    private long offset;
    private long D;

    public InputMapTask(String file_name, long offset, long D) {
        this.file_name = file_name;
        this.offset = offset;
        this.D = D;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getD() {
        return D;
    }

    public void setD(long d) {
        D = d;
    }
}
