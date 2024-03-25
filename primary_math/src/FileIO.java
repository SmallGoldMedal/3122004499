import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileIO {
   /*
   * 文本的写入
   * */
    public void fileWrite(String s, Path path) throws IOException {
        File file = new File(String.valueOf(path));
        OutputStream f = new FileOutputStream(file,false);//第二个参数为true，不覆盖原来文件的内容
        OutputStreamWriter writer = new OutputStreamWriter(f, StandardCharsets.UTF_8);
        if (s!=null) {
            writer.append(s);
        }
        writer.close();
        f.close();

    }
}

