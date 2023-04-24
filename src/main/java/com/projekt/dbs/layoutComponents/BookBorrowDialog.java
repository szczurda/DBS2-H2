package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.entities.Book;
import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.entities.User;
import com.projekt.dbs.service.EntityManagementService;
import com.projekt.dbs.tools.DoubleToIntConverter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("BookBorrowDialog.css")
public class BookBorrowDialog extends Dialog {
    private Label label = new Label("Požádat o výpůjčku");
    private TextField bookNameField = new TextField("Název knihy");
    private TextField authorField = new TextField("Autor");
    private TextField publisherField = new TextField("Vydavatelství");
    private NumberField yearField = new NumberField("Rok vydání");
    private NumberField pagesField = new NumberField("Počet stran");
    private TextField descriptionField = new TextField("Popis knihy");
    private NumberField ISBNfield = new NumberField("ISBN");
    private DoubleToIntConverter doubleToIntConverter = new DoubleToIntConverter();
    private Binder<Book> binder = new BeanValidationBinder<>(Book.class);
    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout numberFieldContainer = new HorizontalLayout();

    private HorizontalLayout buttonContainer = new HorizontalLayout();

    private Button confirmBookBorrow = new Button("Potvrdit výpůjčku");

    private Button cancelButton = new Button("Zrušit");

    private Book book;
    private EntityManagementService service;
    @Autowired
    public BookBorrowDialog(EntityManagementService service) {
        this.service = service;
        configBinders();
        setWidth("600px");
        setHeight("700px");
        label.getStyle().set("font-size", "27.5px");
        initFieldsAsReadOnly();
        configButtons();
        layout.addClassName("dialogLayout");
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        numberFieldContainer.add(yearField, pagesField, ISBNfield);
        numberFieldContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        numberFieldContainer.setWidthFull();
        numberFieldContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.add(label,bookNameField, authorField, publisherField, numberFieldContainer, descriptionField, buttonContainer);
        add(layout);
        initListeners();
    }

    public void initFieldsAsReadOnly() {
        bookNameField.setReadOnly(true);
        authorField.setReadOnly(true);
        publisherField.setReadOnly(true);
        yearField.setReadOnly(true);
        pagesField.setReadOnly(true);
        ISBNfield.setReadOnly(true);
        descriptionField.setReadOnly(true);
    }
    private void configBinders() {
        binder.forField(bookNameField).bind(Book::getName, Book::setName);
        binder.forField(authorField).bind(Book::getAuthorName, Book::setAuthorName);
        binder.forField(publisherField).bind(Book::getPublisher, Book::setPublisher);
        binder.forField(pagesField).withConverter(doubleToIntConverter).bind(Book::getPages, Book::setPages);
        binder.forField(yearField).withConverter(doubleToIntConverter).bind(Book::getYearOfPublishing, Book::setYearOfPublishing);
        binder.forField(ISBNfield).withConverter(doubleToIntConverter).bind(Book::getIsbn, Book::setIsbn);
        binder.forField(descriptionField).bind(Book::getBookDescription, Book::setBookDescription);
    }


    public void setBook(Book book) {
        this.book = book;
        binder.setBean(book);
        binder.readBean(book);

    }

    private void configButtons(){
        confirmBookBorrow.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmBookBorrow.setWidth("50%");
        confirmBookBorrow.setIcon(new Icon(VaadinIcon.CHECK));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.setWidth("50%");
        cancelButton.setIcon(new Icon(VaadinIcon.CLOSE));
        buttonContainer.add(confirmBookBorrow,cancelButton);
        buttonContainer.setWidthFull();
        buttonContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonContainer.getStyle().set("margin-top", "20px");
    }


    // Event
    public void initListeners(){
        cancelButton.addClickListener(e -> close());
        confirmBookBorrow.addClickListener(e -> {
            User currentUser = (User) VaadinSession.getCurrent().getAttribute("currentUser");
            if(service.isNotDuplicate((binder.getBean().getIsbn()), currentUser.getId())){
                BookBorrow bookBorrow = new BookBorrow(binder.getBean(), currentUser);
                service.addBookBorrowRequest(bookBorrow);
                close();
                Notification.show("Žádost o výpůjčku knihy " + binder.getBean().getName() + " proběhla úspěšně").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            } else{
                Notification.show("Kniha je již zařazena ve výpůjčkách").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }


    public Binder<Book> getBinder() {
        return binder;
    }

    public Button getConfirmBookBorrow() {
        return confirmBookBorrow;
    }
}

