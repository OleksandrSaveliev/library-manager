package com.itvdn.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itvdn.library.models.Library;
import com.itvdn.library.services.LibraryDataService;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

@Getter
@Setter
public class AppContext {
    private Properties config;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Library library;
    private LibraryDataService libraryDataService;
}
