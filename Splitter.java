import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

//reads the input file and also splits the read files in fragments
public class Splitter {
    void split() throws IOException {
        File myObj = new File(Tema2.in_file);
        Scanner myReader = new Scanner(myObj);
        Tema2.D = Integer.parseInt(myReader.nextLine());
        Tema2.nr_files = Integer.parseInt(myReader.nextLine());

        //for every read file
        for (int i = 0; i < Tema2.nr_files; i++) {
            String file_name = myReader.nextLine();
            long bytes = Files.size(Paths.get(file_name));

            //create the file fragments
            int offset = 0;
            for (offset = 0; offset < bytes; offset += Tema2.D) {

                long d = 0;
                if (bytes - offset >= 10)
                    d = Tema2.D;
                else
                    d = bytes - offset;

                InputMapTask newInputMapTask = new InputMapTask(file_name, offset, d);
                Tema2.fragments.add(newInputMapTask);
            }
        }
        myReader.close();
    }

    public Splitter() throws IOException {
        split();
    }
}
