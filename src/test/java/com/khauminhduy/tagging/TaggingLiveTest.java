package com.khauminhduy.tagging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class TaggingLiveTest {

	private TagRepository repository = new TagRepository();
	private static Data data;

	@BeforeClass
	public static void setup() {
		data = new Data();
		try {
			data.create();
		} catch (Exception e) {

		}
	}

	@Test
	public void givenTagRepository_whenPostsWithAtLeastOneTagMongoDB_then3Results() {
		List<Post> results = repository.postsWithAtLeastOneTag("MongoDB");
		results.forEach(System.out::println);

		assertEquals(3, results.size());
		results.forEach(post -> {
			assertTrue(post.getTags().contains("MongoDB"));
		});
	}

	@Test
	public void givenTagRepository_whenPostsWithAtLeastOneTagMongoDBJava8_then4Results() {
		List<Post> results = repository.postsWithAtLeastOneTag("MongoDB", "Java 8");
		results.forEach(System.out::println);

		assertEquals(4, results.size());
		results.forEach(post -> {
			assertTrue(post.getTags().contains("MongoDB") || post.getTags().contains("Java 8"));
		});
	}

	@Test
	public void givenTagRepository_whenPostsWithAllTagsMongoDB_then3Results() {
		List<Post> results = repository.postsWithAllTags("MongoDB");
		results.forEach(System.out::println);

		assertEquals(3, results.size());
		results.forEach(post -> {
			assertTrue(post.getTags().contains("MongoDB"));
		});
	}

	@Test
	public void givenTagRepository_whenPostsWithAllTagsMongoDBJava8_then2Results() {
		List<Post> results = repository.postsWithAllTags("MongoDB", "Java 8");
		results.forEach(System.out::println);

		assertEquals(1, results.size());
		results.forEach(post -> {
			assertTrue(post.getTags().contains("MongoDB"));
			assertTrue(post.getTags().contains("Java 8"));
		});
	}

	@Test
	public void givenTagRepository_whenPostsWithoutTagsMongoDB_then1Result() {
		List<Post> results = repository.postsWithoutTags("MongoDB");
		results.forEach(System.out::println);

		assertEquals(1, results.size());
		results.forEach(post -> {
			assertFalse(post.getTags().contains("MongoDB"));
		});
	}

	@Test
	public void givenTagRepository_whenPostsWithoutTagsMongoDBJava8_then0Results() {
		List<Post> results = repository.postsWithoutTags("MongoDB", "Java 8");
		results.forEach(System.out::println);

		assertEquals(0, results.size());
		results.forEach(post -> {
			assertFalse(post.getTags().contains("MongoDB"));
			assertFalse(post.getTags().contains("Java 8"));
		});
	}

	@Test
	public void givenTagRepository_whenAddingRemovingElements_thenNoDuplicates() {
		boolean result = repository.addTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
		assertTrue(result);

		result = repository.addTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
		assertFalse(result);

		List<Post> postsAfterAddition = repository.postsWithAllTags("jUnit", "jUnit5");
		assertEquals(1, postsAfterAddition.size());
		postsAfterAddition.forEach(post -> {
			assertTrue(post.getTags().contains("jUnit"));
			assertTrue(post.getTags().contains("jUnit5"));
		});

		long count = postsAfterAddition.get(0).getTags().stream().filter(e -> e.equals("jUnit5")).count();
		assertEquals(1, count);

		result = repository.removeTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
		assertTrue(result);

		result = repository.removeTags("Post 1", Arrays.asList("jUnit", "jUnit5"));
		assertFalse(result);

		List<Post> postsAfterDeletion = repository.postsWithAllTags("jUnit", "jUnit5");
		assertEquals(0, postsAfterDeletion.size());
		postsAfterDeletion = repository.postsWithAtLeastOneTag("jUnit");
		assertEquals(0, postsAfterDeletion.size());
		postsAfterDeletion = repository.postsWithAtLeastOneTag("jUnit5");
		assertEquals(0, postsAfterDeletion.size());
	}

}
