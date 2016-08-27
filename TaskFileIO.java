import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskFileIO {
    private File file;

    public TaskFileIO(String filepath) throws IOException {
        file = new File(filepath);
        if(!file.exists()) {
            file.createNewFile();
        }
    }

    public ArrayList<String> read() throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        ArrayList<String> list = new ArrayList<>();
        while(scan.hasNextLine()) {
            list.add(scan.nextLine());
        }
        return list;
    }

    public void write(ArrayList<String> list) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        for (int i = 0; i < list.size(); i++) {
            bos.write(toByte(list.get(i)));
            bos.write(13);
        }
        bos.close();
    }

    private static byte[] toByte(String s) throws UnsupportedEncodingException {
        return s.getBytes("utf-8");
    }
}
