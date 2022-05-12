package com.khauminhduy.bsontojson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class BsonToJsonLiveTest {

	private static final String DB_NAME = "library";
	private static Datastore datastore;

	@BeforeClass
	public static void setup() {
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.khauminhduy.bsontojson");
		datastore = morphia.createDatastore(new MongoClient(), DB_NAME);
		datastore.ensureIndexes();
		Book book = Book.builder().isbn("isbn").title("title").author("author").cost(3.95)
				.publisher(new Publisher(new ObjectId("fffffffffffffffffffffffa"), "publisher"))
				.publishDate(LocalDateTime.parse("2020-01-01 10:13:32", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).build()
				.addCompanionBooks(Book.builder().isbn("isbn2").build());
		datastore.save(book);
	}

	@AfterClass
	public static void tearDown() {
		datastore.delete(datastore.createQuery(Book.class));
	}

	@Test
	public void givenBsonDocument_whenUsingStandardJsonTransformation_thenJsonDateIsObjectEpochTime() {
		String json;
		try (MongoClient mongoClient = new MongoClient();) {
			MongoDatabase database = mongoClient.getDatabase(DB_NAME);
			Document bson = database.getCollection("Books").find().first();
			json = bson.toJson();
		}

		String expectedJson = "{\"_id\": \"isbn\", " + "\"className\": \"com.khauminhduy.bsontojson.Book\", "
				+ "\"title\": \"title\", " + "\"author\": \"author\", "
				+ "\"publisher\": {\"_id\": {\"$oid\": \"fffffffffffffffffffffffa\"}, " + "\"name\": \"publisher\"}, "
				+ "\"price\": 3.95, " + "\"publishDate\": {\"$date\": 1577848412000}}";

		assertNotNull(json);
		assertEquals(expectedJson, json);

	}

	@Test
	public void givenBsonDocument_whenUsingRelaxedJsonTransformation_thenJsonDateIsObjectIsoDate() {
		String json;
		try (MongoClient mongoClient = new MongoClient();) {
			MongoDatabase database = mongoClient.getDatabase(DB_NAME);
			Document bson = database.getCollection("Books").find().first();
			json = bson.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
		}

		String expectedJson = "{\"_id\": \"isbn\", " + "\"className\": \"com.khauminhduy.bsontojson.Book\", "
				+ "\"title\": \"title\", " + "\"author\": \"author\", "
				+ "\"publisher\": {\"_id\": {\"$oid\": \"fffffffffffffffffffffffa\"}, " + "\"name\": \"publisher\"}, "
				+ "\"price\": 3.95, " + "\"publishDate\": {\"$date\": \"2020-01-01T03:13:32Z\"}}";

		assertNotNull(json);
		assertEquals(expectedJson, json);

	}
	
	@Test
	public void givenBsonDocument_whenUsingCustomJsonTransformation_thenJsonDateIsStringField() {
		String json;
		try(MongoClient mongoClient = new MongoClient();) {
			MongoDatabase database = mongoClient.getDatabase(DB_NAME);
			Document bson = database.getCollection("Books").find().first();
			json = bson.toJson(JsonWriterSettings.builder().dateTimeConverter(new JsonDateTimeConverter()).build());
		}
		
		String expectedJson = "{\"_id\": \"isbn\", " +
        "\"className\": \"com.khauminhduy.bsontojson.Book\", " +
        "\"title\": \"title\", " +
        "\"author\": \"author\", " +
        "\"publisher\": {\"_id\": {\"$oid\": \"fffffffffffffffffffffffa\"}, " +
        "\"name\": \"publisher\"}, " +
        "\"price\": 3.95, " +
        "\"publishDate\": \"2020-01-01T03:13:32Z\"}";
		
		assertNotNull(json);
		assertEquals(expectedJson, json);
	}

}
