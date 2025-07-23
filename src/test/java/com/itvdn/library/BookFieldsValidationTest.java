package com.itvdn.library;

import com.itvdn.library.entities.Book;
import com.itvdn.library.exceptions.BookValidationException;
import com.itvdn.library.services.LibraryDataService;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class BookFieldsValidationTest {
    @Test
    public void bookFieldValidation() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        Book book = new Book();
        book.setGenre("This is a super loong genre name...");

        Class<LibraryDataService> libraryDataServiceClass = LibraryDataService.class;
        Method validateFieldsMethod = libraryDataServiceClass.getDeclaredMethod("validateFields", Book.class);
        validateFieldsMethod.setAccessible(true);
        LibraryDataService libraryDataService = libraryDataServiceClass.newInstance();

        Exception exception = assertThrows(Exception.class, () -> validateFieldsMethod.invoke(libraryDataService, book));
        assertTrue(exception.getCause() instanceof BookValidationException);
        assertEquals("Filed 'genre' exceeds maximum length", exception.getCause().getMessage());
    }
}
