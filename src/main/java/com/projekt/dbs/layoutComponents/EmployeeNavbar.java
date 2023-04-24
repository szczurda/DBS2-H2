package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.viewsEmployee.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class EmployeeNavbar extends HorizontalLayout{

    Button employeeProfileButton = new Button();
    Tabs employeeViewTabs = new Tabs();
    public EmployeeNavbar() {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        employeeProfileButton.setIcon(VaadinIcon.USER.create());
        employeeProfileButton.getStyle().set("width", "50px").set("height", "50px");
        add(createTabs(), employeeProfileButton);
        setVerticalComponentAlignment(Alignment.CENTER);
    }

    public Tabs createTabs() {
        employeeViewTabs.addClassName("tabs");
        employeeViewTabs.getStyle().set("display", "flex").set("justify-content", "center");
        employeeViewTabs.setWidthFull();
        Tab bookManagerTab = createTab("Správa knih", EmployeeBookManagerView.class);
        Tab userManagerTab = createTab("Přehled uživatelů", EmployeeUserManagerView.class);
        Tab bookBorrowTab = createTab("Přiřazení výpůjčky", EmployeeBookBorrowView.class);
        Tab activeBookBorrowTab = createTab("Aktivní výpůjčky", EmployeeActiveBookBorrowView.class);
        Tab bookReturnsTab = createTab("Vrácené knihy", EmployeeBookReturnsView.class);
        setAlignItems(Alignment.CENTER);
        employeeViewTabs.add(bookManagerTab, userManagerTab, bookBorrowTab, activeBookBorrowTab, bookReturnsTab);
        return employeeViewTabs;
    }


    private Tab createTab(String name, Class<? extends Component> path) {
        RouterLink link = new RouterLink();
        link.add(name);
        link.setRoute(path);
        return new Tab(link);
    }

    public Tabs getEmployeeViewTabs() {
        return employeeViewTabs;
    }
}
