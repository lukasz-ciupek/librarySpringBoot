package pl.sda.library.repository;

import org.springframework.stereotype.Repository;
import pl.sda.library.model.Book;
import pl.sda.library.exception.BookNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    private Set<Book> books = new HashSet<Book>();

    public BookRepository() {
        books.add(new Book(1,"J.R.R. Tolkien", "Władca Pierscieni"));
        books.add(new Book(2,"Jerome David Salinger", "Buszujacy w zbozu"));
        books.add(new Book(3,"J.K. Rowling", "Harry Potter - seria"));
        books.add(new Book(4,"Jane Austen", "Duma i uprzedzenie"));
        books.add(new Book(5,"Joseph Heller", "Paragraf 22"));
        books.add(new Book(6,"Tołstoj Lew", "Anna Karenina"));
        books.add(new Book(7,"Lee Harper", "Zabić drozda"));
        books.add(new Book(8,"Orwell George", "Rok 1984"));
        books.add(new Book(9,"Orwell George", "Folwark zwierzecy"));
        books.add(new Book(10,"Wiliam Szekspir", "Romeo i Julia"));
    }

    private int generateNextId() {
        return books.stream()
                .mapToInt(Book::getId)
                .max().getAsInt() + 1;
    }

    public Optional<Book> borrowBook(String title, LocalDate borrowedTill)
    {
        Optional<Book> any = books.stream()
                .filter(book -> title.equals(book.getTitle()))
                .filter(book -> book.getBorrowedTill() == null)
                .findAny();

        any.ifPresent(book -> book.setBorrowedTill(borrowedTill));
        return any;
    }

    public Optional<Book> borrowBook(Integer id, LocalDate borrowedTill)
    {
        Optional<Book> any = books.stream()
                .filter(book -> id.equals(book.getId()))
                .filter(book -> book.getBorrowedTill() == null)
                .findAny();

        any.ifPresent(book -> book.setBorrowedTill(borrowedTill));
        return any;
    }

    public void returnBook(Integer id)
    {
        books.stream()
                .filter(book -> id.equals(book.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Book not found"))
                .setBorrowedTill(null);
    }

    public Book addBook(String title, String author)
    {
        Book newBook = new Book(generateNextId(),author,title);
        books.add(newBook);
        return newBook;
    }

    public void removeBook(Integer id)
    {
        Book bookToRemove = books.stream()
                .filter(book -> id.equals(book.getId()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Book not found"));

        books.remove(bookToRemove);
    }

    public Set<Book> getBooks(String title)
    {
        if (title == null) {
            return books.stream().collect(Collectors.toSet());
        }

        return books.stream()
                .filter(book -> book.getTitle().equals(title))
                .collect(Collectors.toSet());
    }

}
