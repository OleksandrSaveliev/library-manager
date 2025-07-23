package com.itvdn.library.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itvdn.library.AppContext;
import com.itvdn.library.Constants;
import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.BookFieldValidation;
import com.itvdn.library.entities.User;
import com.itvdn.library.entities.Users;
import com.itvdn.library.exceptions.BookValidationException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.*;
import java.io.*;
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

    public List<User> loadUsers() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Users users = (Users) unmarshaller.unmarshal(new File(usersDataPath));
            return users.getUsers();
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            return new ArrayList<>();
        }

    }

    public void saveUsers(List<User> userList) {
        try (FileWriter writer = new FileWriter(usersDataPath)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Users.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Users users = new Users();
            users.setUsers(userList);
            marshaller.marshal(users, writer);
            writer.flush();
        } catch (IOException | JAXBException e) {
            logger.error(e.getMessage());
        }
    }

    private void validateFields(Book book) throws BookValidationException {
        try {
            Class<Book> bookClass = Book.class;
            for (Field field : bookClass.getDeclaredFields()) {
                BookFieldValidation validation = field.getAnnotation(BookFieldValidation.class);
                if (field.isAnnotationPresent(BookFieldValidation.class)) {
                    field.setAccessible(true);
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
