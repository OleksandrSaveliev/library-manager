package com.itvdn.library.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itvdn.library.AppContext;
import com.itvdn.library.Constants;
import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.BookFieldValidation;
import com.itvdn.library.exceptions.BookValidationException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public class LibraryDataService {
    @Setter
    private AppContext context;
    private String booksDataPath;
    private String usersDataPath;

    public void init() {
        Properties config = context.getConfig();
        booksDataPath = getRequiredProperty(config, Constants.BOOKS_DATA_PATH);
        usersDataPath = getRequiredProperty(config, Constants.USERS_DATA_PATH);
    }

    public List<Book> loadBooks() {
        Gson gson = context.getGson();
        try (FileReader reader = new FileReader(booksDataPath)) {
            Type type = new TypeToken<List<Book>>() {
            }.getType();

            List<Book> books = gson.fromJson(reader, type);
            for (Book book : books) {
                validateFields(book);
            }

            return books;
        } catch (IOException | BookValidationException e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveBooks(List<Book> books) {
        Gson gson = context.getGson();
        try (FileWriter writer = new FileWriter(booksDataPath)) {
            String json = gson.toJson(books);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void validateFields(Book book) throws BookValidationException {
        try {
            Class<Book> bookClass = Book.class;
            for (Field field : bookClass.getDeclaredFields()) {
                BookFieldValidation validation = field.getAnnotation(BookFieldValidation.class);
                if (field.isAnnotationPresent(BookFieldValidation.class)) {
                    Object fieldValue = field.get(book);
                    if (fieldValue instanceof String) {
                        String value = (String) fieldValue;
                        if (value.length() > validation.maxLength()) {
                            throw new BookValidationException("Filed '" + field.getName() + "' exceeds maximum length");
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new BookValidationException(e);
        }
    }

    private String getRequiredProperty(Properties config, String key) {
        String value = config.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config file.");
        }
        return value;
    }
}
