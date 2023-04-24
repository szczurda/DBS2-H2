package com.projekt.dbs.viewsUser;

import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.entities.User;
import com.projekt.dbs.layoutComponents.UserProfileNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@PageTitle("Účet uživatele")
@Route(value = "UserProfile")
public class UserProfileView extends VerticalLayout implements BeforeEnterObserver {
    private Avatar xlAvatar = new Avatar();
    private UserProfileNavbar navbar = new UserProfileNavbar();
    private HorizontalLayout mainLayout = new HorizontalLayout();
    private HorizontalLayout headerLayout = new HorizontalLayout();

    private Grid<BookBorrow> userBookBorrowsGrid = new Grid<>(BookBorrow.class, false);
    private VerticalLayout userInfoLayout = new VerticalLayout();
    private Binder<User> binder = new BeanValidationBinder<>(User.class);
    private TextField nameField = new TextField("Jméno");
    private TextField lastNameField = new TextField("Příjmení");
    private EmailField emailField = new EmailField("Email");
    private PasswordField passwordField = new PasswordField("Heslo");
    private Label nameLabel = new Label();

    EntityManagementService service;

    @Autowired
    public UserProfileView(EntityManagementService service) {
        this.service = service;
        xlAvatar.setName((String) VaadinSession.getCurrent().getAttribute("userName"));
        xlAvatar.setWidth("100px");
        xlAvatar.setHeight("100px");
        configLayouts();
        setHeightFull();
        getThemeList().add("spacing-xs");
        add(navbar, mainLayout);
            configGrid();
    }

    private void configLayouts() {
        configFieldsLayout();
        mainLayout.setWidthFull();
        mainLayout.getStyle().set("align-self", "center").set("padding", "30px");
        mainLayout.setHeightFull();
        nameLabel.setText((String) VaadinSession.getCurrent().getAttribute("userName"));
        nameLabel.getStyle().set("font-size", "40px");
        headerLayout.setWidthFull();
        headerLayout.add(xlAvatar, nameLabel);
        headerLayout.setAlignItems(Alignment.CENTER);
        mainLayout.add(userInfoLayout, userBookBorrowsGrid);
        userInfoLayout.getStyle().set("flex-basis", "40%").set("border-radius", "20px");
        userInfoLayout.setAlignItems(Alignment.CENTER);
        userInfoLayout.setHeightFull();
        userInfoLayout.add(headerLayout, nameField, lastNameField, emailField, passwordField);
    }


    private void configFieldsLayout(){
        binder.setBean((User)VaadinSession.getCurrent().getAttribute("currentUser"));
        binder.forField(nameField).bind(User::getFirstName, User::setFirstName);
        binder.forField(lastNameField).bind(User::getLastName, User::setFirstName);
        binder.forField(emailField).bind(User::getEmail, User::setEmail);
        binder.forField(passwordField).bind(User::getPassword, User::setPassword);
        nameField.setReadOnly(true);
        lastNameField.setReadOnly(true);
        passwordField.setReadOnly(true);
        emailField.setReadOnly(true);
    }

    private void configGrid(){
        ListDataProvider<BookBorrow> dataProvider = getDataProvider();
        userBookBorrowsGrid.setItems(dataProvider);
        userBookBorrowsGrid.setWidthFull();
        userBookBorrowsGrid.addColumn(BookBorrow::getBookBorrowId).setHeader("ID výpůjčky");
        userBookBorrowsGrid.addColumn(BookBorrow::getBookName).setHeader("Kniha");
        userBookBorrowsGrid.addColumn(BookBorrow::getBookAuthor).setHeader("Autor");
        userBookBorrowsGrid.addColumn(BookBorrow::getLoanDate).setHeader("Datum začátku výpůjčky");
        userBookBorrowsGrid.addColumn(BookBorrow::getDueDate).setHeader("Datum konce výpůjčky");
        userBookBorrowsGrid.addComponentColumn(bookBorrow -> {
            if(bookBorrow.isActive()){
                Icon iconCheck = new Icon(VaadinIcon.CHECK);
                iconCheck.getStyle().set("color", "green");
                return iconCheck;
            } else{
                Icon iconCross = new Icon(VaadinIcon.CLOSE);
                iconCross.getStyle().set("color", "red");
                return iconCross;
            }
        }).setHeader("Aktivní");
        userBookBorrowsGrid.getStyle().set("margin-top", "30px");
        userBookBorrowsGrid.setHeight("90%");
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(VaadinSession.getCurrent().getAttribute("currentUser") == null){
            beforeEnterEvent.rerouteTo(MainView.class);
        }
    }

    public ListDataProvider<BookBorrow> getDataProvider(){
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if(service.findUsersBookBorrows(userId) != null){
            return new ListDataProvider<>(service.findUsersBookBorrows(userId));
        } else {
            return new ListDataProvider<>(Collections.emptyList());
        }
    }


}
