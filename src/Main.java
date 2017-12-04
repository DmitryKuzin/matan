import archiver.AbstractArchiver;
import archiver.HaffmanArchiver;
import archiver.ShannonFanoArchiver;
import sun.security.provider.SHA;
import utils.ByteUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String input = "";

        while(!input.equals("exit")) {
            System.out.println("->");

            input = sc.next();


            if(input.equals("exit")) {
                return;
            }

            if(input.equals("-a")) {

                System.out.println("Insert path to read from :");

                input = sc.next();

                File file = new File(input);

                System.out.println("2 sym mode ?");
                input = sc.next();

                boolean twoSymbMode = false;

                if(input.equals("y")) {
                    twoSymbMode = true;
                }


                AbstractArchiver archiver = new ShannonFanoArchiver();
                archiver.archive(file, twoSymbMode);

                String fileExt = "";

                if(twoSymbMode) {
                    fileExt = "2symb.bin";
                }else {
                    fileExt = "1symb.bin";
                }

                String fileWithOutExtension = file.getPath().substring(0, file.getPath().indexOf("."));

                File archieved = new File(fileWithOutExtension+fileExt);

                System.out.println("Uncompressed size: "+ file.length()+" bytes" + " | " + (double)file.length()/1024.0/1024.0 + " MB");
                System.out.println("Compressed size: "+ archieved.length()+ " bytes"+ " | " + (double)archieved.length()/1024.0/1024.0 + " MB");

                System.out.println("uncompressed/compressed: " + (double)file.length()/(double)archieved.length());
            }

            if(input.equals("-dea")) {

                System.out.println("Insert path to read from :");

                input = sc.next();

                File file = new File(input);

                String s = ShannonFanoArchiver.deArchive(file);

                System.out.println(s);
            }


        }
    }
}
