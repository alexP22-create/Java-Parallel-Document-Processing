import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//encapsulates the info given by the MapTask
public class MapTaskResponse {
    //list of the longest found words
    ArrayList<String> max_words = new ArrayList<String>();
    //dictionary containing the number of characters and how many words have that nr of characters
    HashMap<Integer,Integer> dictionary = new HashMap<Integer, Integer>();

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    String file_name;

    public MapTaskResponse() {
    }

    void print() {
        System.out.print(file_name+ " ");
        for (Map.Entry<Integer, Integer> entry : dictionary.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            System.out.print("{"+key+","+value+"} ");
        }
        for(String word : max_words){
            System.out.print(word+" ");
        }
        System.out.print("\n");
    }
}
