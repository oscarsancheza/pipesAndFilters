package com.mcc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Util {
    public static String fromStringToFile(String strContent,String fileName) {
        BufferedWriter bufferedWriter = null;
        String path = "./src/main/java/com/mcc/"+fileName+".txt";
        try {
            File myFile = new File(path);
            if (myFile.exists()) {
                myFile.delete();
            }
            myFile.createNewFile();
            Writer writer = new FileWriter(myFile);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(strContent);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{ if(bufferedWriter != null) bufferedWriter.close(); } catch(Exception ex){ System.err.println(ex.getMessage()); }
        }
        return path;
    }
    public static String fromFileDataToString(String path){
        StringBuilder result = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(path),StandardCharsets.UTF_8)) {
            lines.forEach(l-> {result.append(l);result.append('\n');});
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return result.toString();
    }
}