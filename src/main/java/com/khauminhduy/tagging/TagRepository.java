package com.khauminhduy.tagging;

import static java.util.stream.Collectors.toList;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class TagRepository implements Closeable {

	private static final String TAGS_FIELD = "tags";

	private MongoCollection<Document> collection;

	private MongoClient mongoClient;

	public TagRepository() {
		mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase database = mongoClient.getDatabase("blog");
		collection = database.getCollection("posts");
	}

	public List<Post> postsWithAtLeastOneTag(String... tags) {
		// db.posts.find({ "tags" : { $in : ["MongoDB", "Stream API" ] } });
		FindIterable<Document> results = collection.find(Filters.in(TAGS_FIELD, tags));
		return StreamSupport.stream(results.spliterator(), false).map(TagRepository::documentToPost).collect(toList());
	}

	public List<Post> postsWithAllTags(String... tags) {
		// db.posts.find({ "tags" : { $all : ["Java 8", "JUnit 5" ] } });
		FindIterable<Document> results = collection.find(Filters.all(TAGS_FIELD, tags));
		return StreamSupport.stream(results.spliterator(), false).map(TagRepository::documentToPost).collect(toList());
	}

	public List<Post> postsWithoutTags(String... tags) {
		// db.posts.find({ "tags" : { $nin : ["Groovy", "Scala" ] } });
		FindIterable<Document> results = collection.find(Filters.nin(TAGS_FIELD, tags));
		return StreamSupport.stream(results.spliterator(), false).map(TagRepository::documentToPost).collect(toList());
	}

	public boolean addTags(String title, List<String> tags) {
		UpdateResult result = collection.updateOne(new BasicDBObject(DBCollection.ID_FIELD_NAME, title),
				Updates.addEachToSet(TAGS_FIELD, tags));
		return result.getModifiedCount() == 1;
	}

	public boolean removeTags(String title, List<String> tags) {
		UpdateResult result = collection.updateOne(new BasicDBObject(DBCollection.ID_FIELD_NAME, title),
				Updates.pullAll(TAGS_FIELD, tags));
		return result.getModifiedCount() == 1;
	}

	private static Post documentToPost(Document document) {
		Post post = new Post();
		post.setTitle(document.getString(DBCollection.ID_FIELD_NAME));
		post.setAuthor(document.getString("author"));
		post.setTags(document.getList(TAGS_FIELD, String.class));
		return post;
	}

	@Override
	public void close() throws IOException {
		mongoClient.close();
	}

}
