package com.ksoot.web.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@ToString
public class Book {

  private int id;

  private String author;

  private String title;
}
