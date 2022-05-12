package com.khauminhduy;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoBsonExample {

	public static void main(String[] args) {
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase("myDB");
		MongoCollection<Document> collection = database.getCollection("employees");

		Document document = new Document().append("first_name", "Joe").append("last_name", "Smith")
				.append("title", "Java Developer").append("years_of_service", 3)
				.append("skills", Arrays.asList("java", "spring", "mongodb"))
				.append("manager", new Document().append("first_name", "Sally").append("last_name", "Johanson"));

		collection.insertOne(document);

		Document query = new Document("last_name", "Smith");
		FindIterable<Document> iterable = collection.find(query);
		StreamSupport.stream(iterable.spliterator(), false).forEach(System.out::println);

		System.out.println("-------------------------------------");

		query = new Document("$or", Arrays.asList(new Document("last_name", "Smith"), new Document("first_name", "Joe")));
		iterable = collection.find(query);
		StreamSupport.stream(iterable.spliterator(), false).forEach(System.out::println);

		query = new Document("skills", new Document("$elemMatch", new Document("$eq", "spring")));
		Document update = new Document("$push", new Document("skills", "security"));
		collection.updateMany(query, update);

		query = new Document("years_of_service", new Document("$gt", 0));
		collection.deleteMany(query);

	}

}
