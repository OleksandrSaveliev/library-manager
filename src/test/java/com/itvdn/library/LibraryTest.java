package com.itvdn.library;

import com.itvdn.library.entities.Book;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LibraryTest extends BaseTest {
    @Test
    public void getByAuthor() {
        List<Book> books = library.getByAuthor("Kathy Sierra, Bert Bates");
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals(5, book.getId());
        assertEquals("Head First Java", book.getName());
        assertEquals("Kathy Sierra, Bert Bates", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals(2003, book.getYear());
    }

    @Test
    public void getByAuthorPartName() {
        List<Book> books = library.getByAuthor("Sierra");
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals(5, book.getId());
        assertEquals("Head First Java", book.getName());
        assertEquals("Kathy Sierra, Bert Bates", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals(2003, book.getYear());
    }

    @Test
    public void getByName() {
        List<Book> books = library.getByName("Effective Java");
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals(6, book.getId());
        assertEquals("Effective Java", book.getName());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("Programming", book.getGenre());
        assertEquals(2001, book.getYear());
    }

    @Test
    public void getByPartName() {
        List<Book> books = library.getByName("Java");
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(2, books.size());
    }

    @Test
    public void getByGenre() {
        List<Book> books = library.getByGenre("Programming");
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(2, books.size());
    }
}
