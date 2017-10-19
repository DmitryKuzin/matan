import archiver.ShannonFanoArchiver;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String input = "";

        while(!input.equals("exit")) {
            System.out.println("Insert path to read from :");

            input = sc.next();

            if(input.equals("exit")) {
                return;
            }

            File file = new File(input);


            ShannonFanoArchiver.archive(file);

            File zip = new File(file.getPath()+".zip");

            System.out.println("Uncompressed size: "+ file.length()+" bytes" + " | " + (double)file.length()/1024.0/1024.0 + " MB");
            System.out.println("Compressed size: "+ zip.length()+ " bytes"+ " | " + (double)zip.length()/1024.0/1024.0 + " MB");

            System.out.println("uncompressed/compressed: " + (double)file.length()/(double)zip.length());
        }
    }
}
