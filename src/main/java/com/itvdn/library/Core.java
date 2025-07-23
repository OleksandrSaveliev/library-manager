package com.itvdn.library;

import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.User;
import com.itvdn.library.models.Library;
import com.itvdn.library.services.LibraryDataService;
import com.itvdn.library.services.LibraryService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class Core {
    private Properties config;
    private Library library;
    private LibraryDataService libraryDataService;

    public void init(String fileName) {
        try {
            config = new Properties();
            config.load(new FileReader(fileName));

            AppContext context = new AppContext();
            context.setConfig(config);

            libraryDataService = new LibraryDataService();
            context.setLibraryDataService(libraryDataService);
            libraryDataService.setContext(context);

            library = new Library();
            context.setLibrary(library);
            library.setContext(context);

            libraryDataService.init();
            library.init();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void start() {
        List<User> users = library.getUsers();
        List<Book> books = library.getBooks();

        for (User user : users) {
            user.setDesiredBooks(books);
            LibraryService libraryService = new LibraryService(library, user);
            Thread thread = new Thread(libraryService, "User " + user.getId());
            thread.start();
        }
    }
}
