package com.itvdn.library;

import com.itvdn.library.models.Library;
import com.itvdn.library.services.LibraryDataService;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
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
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
