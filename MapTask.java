import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class MapTask implements Callable<MapTaskResponse> {
    private final String file;
    private final long offset;
    private final long D;
    private long end_file;

    public MapTask(String file, long offset, long d) {
        this.file = file;
        this.offset = offset;
        D = d;
    }

    @Override
    public MapTaskResponse call() throws Exception {
        MapTaskResponse response = new MapTaskResponse();

        String deliminators = ";:/?˜\\.,><‘[]{}()!@#$%ˆ&- +’'=*”| /\t/\n/\r";

        //the size of the file
        end_file = Files.size(Paths.get(file));

        //read from file only D bytes
        FileReader location = new FileReader(file);
        BufferedReader myReader = new BufferedReader(location);
        StringBuilder sb = new StringBuilder();

        //character before the start of the first valid word in the sequence
        char before_seq = ' ';

        if(offset >= 1) {
            myReader.skip(offset - 1);
            before_seq = (char)myReader.read();
        } else {
            //move the position to the offset
            myReader.skip(offset);
        }

        boolean need_valid_before_seq  = true;

        for (int i = 0; i < D; i++) {
            //don't read the last character from a file because it s the terminator
            if (offset + i == end_file)
                break;
            int c = myReader.read();

            //when we reached the first word for the current seq stop searching for the character before it
            if (deliminators.indexOf(c) == -1)
                need_valid_before_seq = false;
            if (need_valid_before_seq)
                before_seq = (char)c;

            sb.append((char)c);
        }

        String str = sb.toString();
        StringTokenizer st = new StringTokenizer(str, deliminators);
        int max = 0;
        boolean first = true;

        //create dictionary and find the longest words
        while (st.hasMoreTokens()) {
            StringBuilder word = new StringBuilder(st.nextToken());

            //convention if the thread starts in the middle of one word and isn't the first thread
            if (first && offset >= 1) {
                //if the char before the part of the word in the current thread is not a deliminator drop the word
                if (deliminators.indexOf(before_seq) == -1) {
                    first = false;
                    continue;
                }
            }

            //convention if the thread finishes in the middle of one word and isn't the last thread
            if(!st.hasMoreTokens() && offset + D < end_file && deliminators.indexOf(str.charAt(str.length()-1)) == -1) {
                char c = (char)myReader.read();
                int nr_new_chars = 1;
                while (deliminators.indexOf(c) == -1 && offset + D + nr_new_chars <= end_file) {
                    word.append(c);
                    c = (char)myReader.read();
                    nr_new_chars++;
                }
            }

            if(word.length() > max) {
                //reset max words
                response.max_words.clear();

                max = word.length();
                response.max_words.add(word.toString());
            } else
                if (word.length() == max) {
                    response.max_words.add(word.toString());
                }

            if (response.dictionary.containsKey(word.length())) {
                //increase the nr of apparitions
                int nr_apparitions = 0;
                nr_apparitions = response.dictionary.get(word.length()) + 1;
                response.dictionary.replace(word.length(),  nr_apparitions);
            } else {
                //appears just 1 time
                response.dictionary.put(word.length(), 1);
            }
        }
        myReader.close();

        response.setFile_name(file);
        return response;
    }
}
