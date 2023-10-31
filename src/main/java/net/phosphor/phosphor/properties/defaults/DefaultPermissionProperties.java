package net.phosphor.phosphor.properties.defaults;

import com.google.gson.JsonParser;
import net.phosphor.phosphor.api.json.Document;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author : Lisa
 * @created : 30. Okt.. 2023 | 18:07
 * @contact : @imlisaa_ (Discord)
 * <p>
 * You are not allowed to modify or make changes to
 * this file without permission.
 **/
public class DefaultPermissionProperties {

    public static Document get() {
        Document document = new Document();
        document.addIfNotExists("operators", new JSONArray());
        return document;
    }
}
