package basics;

import utils.BookReader;
import utils.ZipArchiver;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

//        Scanner sc = new Scanner(System.in);
//
//        String input = "";
//
//        while(!input.equals("exit")) {
//            System.out.println("Insert path to read from :");
//
//            input = sc.next();
//
//            if(input.equals("exit")) {
//                return;
//            }
//
//            File file = new File(input);
//
//            File zip = new File(file.getPath()+".zip");
//            ZipArchiver.compress(file,zip);
//
//            System.out.println("Uncompressed size: "+ file.length()+" bytes" + " | " + (double)file.length()/1024.0/1024.0 + " MB");
//            System.out.println("Compressed size: "+ zip.length()+ " bytes"+ " | " + (double)zip.length()/1024.0/1024.0 + " MB");
//
//            System.out.println("uncompressed/compressed: " + (double)file.length()/(double)zip.length());
//
//            EntropySource entropySource = new EntropySource(BookReader.readBook(file));
//
//            System.out.println("HU->" + Calc.entropy(entropySource,1));
//            System.out.println("HU->" + Calc.entropy(entropySource,2));
//        }

    }

}
