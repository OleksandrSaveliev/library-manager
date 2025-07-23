package com.itvdn.library.entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
