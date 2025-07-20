package com.itvdn.library.models;

import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {
    private final List<User> users = new ArrayList<>();
    private final Map<Integer, Book> bookMap = new HashMap();

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

    public List<Book> getByGenre(String genre) {
        return bookMap.values().stream()
                .filter(b -> b.getGenre().equalsIgnoreCase(genre) || b.getGenre().contains(genre))
                .collect(Collectors.toList());
    }

    public List<Book> getBooks() {
        return new ArrayList<>(bookMap.values());
    }
}
