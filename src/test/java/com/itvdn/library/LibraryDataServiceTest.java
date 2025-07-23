package com.itvdn.library;

import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class LibraryDataServiceTest extends BaseTest {
    @Test
    public void loadDataTest() {
        List<Book> books = libraryDataService.loadBooks();
        assertFalse(books.isEmpty());
        assertEquals(6, books.size());

        List<User> users = libraryDataService.loadUsers();
        assertFalse(users.isEmpty());
        assertEquals(5, users.size());
    }

    @Test
    public void loadDataBadBathTest() throws NoSuchFieldException, IllegalAccessException {
        Field booksDataPath = libraryDataService.getClass().getDeclaredField("booksDataPath");
        booksDataPath.setAccessible(true);
        String currentValue = (String) booksDataPath.get(libraryDataService);

        booksDataPath.set(libraryDataService, "bad/path");
        List<Book> books = libraryDataService.loadBooks();
        assertTrue(books.isEmpty());

    }
}