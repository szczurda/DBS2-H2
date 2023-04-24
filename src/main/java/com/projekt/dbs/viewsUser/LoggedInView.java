package com.projekt.dbs.viewsUser;

import com.projekt.dbs.entities.Book;
import com.projekt.dbs.layoutComponents.BookBorrowDialog;
import com.projekt.dbs.layoutComponents.MainNavbar;
import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@PageTitle("Katalog knihovny")
@Route(value = "/LoggedInView")
@CssImport("MainView.css")
public class LoggedInView extends VerticalLayout {
    Grid<Book> grid = new Grid<>(Book.class, false);
    MainNavbar navbar;
    private EntityManagementService service;
    Button logoutButton = new Button("Odhlásit se");
    BookBorrowDialog bookDialog;
    private AuthService authService;
    Button profileButton = new Button();

    public LoggedInView(EntityManagementService service, AuthService authService){
        this.service = service;
        bookDialog = new BookBorrowDialog(service);
        this.authService = authService;
        navbar = new MainNavbar(service,authService);
        setClassName("loggedInView");
        setJustifyContentMode(JustifyContentMode.START);
        setSpacing(false);
        getThemeList().add("spacing-l");
        configGrid();
        initListeners();
        navbar.remove(navbar.getLoginButton());
        profileButton.setText(((String) VaadinSession.getCurrent().getAttribute("userName")));
        profileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        profileButton.setIcon(new Icon(VaadinIcon.USER));
        navbar.add(profileButton, logoutButton);
        add(navbar, grid);
        if(VaadinSession.getCurrent().getAttribute("loggedIn") == null){
            UI.getCurrent().navigate(MainView.class);
        }
    }
    public void configGrid(){
        ListDataProvider<Book> dataProvider = new ListDataProvider<>(service.findAllBooks());
        grid.setHeight("500px");
        grid.setColumns("name", "bookAuthor", "publisher","yearOfPublishing", "pages", "bookDescription");
        grid.getColumns().forEach(column -> setClassName("columns"));
        grid.getColumnByKey("name").setHeader("Název knihy");
        grid.getColumnByKey("bookAuthor").setHeader("Autor");
        grid.getColumnByKey("publisher").setHeader("Nakladatelství");
        grid.getColumnByKey("yearOfPublishing").setHeader("Rok vydání");
        grid.getColumnByKey("pages").setHeader("Počet stran");
        grid.getColumnByKey("bookDescription").setHeader("Detaily");
        grid.setItems(dataProvider);
        navbar.getSearchField().addValueChangeListener(e -> {
            String input = e.getValue();
            dataProvider.setFilter(book ->
                    book.getName().toLowerCase().contains(input.toLowerCase())
                            || book.getAuthorName().toLowerCase().contains(input.toLowerCase())
                            || book.getPublisher().toLowerCase().contains(input.toLowerCase()));
        });
        grid.asSingleSelect().addValueChangeListener(e -> {
            Button button = bookDialog.getConfirmBookBorrow();
            if(e.getValue() != null){
                bookDialog.setBook(e.getValue());
                bookDialog.open();
            }if(bookDialog.getBinder().getBean().getAvailableCopies() < 1){
                button.setEnabled(false);
                button.getStyle().set("background-color", "grey");
            } else {
                button.setEnabled(true);
                button.getStyle().remove("background-color");
            }
        });
    }

    public void initListeners(){
        logoutButton.addClickListener(e -> {
            VaadinSession.getCurrent().setAttribute("loggedIn", false);
            VaadinSession.getCurrent().setAttribute("userName", null);
            VaadinSession.getCurrent().setAttribute("currentUser", null);
            UI.getCurrent().navigate("");
        });
        profileButton.addClickListener(e -> UI.getCurrent().navigate("UserProfile"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(VaadinSession.getCurrent().getAttribute("loggedIn") != null) {
            if (!(boolean) VaadinSession.getCurrent().getAttribute("loggedIn")) {
                UI.getCurrent().navigate("");
                Notification.show("Přihlaste se").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else{
            UI.getCurrent().navigate("");
            Notification.show("Přihlaste se").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
