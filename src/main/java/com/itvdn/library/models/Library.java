package com.itvdn.library.models;

import com.itvdn.library.AppContext;
import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.User;
import com.itvdn.library.services.LibraryDataService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {
    @Setter
    private AppContext context;
    @Getter
    private List<User> users = new ArrayList<>();
    private final Map<Integer, Book> bookMap = new HashMap<>();

    public void init() {
        LibraryDataService libraryDataService = context.getLibraryDataService();
        List<Book> books = libraryDataService.loadBooks();
        books.forEach(book -> bookMap.put(book.getId(), book));
        users = libraryDataService.loadUsers();
    }

    public Book borrowBook(int id) {
        return bookMap.remove(id);
    }

    public void returnBook(Book book) {
        bookMap.put(book.getId(), book);
    }

    public List<Book> getByAuthor(String author) {
        return bookMap.values().stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author) || b.getAuthor().contains(author))
                .collect(Collectors.toList());
    }

    public List<Book> getByName(String name) {
        return bookMap.values().stream()
                .filter(b -> b.getName().equalsIgnoreCase(name) || b.getName().contains(name))
                .collect(Collectors.toList());
    }

    public List<Book> getByGenre(String genre) {
        return bookMap.values().stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre) || b.getGenre().contains(genre))
                .collect(Collectors.toList());
    }

    public List<Book> getBooks() {
        return new ArrayList<>(bookMap.values());
    }
}
