package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.entities.Book;
import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.entities.BookRating;
import com.projekt.dbs.service.EntityManagementService;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@CssImport("BookRatingDialog.css")
public class BookRatingDialog extends Dialog {

    Label label = new Label("Ohodnotit knihu");

    private Binder<BookBorrow> binder = new BeanValidationBinder<>(BookBorrow.class);
    private VerticalLayout layout = new VerticalLayout();

    private HorizontalLayout buttonLayout = new HorizontalLayout();
    private HorizontalLayout starsLayout = new HorizontalLayout();
    TextField bookName = new TextField("Název knihy");
    TextField bookAuthor = new TextField("Autor");

    TextArea bookDesc = new TextArea("Popis knihy");
    Button cancelButton;
    Button addRatingButton;
    int ratingIndex;

    Book book;
    @Autowired
    private EntityManagementService service;

    public BookRatingDialog(Book book, EntityManagementService service) {
        this.book = book;
        this.service = service;
        setWidth("500px");
        label.getStyle().set("font-size", "24px");
        initFields();
        initButtons();
        initLayouts();
        configRating();
        add(layout);
    }

    private void initFields() {
        bookAuthor.setReadOnly(true);
        bookAuthor.setValue(book.getAuthorName());
        bookName.setReadOnly(true);
        bookName.setValue(book.getName());
        bookDesc.setValue(book.getBookDescription());
        bookDesc.setReadOnly(true);
        bookDesc.setWidthFull();
    }

    private void initButtons(){
        addRatingButton = new Button("Potvrdit");
        addRatingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRatingButton.addClickListener(e -> {
            if(ratingIndex != 0){
                Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
                if(service.bookRatingAlreadyExists(userId, book.getIsbn())){
                    service.updateBookRating(ratingIndex, userId, book.getIsbn());
                    Notification.show("Hodnocení knihy " + book.getName() + " upraveno").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    this.close();
                } else {
                    BookRating rating = new BookRating(service.searchById(userId), ratingIndex, book);
                    service.addBookRating(rating);
                    Notification.show("Hodnocení knihy " + book.getName() + " přidáno").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    this.close();
                }
            } else {
                Notification.show("Vyplňte pole s hodnocením");
            }
        });
        cancelButton = new Button("Zrušit");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        cancelButton.addClickListener(e -> this.close());
    }

    private void initLayouts(){
        buttonLayout.setWidthFull();
        buttonLayout.add(addRatingButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.add(label,bookName,bookAuthor, bookDesc,starsLayout, buttonLayout);
        layout.setHeightFull();
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("dialogLayout");
    }

    private void configRating(){
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if(service.bookRatingAlreadyExists(userId, book.getIsbn())){
            ratingIndex = service.getUsersBookRating(userId, book.getIsbn());
        } else {
            ratingIndex = 0;
        }
        starsLayout.setWidthFull();
        starsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        starsLayout.addClassName("stars-layout");
        List<Button> stars = new ArrayList<>();
        Button star1 = new Button();
        Button star2 = new Button();
        Button star3 = new Button();
        Button star4 = new Button();
        Button star5 = new Button();
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);
        if(ratingIndex != 0){
            for (int i = 0; i < ratingIndex; i++) {
                stars.get(i).setIcon(new Icon(VaadinIcon.STAR));
            }
            for(int i2 = stars.size() - 1; i2 >= ratingIndex; i2--){
                stars.get(i2).setIcon(new Icon(VaadinIcon.STAR_O));
            }
        } else {
            for(Button star: stars){
                star.setIcon(new Icon(VaadinIcon.STAR_O));
            }
        }

        for(int i = 0, starsSize = stars.size(); i < starsSize; i++) {
            Button star = stars.get(i);
            star.getStyle().set("background-color", "white");
            int finalI = i;
            star.addClickListener(e -> {
                for(int j = 0; j <= finalI; j++){
                    stars.get(j).setIcon(new Icon(VaadinIcon.STAR));
                    if(j == finalI){
                        ratingIndex = j + 1;
                        System.out.println(ratingIndex);
                    }
                }
                for(int k = stars.size() - 1; k > finalI; k--){
                    stars.get(k).setIcon(new Icon(VaadinIcon.STAR_O));
                }

            });
        }
        starsLayout.add(star1,star2,star3,star4,star5);
    }

    public int getRatingIndex() {
        return ratingIndex;
    }

    public void setRatingIndex(int ratingIndex) {
        this.ratingIndex = ratingIndex;
    }
}
