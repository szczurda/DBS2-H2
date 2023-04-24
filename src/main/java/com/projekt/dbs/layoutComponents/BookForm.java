package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.entities.Author;
import com.projekt.dbs.entities.Book;
import com.projekt.dbs.entities.Genre;
import com.projekt.dbs.service.EntityManagementService;
import com.projekt.dbs.tools.DoubleToIntConverter;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookForm extends FormLayout {
    private TextField name = new TextField("Název Knihy");
    private ComboBox<Author> author = new ComboBox<>("Autor");
    private TextField publisher = new TextField("Nakladatelství");
    private NumberField yearOfPublishing = new NumberField("Rok vydání");
    private NumberField pages = new NumberField("Počet stran");
    private NumberField isbn = new NumberField("isbn");
    private TextField bookDescription = new TextField("Popis knihy");
    private ComboBox<Genre> genre = new ComboBox<>("Žánr");
    private NumberField availableCopiesField = new NumberField("Počet dostupných kopií");
    private Button saveButton = new Button("Uložit");
    private Button deleteButton = new Button("Smazat");
    private Button cancelButton = new Button("Zrušit");
    private Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private Book book;
    private String authorName;
    private List<Author> authorList;

    DoubleToIntConverter doubleToIntConverter = new DoubleToIntConverter();

    @Autowired
    EntityManagementService service;
    public BookForm(EntityManagementService service) {
        this.service = service;
        addClassName("book-form");
        // instance jsou nabindovány přes onAttach() metodu
        add(name, author, genre, publisher, yearOfPublishing, pages, isbn, bookDescription, availableCopiesField, createButtonLayout());
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, book)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(book);
            fireEvent(new SaveEvent(this, book));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public void setBook(Book book) {
        authorName = null;
        this.book = book;
        binder.readBean(book);
    }

    public static abstract class BookFormEvent extends ComponentEvent<BookForm> {
        private Book book;

        protected BookFormEvent(BookForm source, Book book) {
            super(source, false);
            this.book = book;
        }

        public Book getBook() {
            return book;
        }
    }

    public static class SaveEvent extends BookFormEvent {
        SaveEvent(BookForm source, Book book) {
            super(source, book);
        }
    }

    public static class DeleteEvent extends BookFormEvent {
        DeleteEvent(BookForm source, Book book) {
            super(source, book);
        }

    }

    public static class CloseEvent extends BookFormEvent {
        CloseEvent(BookForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public Binder<Book> getBinder() {
        return binder;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        configBinders();
    }

    private void configBinders() {
        authorList = service.findallAuthors();
        author.setItems(authorList);
        author.setItemLabelGenerator(Author::getName);
        author.setAllowCustomValue(true);
        author.addCustomValueSetListener(e -> {
            authorName = e.getDetail();
        });
        author.addValueChangeListener(e -> {
            if(e.getValue() != null){
                authorName = e.getValue().getName();
            }
        });
        genre.setItems(service.findAllGenres());
        genre.setItemLabelGenerator(Genre::getGenreName);


        binder.forField(name).bind(Book::getName, Book::setName);
        binder.forField(publisher).bind(Book::getPublisher, Book::setPublisher);
        binder.forField(pages).withConverter(doubleToIntConverter).bind(Book::getPages, Book::setPages);
        binder.forField(author).bind(Book::getBookAuthor, (book, auth) -> service.setAuthor(book, authorName));
        binder.forField(yearOfPublishing).withConverter(doubleToIntConverter).bind(Book::getYearOfPublishing, Book::setYearOfPublishing);
        binder.forField(isbn).withConverter(doubleToIntConverter).bind(Book::getIsbn, Book::setIsbn);
        binder.forField(bookDescription).bind(Book::getBookDescription, Book::setBookDescription);
        binder.forField(availableCopiesField).withConverter(doubleToIntConverter).bind(Book::getAvailableCopies, Book::setAvailableCopies);
        binder.forField(genre).bind(Book::getGenre, (book, genre) -> service.setGenre(book, genre.getGenreId()));
    }

    public NumberField getISBN() {
        return isbn;
    }


}
