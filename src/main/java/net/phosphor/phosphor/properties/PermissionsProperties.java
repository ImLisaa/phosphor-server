package net.phosphor.phosphor.properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.phosphor.phosphor.api.json.Document;
import net.phosphor.phosphor.properties.defaults.DefaultPermissionProperties;
import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author : Lisa
 * @created : 29. Okt.. 2023 | 20:59
 * @contact : @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/

public class PermissionsProperties {

    private final Document document;
    private final Path path;
    private JsonArray jsonArray;

    public PermissionsProperties() {
        Document loadedDocument;
        this.path = Path.of("permissions.json");

        if (Files.notExists(this.path)) {
            loadedDocument = DefaultPermissionProperties.get();
            loadedDocument.write(this.path);
        } else {
            loadedDocument = new Document(this.path);
        }
        this.document = loadedDocument;
        this.jsonArray = getProperty("operators", JsonArray.class);
    }

    public Document getDocument() {
        return this.document;
    }

    public <T> T getProperty(String property, Class<T> clazz) {
        return this.document.get(property, clazz);
    }

    public void setProperty(String property, Object value) {
        this.document.set(property, value);
    }

    public void updateDocument() {
        this.document.write(this.path);
    }

    public void reload() {
        this.document.read(this.path);
        this.jsonArray = null;
        this.jsonArray = getProperty("operators", JsonArray.class);
    }

    public JsonArray getJsonArray() {
        return this.jsonArray;
    }
}
