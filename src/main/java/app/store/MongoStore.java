
package app.store;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import app.model.Student;
import com.google.gson.Gson;

public class MongoStore {
    static MongoClient client;
    static MongoCollection<Document> collection;
    static Gson gson = new Gson();

    public static void init() {
        client = MongoClients.create("mongodb://localhost:27017"); 
        collection = client.getDatabase("nosqllab").getCollection("ogrenciler");
        collection.drop(); // eski kayıtlar silinir
    }

    public static void put(String key, Student student){
        String json = gson.toJson(student);
        Document doc = Document.parse(json);

        collection.replaceOne(Filters.eq("ogrenciNo", key),
        doc,
        new ReplaceOptions().upsert(true)
        );
    }

    public static Student get(String id) {
        Document doc = collection.find(new Document("ogrenciNo", id)).first();
        return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
    }
}
