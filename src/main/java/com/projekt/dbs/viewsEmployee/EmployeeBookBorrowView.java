package com.projekt.dbs.viewsEmployee;
import com.projekt.dbs.entities.BookBorrow;
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

@Route(value = "EmployeeBookBorrowView")
public class EmployeeBookBorrowView extends VerticalLayout {
    private Grid<BookBorrow> pendingBorrowsGrid = new Grid<>(BookBorrow.class, false);

    private EmployeeNavbar navbar = new EmployeeNavbar();

    private EntityManagementService service;

    @Autowired
    public EmployeeBookBorrowView(EntityManagementService service) {
        this.service = service;
        navbar.getEmployeeViewTabs().setSelectedIndex(2);
        configGrid();
        add(navbar, pendingBorrowsGrid);

    }

    private void configGrid() {
        ListDataProvider<BookBorrow> dataProvider = getDataProvider();
        pendingBorrowsGrid.setHeight("550px");
        pendingBorrowsGrid.addColumn(BookBorrow::getBookBorrowId).setHeader("ID výpůjčky");
        pendingBorrowsGrid.addColumn(BookBorrow::getBookName).setHeader("Kniha");
        pendingBorrowsGrid.addColumn(BookBorrow::getBookAuthor).setHeader("Autor");
        pendingBorrowsGrid.addColumn(BookBorrow::getBorrowerName).setHeader("Žadatel o výpůjčku");
        pendingBorrowsGrid.addColumn(BookBorrow::getBorrowerId).setHeader("ID žadatele");
        pendingBorrowsGrid.addComponentColumn(bookBorrow -> {
            Button decline = new Button();
            Button confirm = new Button();
            confirm.addClickListener(e -> {
                if(bookBorrow.getBook().getAvailableCopies() < 1){
                    Notification.show("Kniha není dostupná").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
                service.reduceAvailableCopies(bookBorrow.getBook().getIsbn());
                service.activateBookBorrow(service.findEmployeeByIntId((Integer) VaadinSession.getCurrent().getAttribute("employee")), bookBorrow.getBookBorrowId());
                ListDataProvider<BookBorrow> refreshedDataProvider = getDataProvider();
                pendingBorrowsGrid.setItems(refreshedDataProvider);
                Notification.show("Výpůjčka úspěšně přiřazena uživateli " + bookBorrow.getBorrowerName()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
            decline.addClickListener(e -> {
               service.deleteBookBorrow(bookBorrow.getBookBorrowId());
                ListDataProvider<BookBorrow> refreshedDataProvider = getDataProvider();
                pendingBorrowsGrid.setItems(refreshedDataProvider);
                Notification.show("Výpůjčka  uživatele " + bookBorrow.getBorrowerName() + " byla úspěšně zamítnuta").addThemeVariants(NotificationVariant.LUMO_CONTRAST);


            });
            confirm.setIcon(new Icon(VaadinIcon.CHECK_SQUARE));
            confirm.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            decline.setIcon(new Icon(VaadinIcon.CLOSE));
            decline.addThemeVariants(ButtonVariant.LUMO_ERROR);
            HorizontalLayout layout = new HorizontalLayout();
            layout.add(confirm, decline);
            return layout;
        }).setHeader("Potvrdit výpůjčku");
        pendingBorrowsGrid.setItems(dataProvider);
    }

    public ListDataProvider<BookBorrow> getDataProvider(){
        if(service.findPendingBookBorrows() != null){
            return new ListDataProvider<>(service.findPendingBookBorrows());
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

