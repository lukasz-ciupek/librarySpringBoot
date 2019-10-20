package pl.sda.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.library.model.Book;
import pl.sda.library.service.OrderService;

import java.util.Optional;
import java.util.Set;

@RestController
public class BookController {

    private final OrderService orderService;

    public BookController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @GetMapping(value = "/books", produces = "application/json")
    public Set<Book> getBooks(@RequestParam(required = false) String title)
    {
        return orderService.getBooks(title);
    }

    @GetMapping(value = "/book/order/{id}", produces = "application/json")
    public ResponseEntity<Book> borrowBook(@PathVariable Integer id)
    {
        Optional<Book> book = orderService.borrowBook(id);

        if (book.isPresent())
        {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/book/add", consumes = "application/json")
    public ResponseEntity addBook(@RequestBody Book book)
    {
        Book addedBook = orderService.addBook(book.getAuthor(),book.getTitle());
        return new ResponseEntity<>(addedBook.getId(), HttpStatus.CREATED);
    }

    @DeleteMapping("/book/remove/{id}")
    public ResponseEntity deleteBook(@PathVariable Integer id)
    {
        Optional<Book> deletedBook = orderService.getBooks(null).stream().filter(book -> id.equals(book.getId())).findAny();

        if (deletedBook.isPresent())
        {
            orderService.removeBook(id);
            return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.notFound().build();
    }

}
