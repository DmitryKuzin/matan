package utils;

import java.io.*;

public class BookReader {

    public static String readBook(File file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder stringBuilder = new StringBuilder();

        String s;

        while((s = br.readLine())!= null) {
            stringBuilder.append(s);
        }

        return stringBuilder.toString();
    }
}
