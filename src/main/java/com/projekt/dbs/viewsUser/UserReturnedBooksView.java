package com.projekt.dbs.viewsUser;

import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.layoutComponents.BookRatingDialog;
import com.projekt.dbs.layoutComponents.UserProfileNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@PageTitle("Účet uživatele - Vrácené knihy")
@Route(value = "UserReturns")
@CssImport("UserReturnedBooksView.css")
public class UserReturnedBooksView extends VerticalLayout implements BeforeEnterObserver {

    private UserProfileNavbar navbar = new UserProfileNavbar();

    private Grid<BookBorrow> userReturnsGrid = new Grid<>(BookBorrow .class, false);

    Button rateButton;

    EntityManagementService service;

    public UserReturnedBooksView(EntityManagementService service) {
        this.service = service;
        setHeightFull();
        navbar.getUserProfileTabs().setSelectedIndex(1);
        configGrid();
        add(navbar, userReturnsGrid);
    }

    private void configGrid(){
        ListDataProvider<BookBorrow> dataProvider = getDataProvider();
        userReturnsGrid.setItems(dataProvider);
        userReturnsGrid.setWidthFull();
        userReturnsGrid.addColumn(BookBorrow::getBookBorrowId).setHeader("ID výpůjčky");
        userReturnsGrid.addColumn(BookBorrow::getBookName).setHeader("Kniha");
        userReturnsGrid.addColumn(BookBorrow::getBookAuthor).setHeader("Autor");
        userReturnsGrid.addColumn(BookBorrow::getReturnDate).setHeader("Datum vrácení knihy");;
        userReturnsGrid.addComponentColumn(bookBorrow -> {
            rateButton = new Button();
            rateButton.setIcon(new Icon(VaadinIcon.STAR));
            rateButton.addClickListener(e -> {
                BookRatingDialog bookRatingDialog = new BookRatingDialog(bookBorrow.getBook(), service);
                bookRatingDialog.open();
            });
            return rateButton;

        }).setHeader("Udělit hodnocení");
        userReturnsGrid.getStyle().set("margin-top", "30px");
        userReturnsGrid.setHeight("90%");
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(VaadinSession.getCurrent().getAttribute("currentUser") == null){
            beforeEnterEvent.rerouteTo(MainView.class);
        }
    }

    public ListDataProvider<BookBorrow> getDataProvider(){
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        if(service.findUsersReturnedBookBorrows(userId) != null){

            List<BookBorrow> list = service.findUsersReturnedBookBorrows(userId).stream()
                    .collect(Collectors.toMap(BookBorrow::getBookName, Function.identity(), (bookborrow1, bookborrow2) -> bookborrow1))
                    .values()
                    .stream()
                    .collect(Collectors.toList());
            return new ListDataProvider<>(list);

        } else {
            return new ListDataProvider<>(Collections.emptyList());
        }
    }
}
