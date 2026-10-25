package com.example.employeemanagement.config;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private final MongoTemplate mongo;

    public DataSeeder(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public void run(String... args) throws IOException {
        seed("department", "data/departments.json");
        seed("position", "data/positions.json");
    }

    private void seed(String collectionName, String resourcePath) throws IOException {
        MongoCollection<Document> collection = mongo.getCollection(collectionName);
        if (collection.countDocuments() > 0) {
            return;
        }
        String json;
        try (InputStream in = new ClassPathResource(resourcePath).getInputStream()) {
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8).replace("\uFEFF", "");
        }
        List<Document> docs = Document.parse("{\"data\":" + json + "}").getList("data", Document.class);
        collection.insertMany(docs);
        System.out.println("[DataSeeder] Đã nạp " + docs.size() + " document vào collection '" + collectionName + "'.");
    }
}
