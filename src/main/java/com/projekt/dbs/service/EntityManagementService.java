package com.projekt.dbs.service;

import com.projekt.dbs.entities.*;
import com.projekt.dbs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("entityManagementService")
public class EntityManagementService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookBorrowRepository bookBorrowRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    BookReturnRepository bookReturnRepository;

    @Autowired
    BookRatingRepository bookRatingRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    AuthorRepository authorRepository;

    public EntityManagementService() {
    }

    // <--------------------------- BOOKS ------------------------------->//

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void addBook(Book book) {
        if (book == null) {
            System.err.println("Book is null");
        } else {
            bookRepository.save(book);
        }
    }

    public void removeBook(Book book) {
        bookRepository.delete(book);
    }

    public void reduceAvailableCopies(Integer isbn) {
        bookRepository.reduceAvailableCopies(isbn);
    }

    public void increaseAvailableCopies(Integer isbn) {
        bookRepository.increaseAvailableCopies(isbn);
    }


    // <--------------------------- USERS ------------------------------->//

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public User searchById(Integer id) {
        if (id == null) {
            return null;
        } else {
            return userRepository.searchById(id);
        }
    }

    public Integer getIdByEmail(String email) {
        if (email.isEmpty() || email == null) {
            return null;
        } else {
            return userRepository.getIdByEmail(email);
        }
    }

    public User searchByEmail(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return null;
        } else {
            return userRepository.searchByEmail(filterText);
        }
    }

    public void addUser(User user) {
        if (user == null) {
            System.err.println("User is null");
        } else {
            userRepository.save(user);
        }
    }


    // <--------------------------- BORROWS ------------------------------->//

    public void addBookBorrowRequest(BookBorrow bookBorrow) {
        if (bookBorrow.getBorrower() == null) {
            System.err.println("Null user");
        } else if (bookBorrow.getBook() == null) {
            System.out.println(bookBorrow.getBorrower().toString());
            System.err.println("Null book");
        } else {
            bookBorrowRepository.save(bookBorrow);
        }
    }

    public boolean isNotDuplicate(Integer isbn, Integer userId) {
        if (bookBorrowRepository.isDuplicate(isbn, userId)) {
            return false;
        } else
            return true;
    }

    public List<BookBorrow> findPendingBookBorrows() {
        if (bookBorrowRepository.findPendingBookBorrows().isEmpty()) {
            return null;
        } else {
            return bookBorrowRepository.findPendingBookBorrows();
        }
    }

    public List<BookBorrow> findUsersBookBorrows(Integer id) {
        if (bookBorrowRepository.findUsersBookBorrows(id).isEmpty()) {
            return null;
        } else {
            return bookBorrowRepository.findUsersBookBorrows(id);
        }
    }

    public List<BookBorrow> findUsersReturnedBookBorrows(Integer id) {
        if (bookBorrowRepository.findUsersReturnedBookBorrows(id).isEmpty()) {
            return null;
        } else {
            return bookBorrowRepository.findUsersReturnedBookBorrows(id);
        }
    }

    public void deleteBookBorrow(Long bookBorrowId) {
        if (bookBorrowRepository.existsById(bookBorrowId)) {
            bookBorrowRepository.deleteById(bookBorrowId);
        }
    }

    public List<BookBorrow> findActiveBookBorrows() {
        if (bookBorrowRepository.findActiveBookBorrows().isEmpty()) {
            return null;
        } else {
            return bookBorrowRepository.findActiveBookBorrows();
        }
    }

    public void endBookBorrow(Long bookBorrowId) {
        if (bookBorrowRepository.isActive(bookBorrowId)) {
            bookBorrowRepository.endBookBorrow(bookBorrowId);
        }
    }

    public void activateBookBorrow(Employee employee, Long bookBorrowId) {
        if (bookBorrowRepository.existsById(bookBorrowId) && !bookBorrowRepository.isActive(bookBorrowId)) {
            bookBorrowRepository.activateBookBorrow(employee.getEmployeeId(), bookBorrowId);
        }
    }

    public BookBorrow getBookBorrowById(Long bookBorrowId) {
        return bookBorrowRepository.getBookBorrowById(bookBorrowId);
    }

    // <--------------------------- EMPLOYEES ------------------------------->//

    public Employee findEmployeeById(Long id) {
        if (id != null) {
            return employeeRepository.searchById(id);
        } else return null;
    }

    public Employee findEmployeeByIntId(Integer id) {
        if (id != null) {
            return employeeRepository.searchById(id.longValue());
        } else return null;
    }


    // <--------------------------- RETURNS ------------------------------->//

    public void addBookReturn(BookReturn bookReturn) {
        bookReturnRepository.save(bookReturn);
    }

    public List<BookBorrow> findReturnedBookBorrows() {
        return bookBorrowRepository.findReturnedBookBorrows();
    }


    // <--------------------------- RATING ------------------------------->//

    public void addBookRating(BookRating bookRating) {
        bookRatingRepository.save(bookRating);
    }

    public boolean bookRatingAlreadyExists(Integer userId, Integer bookIsbn) {
        return bookRatingRepository.alreadyExists(userId, bookIsbn);
    }

    public void updateBookRating(int rating, Integer userId, Integer bookIsbn) {
        bookRatingRepository.updateUsersBookRating(rating, userId, bookIsbn);
    }

    public Integer getUsersBookRating(Integer userId, Integer bookISBN) {
        return bookRatingRepository.getUsersBookRating(userId, bookISBN);
    }


    // <--------------------------- ADMIN ------------------------------->//

    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public void setGenre(Book book, Long id) {
        Genre g = genreRepository.findById(id).orElse(null);
        if (g != null) {
            book.setGenre(g);
        } else {
            return;
        }

    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }


    public List<Author> findallAuthors() {
        return authorRepository.findAll();
    }

    public void setAuthor(Book book, String name) {
        Optional<Author> a = authorRepository.findIfAuthorExistsByName(name);
        if(a.isEmpty()) {
            Author newAuthor = new Author(name);
            authorRepository.save(newAuthor);
            book.setBookAuthor(newAuthor);
            bookRepository.setBookAuthor(newAuthor.getAuthorId(), book.getIsbn());
        } else {
            book.setBookAuthor(authorRepository.findIfAuthorExistsByName(name).get());
        }
    }

}
