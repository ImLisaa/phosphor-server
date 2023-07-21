package net.phosphor.phosphor.api.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class Document {

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private JsonObject jsonObject = new JsonObject();

    public Document() {
        this(new JsonObject());
    }

    public Document(final Path path) {
        this.read(path);
    }

    public Document(final Reader reader) {
        this.read(reader);
    }

    public Document(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Document(final String json) {
        this.jsonObject = GSON.fromJson(json, JsonObject.class);
    }

    public Document(final Object object) {
        this.setJsonObject(object);
    }

    public <T> T get(final String key, final Class<T> clazz) {
        return GSON.fromJson(this.jsonObject.get(key), clazz);
    }

    public <T> T get(final String key, final Type type) {
        return GSON.fromJson(this.jsonObject.get(key), type);
    }

    public Document addIfNotExists(String key, boolean state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public Document addIfNotExists(String key, Number state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public Document addIfNotExists(String key, String state) {
        if (!has(key)) set(key, state);
        return this;
    }

    public <T> T get(final Class<T> clazz) {
        return GSON.fromJson(this.jsonObject, clazz);
    }

    public <T> T get(final Type type) {
        return GSON.fromJson(this.jsonObject, type);
    }

    public Document set(final String key, final Object object) {
        this.jsonObject.add(key, GSON.toJsonTree(object, object.getClass()));
        return this;
    }

    public Document read(final Path path) {
        if (!Files.exists(path)) return this;
        try (final var fileReader = Files.newBufferedReader(path)) {
            this.jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public boolean has(String key) {
        return this.jsonObject.has(key);
    }

    public Document read(final Reader reader) {
        this.jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        return this;
    }

    public Document write(final Path path) {
        try (final var writer = Files.newBufferedWriter(path)) {
            writer.write(GSON.toJson(this.jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Document setJsonObject(final JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    public Document setJsonObject(final Object object) {
        this.jsonObject = GSON.toJsonTree(object).getAsJsonObject();
        return this;
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }

    @Override
    public String toString() {
        return this.jsonObject.toString();
    }
}

