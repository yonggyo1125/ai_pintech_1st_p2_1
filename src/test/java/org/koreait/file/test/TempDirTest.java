package org.koreait.file.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class TempDirTest {

    @TempDir
    private static File tempDir;

    @Test
    void test1() {
        String path = tempDir.getAbsolutePath();
        System.out.println(path);
    }

    @AfterAll
    static void destroy() {
        tempDir.delete();
        System.out.println(tempDir.exists());
    }
}
