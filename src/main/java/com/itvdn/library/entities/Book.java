package com.itvdn.library.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book implements Cloneable {
    private int id;
    private String name;
    private String author;
    @BookFieldValidation(maxLength = 15)
    private String genre;
    private int year;

    @Override
    protected Book clone() throws CloneNotSupportedException {
        return (Book) super.clone();
    }
}
