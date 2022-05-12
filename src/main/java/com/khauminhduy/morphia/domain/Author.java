package com.khauminhduy.morphia.domain;

import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Author {

	 @Id
   private String name;
   private List<String> books;
	
}
