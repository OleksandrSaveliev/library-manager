package com.itvdn.library;

import com.itvdn.library.models.Library;
import com.itvdn.library.services.LibraryDataService;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseTest {
    protected static LibraryDataService libraryDataService;
    protected static Library library;

    @BeforeClass
    public static void init() {
        Properties config = new Properties();
        try (InputStream is = BaseTest.class.getClassLoader().getResourceAsStream("config-test.properties")) {
            config.load(is);

            AppContext context = new AppContext();
            context.setConfig(config);

            libraryDataService = new LibraryDataService();
            libraryDataService.setContext(context);
            context.setLibraryDataService(libraryDataService);

            library = new Library();
            library.setContext(context);
            context.setLibrary(library);

            libraryDataService.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
