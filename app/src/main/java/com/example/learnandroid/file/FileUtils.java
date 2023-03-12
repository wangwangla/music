package com.example.learnandroid.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 10:41
 */
public class FileUtils {
    protected File file;

    public FileUtils (String fileName) {
        this.file = new File(fileName);
    }

    public FileUtils (File file) {
        this.file = file;
    }

    public String path () {
        return file.getPath().replace('\\', '/');
    }

    public String name () {
        return file.getName();
    }

    public String extension () {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return name.substring(dotIndex + 1);
    }

    public String nameWithoutExtension () {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return name;
        return name.substring(0, dotIndex);
    }

    public String pathWithoutExtension () {
        String path = file.getPath().replace('\\', '/');
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex == -1) return path;
        return path.substring(0, dotIndex);
    }


    public InputStream read () {
        try {
            return new FileInputStream(file);
        } catch (Exception ex) {
            if (file.isDirectory()){
                throw new RuntimeException();
            }
        }
        return null;
    }

    public BufferedInputStream read (int bufferSize) {
        return new BufferedInputStream(read(), bufferSize);
    }

    public Reader reader () {
        return new InputStreamReader(read());
    }

    public Reader reader (String charset) {
        InputStream stream = read();
        try {
            return new InputStreamReader(stream, charset);
        } catch (UnsupportedEncodingException ex) {
            try {
                stream.close();
            } catch (IOException e) {

            }
        }
        return null;
    }

    public BufferedReader reader (int bufferSize) {
        return new BufferedReader(new InputStreamReader(read()), bufferSize);
    }

    public String readString (String charset) {
        StringBuilder output = new StringBuilder();
        InputStreamReader reader = null;
        try {
            if (charset == null)
                reader = new InputStreamReader(read());
            else
                reader = new InputStreamReader(read(), charset);
            char[] buffer = new char[256];
            while (true) {
                int length = reader.read(buffer);
                if (length == -1) break;
                output.append(buffer, 0, length);
            }
        } catch (IOException ex) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }


    public void writeString (String string, boolean append, String charset) {
        Writer writer = null;
        try {
            writer = writer(append, charset);
            writer.write(string);
        } catch (Exception ex) {
//            throw new GdxRuntimeException("Error writing file: " + file + " (" + type + ")", ex);
        } finally {
//            StreamUtils.closeQuietly(writer);
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Writer writer (boolean append, String charset) {
        try {
            FileOutputStream output = new FileOutputStream(file, append);
            if (charset == null)
                return new OutputStreamWriter(output);
            else
                return new OutputStreamWriter(output, charset);
        } catch (IOException ex) {

        }
        return null;
    }

}
