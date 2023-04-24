package com.projekt.dbs.viewsUser;

import com.projekt.dbs.entities.Book;
import com.projekt.dbs.layoutComponents.MainNavbar;
import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Katalog knihovny")
@Route(value = "")
@CssImport("MainView.css")
public class MainView extends VerticalLayout {
    Grid<Book> grid = new Grid<>(Book.class, false);
    MainNavbar navbar;
    private EntityManagementService service;
    private AuthService authService;

    private Button loginButton = new Button();

    @Autowired
    public MainView(EntityManagementService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
        navbar = new MainNavbar(service, authService);
        setClassName("mainpage");
        setJustifyContentMode(JustifyContentMode.START);
        setSpacing(false);
        getThemeList().add("spacing-l");
        //center grid
        configGrid();
        add(navbar, grid);
    }


    public void configGrid() {
        ListDataProvider<Book> dataProvider = new ListDataProvider<>(service.findAllBooks());
        grid.setHeight("500px");
        grid.setColumns("name", "bookAuthor", "publisher", "yearOfPublishing", "pages", "bookDescription");
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
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (VaadinSession.getCurrent().getAttribute("userid") != null) {
            UI.getCurrent().navigate(LoggedInView.class);
        }
    }

    public MainNavbar getNavbar() {
        return navbar;
    }
}
