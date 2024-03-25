import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

public class FileIOTest {

    @Test
    public void fileWrite() {
        FileIO fileIO = new FileIO();
        Path path = Paths.get("textfile/Exercises.txt");
        try {
            fileIO.fileWrite(" ", path);
        } catch (Exception e) {
            fail("IO错误");
        }

        path = Paths.get("textfile/Exercises.txt");
        try {
            fileIO.fileWrite("uhasifhiuasfhuoasf", path);
        } catch (Exception e) {
            fail("IO错误");
        }
    }
}