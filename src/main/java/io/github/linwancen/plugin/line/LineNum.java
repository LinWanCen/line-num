package io.github.linwancen.plugin.line;

import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

public class LineNum {

    private LineNum() {}

    public static int fileLine(@NotNull String path) {
        try (@NotNull FileReader reader = new FileReader(path)) {
            return line(reader);
        } catch (IOException e) {
            return -1;
        }
    }

    public static int strLine(@NotNull String str) {
        try (@NotNull StringReader reader = new StringReader(str)) {
            return line(reader);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int line(@NotNull Reader reader) {
        try (@NotNull LineNumberReader lineNumberReader = new LineNumberReader(reader)) {
            //noinspection ResultOfMethodCallIgnored
            lineNumberReader.skip(Long.MAX_VALUE);
            int lineNumber = lineNumberReader.getLineNumber();
            return lineNumber + 1;
        } catch (IOException e) {
            return -1;
        }
    }
}
