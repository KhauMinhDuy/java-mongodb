package com.khauminhduy;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoExample {

	public static void main(String[] args) {
		try (MongoClient mongoClient = new MongoClient("localhost", 27017);) {

			DB database = mongoClient.getDB("myMongoDb");

			getCollectionNames(mongoClient);

			database.createCollection("customers", null);

			// create data
			DBCollection dbCollection = database.getCollection("customers");
			BasicDBObject document = new BasicDBObject();
			document.put("name", "DuyKhau");
			document.put("company", "MWG");
			dbCollection.insert(document);

			// update data
			BasicDBObject query = new BasicDBObject();
			query.put("name", "DuyKhau");
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("name", "KhauMinhDuy");
			BasicDBObject updateObject = new BasicDBObject();
			updateObject.put("$set", newDocument);
			dbCollection.update(query, updateObject);

			// read data
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("name", "KhauMinhDuy");
			DBCursor cursor = dbCollection.find(searchQuery);
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}

			// delete data
			BasicDBObject deleteQuery = new BasicDBObject();
			deleteQuery.put("name", "KhauMinhDuy");
			dbCollection.remove(deleteQuery);
		}
	}

	private static void getCollectionNames(MongoClient mongoClient) {
		// method old
		List<String> databaseNames = mongoClient.getDatabaseNames();
		databaseNames.forEach(System.out::println);

		// method new
//		System.out.println();
//		MongoIterable<String> listDatabaseNames = mongoClient.listDatabaseNames();
//		StreamSupport.stream(listDatabaseNames.spliterator(), false).forEach(System.out::println);
	}

}
