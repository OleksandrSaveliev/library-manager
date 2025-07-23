package com.itvdn.library.entities;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlAttribute(name = "id")
    private int id;
    @XmlElement(name = "first_name")
    private String firstname;
    @XmlElement(name = "last_name")
    private String lastname;
    @XmlElement(name = "reading_speed")
    private int readingSpeed;
    private Set<Integer> borrowedBooks = new HashSet<>();
    private Set<Integer> desiredBooks = new HashSet<>();

    public boolean isReadAllDesiredBooks() {
        return desiredBooks.isEmpty();
    }

    public void setDesiredBooks(List<Book> books) {
        for (Book book : books) {
            desiredBooks.add(book.getId());
        }
    }

    public Set<Integer> gerUnreadBooks() {
        return desiredBooks;
    }

    public void readBook(Book book) {
        desiredBooks.remove(book.getId());
        borrowedBooks.add(book.getId());
        try {
            Thread.sleep(readingSpeed);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        borrowedBooks.remove(book.getId());
    }
}
