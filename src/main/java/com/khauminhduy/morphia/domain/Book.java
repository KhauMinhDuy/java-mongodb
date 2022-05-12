package com.khauminhduy.morphia.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.annotations.Validation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity("Books")
@Indexes({ @Index(fields = @Field("title"), options = @IndexOptions(name = "book_title")) })
@Validation("{price: {$gt: 0}}")
@NoArgsConstructor
@Data
public class Book {

	@Id
	private String isbn;

	@Property
	private String title;

	private String author;

	@Embedded
	private Publisher publisher;

	@Property("price")
	private double cost;

	@Reference
	private Set<Book> companionBooks;

	@Property
	private LocalDateTime publishDate;

	public Book(String isbn, String title, String author, double cost, Publisher publisher) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.cost = cost;
		this.publisher = publisher;
		this.companionBooks = new HashSet<>();
	}

	public Book addCompanionBooks(Book book) {
		if (companionBooks != null) {
			this.companionBooks.add(book);
		}
		return this;
	}

}
