package app.store;

import com.mongodb.client.*;
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

        collection.drop();

        for (int i = 0; i < 10000; i++) {
            String id = "2025" + String.format("%06d", i);
            Student s = new Student(id, "Ad Soyad " + i, "Bilgisayar");
            collection.insertOne(Document.parse(gson.toJson(s)));
        }

        // Debug: gerçekten yazıldı mı?
        System.out.println("[Mongo] count=" + collection.countDocuments());
        System.out.println("[Mongo] sample=" + collection.find(new Document("ogrenciNo", "2025000001")).first());
    }

    public static Student get(String id) {
        try {
            Document doc = collection.find(new Document("ogrenciNo", id)).first();
            return doc != null ? gson.fromJson(doc.toJson(), Student.class) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
