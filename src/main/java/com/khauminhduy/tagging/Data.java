package com.khauminhduy.tagging;

import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Data {

	private DBCollection dbCollection;

	public void create() {
		try (MongoClient mongoClient = new MongoClient("localhost", 27017);) {

			DB database = mongoClient.getDB("blog");

			dbCollection = database.getCollection("posts");

			BasicDBObject document = new BasicDBObject();
			document.put("_id", "Post 1");
			document.put("author", "DuyKhau");
			document.put("tags", Arrays.asList("MongoDB"));
			dbCollection.insert(document);

			document.put("_id", "Post 2");
			document.put("author", "DuyKhau");
			document.put("tags", Arrays.asList("MongoDB"));
			dbCollection.insert(document);

//			
			document.put("_id", "Post 3");
			document.put("author", "DuyKhau");
			document.put("tags", Arrays.asList("MongoDB", "Java 8"));
			dbCollection.insert(document);

			document.put("_id", "Post 4");
			document.put("author", "DuyKhau");
			document.put("tags", Arrays.asList("Java 8"));
			dbCollection.insert(document);
		}
	}


}
