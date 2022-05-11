package com.khauminhduy.tagging;

import java.util.List;

import lombok.Data;

@Data
public class Post {

	private String title;

	private String author;

	private List<String> tags;
	
}
