/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import beans.Task;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import handlers.UpdateTaskHandler;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trofimov
 */
public class JsonDeserializerImpl implements JsonDeserializer<Task> {
    
    public JsonDeserializerImpl() {
    }

    @Override
    public Task deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final Gson gson = new Gson();
        // Parse the JsonElement tree here
        final Task task = gson.fromJson(json, Task.class);
        // getting date properties as string from JsonElement and parse them into date object.
        String dateReportedStr = jsonObject.get("start").getAsString();
        String dateComposedStr = jsonObject.get("finish").getAsString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        try {
            //                        DateFormat dflocal = new SimpleDateFormat("yyyy-MM-dd");
            //                        Date news = new DateTime( dateReportedStr).toDate();
            //                        Date newf = new DateTime( dateComposedStr).toDate();
            //
            task.setStart_date(df.parse(dateReportedStr));
            task.setFinish_date(df.parse(dateComposedStr));
        } catch (ParseException ex) {
            Logger.getLogger(UpdateTaskHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return task;
        //          return json == null ? null : new Date(json.getAsLong());
    }
    
}
