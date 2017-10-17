package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {

    public static void compress(File inputDir, File outputZipFile){
//        outputZipFile.getParentFile().mkdirs();
        String inputDirPath = inputDir.getAbsolutePath();
        byte[] buffer = new byte[1024];

        try (FileOutputStream fileOs = new FileOutputStream(outputZipFile);
             ZipOutputStream zipOs = new ZipOutputStream(fileOs)) {
            String filePath = inputDirPath;
            String entryName = filePath.split("\\\\")[filePath.split("\\\\").length-1];
            ZipEntry ze = new ZipEntry(entryName);
            zipOs.putNextEntry(ze);
            FileInputStream fileIs = new FileInputStream(filePath);
            int len;
            while ((len = fileIs.read(buffer)) > 0) {
                zipOs.write(buffer, 0, len);
            }
            fileIs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
