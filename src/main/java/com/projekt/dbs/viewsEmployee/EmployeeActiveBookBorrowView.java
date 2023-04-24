package com.projekt.dbs.viewsEmployee;

import com.projekt.dbs.entities.BookBorrow;
import com.projekt.dbs.entities.BookReturn;
import com.projekt.dbs.entities.Employee;
import com.projekt.dbs.layoutComponents.EmployeeNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route(value = "EmployeeActiveBookBorrowView")
public class EmployeeActiveBookBorrowView extends VerticalLayout {

    private Grid<BookBorrow> activeBorrowsGrid = new Grid<>(BookBorrow.class, false);

    private EmployeeNavbar navbar = new EmployeeNavbar();

    private EntityManagementService service;

    @Autowired
    public EmployeeActiveBookBorrowView(EntityManagementService service) {
        this.service = service;
        navbar.getEmployeeViewTabs().setSelectedIndex(3);
        configGrid();
        add(navbar, activeBorrowsGrid);
    }

    private void configGrid() {
        ListDataProvider<BookBorrow> dataProvider = getDataProvider();
        activeBorrowsGrid.setHeight("550px");
        activeBorrowsGrid.addColumn(BookBorrow::getBookBorrowId).setHeader("ID výpůjčky");
        activeBorrowsGrid.addColumn(BookBorrow::getBookName).setHeader("Kniha");
        activeBorrowsGrid.addColumn(BookBorrow::getBookAuthor).setHeader("Autor");
        activeBorrowsGrid.addColumn(BookBorrow::getBorrowerName).setHeader("Uživatel");
        activeBorrowsGrid.addColumn(BookBorrow::getBorrowerId).setHeader("ID uživatele");
        activeBorrowsGrid.addColumn(BookBorrow::getDueDate).setHeader("Datum konce výpůjčky");
        activeBorrowsGrid.addComponentColumn(bookBorrow -> {
            Button returnBook = new Button();
            returnBook.addClickListener(e -> {
                Employee employee = service.findEmployeeByIntId((Integer) VaadinSession.getCurrent().getAttribute("employee"));
                BookReturn bookReturn = new BookReturn(service.getBookBorrowById(bookBorrow.getBookBorrowId()), employee.getEmployeeId());
                service.addBookReturn(bookReturn);
                service.increaseAvailableCopies(bookBorrow.getBook().getIsbn());
                service.endBookBorrow(bookBorrow.getBookBorrowId());
                ListDataProvider<BookBorrow> refreshedDataProvider = getDataProvider();
                activeBorrowsGrid.setItems(refreshedDataProvider);
                Notification.show("Výpůjčka úspěšně ukončena, kniha je vrácena").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
            returnBook.setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
            returnBook.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            HorizontalLayout layout = new HorizontalLayout();
            layout.add(returnBook);
            return layout;
        }).setHeader("Vrácení knihy");
        activeBorrowsGrid.setItems(dataProvider);
    }

    public ListDataProvider<BookBorrow> getDataProvider(){
        if(service.findActiveBookBorrows() != null){
            return new ListDataProvider<>(service.findActiveBookBorrows());
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
