package HA;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class jsonHelper {
    static File folder=new File("config");
    public static void setFolder(File file){
        folder=file;
    }

    public static String JsonReads(String id) {
        File file = new File(folder,id + ".json");
        if (file.exists() && file.canExecute() && file.canRead()) {
            return toString(file);
        }
        return null;
    }

    public static String toString(File input) {
        InputStream is;
        BufferedReader reader;
        String temp;
        StringBuilder buffer = new StringBuilder();
        try {
            is = Files.newInputStream(input.toPath());
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((temp = reader.readLine()) != null) {
                buffer.append(temp);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void creatFile(String id, String inf) {
        File file = new File(folder,id + ".json");
        if(file.exists())file.delete();
        try {
            file.createNewFile();
            FileOutputStream s = new FileOutputStream(file);
            OutputStreamWriter w = new OutputStreamWriter(s, StandardCharsets.UTF_8);
            w.append(inf);
            w.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
