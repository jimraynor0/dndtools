package org.toj.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileIoUtils {

    public static Writer getWriter(File out) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(out));
    }

    public static BufferedReader getReader(File wf, String encoding) throws IOException {
        FileInputStream fis = new FileInputStream(wf);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        BufferedReader reader = new BufferedReader(isr);
        return reader;
    }

    public static BufferedReader getReader(File wf) throws IOException {
        FileInputStream fis = new FileInputStream(wf);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader reader = new BufferedReader(isr);
        return reader;
    }

    public static BufferedReader getReader(String filename) throws IOException {
        return getReader(new File(filename));
    }

    public static Writer getWriter(String filename) throws IOException {
        return getWriter(new File(filename));
    }
}
