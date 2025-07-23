package com.itvdn.library.services;

import com.itvdn.library.entities.Book;
import com.itvdn.library.entities.User;
import com.itvdn.library.models.Library;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class LibraryService implements Runnable {
    private final Library library;
    private final User user;

    @Override
    public void run() {
        while (!user.isReadAllDesiredBooks()) {
            Set<Integer> unreadBooks = user.getUnreadBooks();
            Book borrowedBook;

            while (!unreadBooks.isEmpty()) {
                borrowedBook = tryBorrowUnreadBook(unreadBooks);
                if (borrowedBook == null) {
                    logWaitingState();
                    waitBeforeRetry();
                } else {
                    readBook(borrowedBook);
                    returnBook(borrowedBook);
                }
            }
        }
    }

    private void readBook(Book book) {
        logger.info("{} {}. Reading {}", user.getFirstname(), user.getLastname(), book);
        user.readBook(book);
    }

    private void returnBook(Book book) {
        library.returnBook(book);
        logger.info("{} {} <- returning {}", user.getFirstname(), user.getLastname(), book);
    }

    private Book tryBorrowUnreadBook(Set<Integer> unreadBooks) {
        for (int bookId : unreadBooks) {
            Book book = library.borrowBook(bookId);
            if (book != null) return book;
        }
        return null;
    }

    private void logWaitingState() {
        logger.warn("{} {}. There are no book available that I want. I'll wait for a little.",
                user.getFirstname(),
                user.getLastname());

        logger.warn("{} {}. Waiting for books {}, but now available {}",
                user.getFirstname(),
                user.getLastname(),
                user.getUnreadBooks(),
                library.getBooks().stream().map(Book::getId).collect(Collectors.toList()));
    }

    private void waitBeforeRetry() {
        try {
            Thread.sleep(user.getReadingSpeed());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting", e);
        }
    }
}
