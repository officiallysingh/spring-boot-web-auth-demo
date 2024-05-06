package com.ksoot.web.auth;

import com.ksoot.web.auth.security.IdentityHelper;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Book", description = " secured APIs")
@RequiredArgsConstructor
public class BooksController {

  private final Map<Integer, Book> books;

  BooksController() {
    this.books = new HashMap<>();
    this.books.put(1, Book.of(1, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone"));
    this.books.put(2, Book.of(2, "J.R.R. Tolkien", "The Lord of the Rings"));
    this.books.put(3, Book.of(3, "George Orwell", "1984"));
  }

//  @PreAuthorize("hasAuthority('ROLE_Books.Read')")
  @GetMapping("/books/{id}")
  public ResponseEntity<Book> getBook(@PathVariable Integer id) {
    return Optional.ofNullable(this.books.get(id))
        .map(book -> ResponseEntity.ok().body(book))
        .orElse(ResponseEntity.notFound().build());
  }

//  @PreAuthorize("!hasAuthority('APPROLE_Books.Read')")
  @GetMapping(path = "/books")
  public ResponseEntity<Collection<Book>> getAllBook() {
    return ResponseEntity.ok(this.books.values());
  }

//  @PreAuthorize("hasAuthority('APPROLE_Books.Write')")
  @PostMapping(path = "/books")
  public Book addBook(@RequestBody Book book) {
    return this.books.put(book.getId(), book);
  }

  @GetMapping(path = "/hello")
  public ResponseEntity<String> sayHello(@AuthenticationPrincipal Principal principal) {
    Authentication authentication = IdentityHelper.getAuthentication();
    return ResponseEntity.ok("Hello " + IdentityHelper.getClaim("unique_name") + " from Downstream stream service");
  }
}
