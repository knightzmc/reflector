package me.bristermitten.reflector.examples.json;

import lombok.SneakyThrows;

import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

public class JsonReader {
    private final Scanner scanner;
    private char read;

    public JsonReader(String s) {
        scanner = new Scanner(new StringReader(s));
        scanner.useDelimiter("");
    }

    public JsonReader openObject() {
        assertChar(read(), '{');
        return this;
    }

    public JsonReader closeObject() {
        assertChar(read(), '}');
        return this;
    }

    @SneakyThrows
    private int read() {
        int temp = scanner.next().charAt(0);
        if (temp == -1)
            return read;
        return read = (char) temp;
    }

    public String nextName() {
        scanner.useDelimiter(":");
        String s = scanner.next();
        scanner.useDelimiter("");
        if (scanner.hasNext())
            scanner.next(); //remove the :
        return s;
    }

    public String nextString() {
        StringBuilder buffer = new StringBuilder();
        char read;
        while (true) {
            read = (char) read();
            if (read == ',' || read == '}') break;
            buffer.append(read);
        }
        String s = buffer.toString();
        assertChar(s.charAt(0), '"');
        assertChar(s.charAt(s.length() - 1), '"');
        return s;
    }

    public Number nextNumber() {
        try {
            scanner.useDelimiter("[,|}]");
            String source = scanner.next();
            Number parse = NumberFormat.getInstance().parse(source);
            scanner.useDelimiter("");
            System.out.println("scanner.next() = " + scanner.next());
            return parse;
        } catch (ParseException e) {
            return scanner.nextBigInteger();
        }
    }

    public boolean nextBoolean() {
        return scanner.nextBoolean();
    }

    private void assertChar(int read, char c) {
        this.read = c;
        if (read != c) {
            throw new IllegalStateException("Invalid JSON: Expected: " + c + " Actual: " + (char) read);
        }
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String next() {
        scanner.useDelimiter("[,|}]");
        if (!scanner.hasNext())
            return null;
        String next = scanner.next();
        scanner.useDelimiter("");
        scanner.next(); //remove comma
        return next;

    }
}

