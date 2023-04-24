package com.projekt.dbs.viewsEmployee;

import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.layoutComponents.EmployeeNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Collections;

@Route(value = "EmployeeBookReturnsView")
public class EmployeeBookReturnsView extends VerticalLayout {

    private Grid<BookBorrow> bookReturnsGrid = new Grid<>(BookBorrow.class, false);

    private EmployeeNavbar navbar = new EmployeeNavbar();

    private EntityManagementService service;


    public EmployeeBookReturnsView(EntityManagementService service) {
        this.service = service;
        navbar.getEmployeeViewTabs().setSelectedIndex(4);
        configGrid();
        add(navbar, bookReturnsGrid);
    }

    private void configGrid() {
        ListDataProvider<BookBorrow> dataProvider = getDataProvider();
        bookReturnsGrid.setHeight("550px");
        bookReturnsGrid.addColumn(BookBorrow::getBookBorrowId).setHeader("ID výpůjčky");
        bookReturnsGrid.addColumn(BookBorrow::getBookName).setHeader("Kniha");
        bookReturnsGrid.addColumn(BookBorrow::getBookAuthor).setHeader("Autor");
        bookReturnsGrid.addColumn(BookBorrow::getBorrowerName).setHeader("Uživatel");
        bookReturnsGrid.addColumn(BookBorrow::getReturnDate).setHeader("Datum vratky");
/*        BookBorrowsGrid.addComponentColumn(BookBorrow -> {
            Button returnBook = new Button();
            returnBook.addClickListener(e -> {
                service.increaseAvailableCopies(BookBorrow.getBook().getIsbn());
                service.endBookBorrow(BookBorrow.getBookBorrowId());
                ListDataProvider<BookBorrow> refreshedDataProvider = getDataProvider();
                BookBorrowsGrid.setItems(refreshedDataProvider);
                Notification.show("Výpůjčka úspěšně ukončena, kniha je vrácena").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
            returnBook.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
            returnBook.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            HorizontalLayout layout = new HorizontalLayout();
            layout.add(returnBook);
            return layout;
        }).setHeader("Vrácení knihy");*/
        bookReturnsGrid.setItems(dataProvider);
    }

    public ListDataProvider<BookBorrow> getDataProvider(){
        if(service.findReturnedBookBorrows() != null){
            return new ListDataProvider<>(service.findReturnedBookBorrows());
        } else {
            return new ListDataProvider<>(Collections.emptyList());
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(VaadinSession.getCurrent().getAttribute("employee") == null){
            UI.getCurrent().navigate("EmployeeLogin");
        }
    }
}
