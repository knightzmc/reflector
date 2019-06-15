package me.bristermitten.reflector.examples.json;

import java.io.StringWriter;

public class JsonWriter {
    private final StringWriter tempWriter = new StringWriter();

    public JsonWriter openObject() {
        tempWriter.append('{');
        return this;
    }

    public JsonWriter closeObject() {
        tempWriter.append('}');
        return this;
    }

    public JsonWriter name(String name) {
        tempWriter.append('"').append(name).append('"').append(':');
        return this;
    }

    public JsonWriter value(String value) {
        tempWriter.append('"').append(value).append('"').append(',');
        return this;
    }

    public JsonWriter writeNull() {
        tempWriter.append("null").append(',');
        return this;
    }

    public JsonWriter value(Number value) {
        tempWriter.append(value.toString()).append(',');
        return this;
    }

    public JsonWriter value(boolean value) {
        tempWriter.append(value ? "true" : "false").append(',');
        return this;
    }

    public String complete() {
        return tempWriter.toString().replaceAll(",(?=\\s*?[}\\]])", "");
    }
}
