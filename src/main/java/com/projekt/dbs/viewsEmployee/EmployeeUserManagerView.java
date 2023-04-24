package com.projekt.dbs.viewsEmployee;

import com.projekt.dbs.entities.User;
import com.projekt.dbs.layoutComponents.EmployeeNavbar;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("EmployeeView.css")
@Route(value = "EmployeeViewUsers")
public class EmployeeUserManagerView extends VerticalLayout {

    private EmployeeNavbar navbar = new EmployeeNavbar();

    private Grid<User> grid = new Grid<>(User.class, false);

    private EntityManagementService service;


    @Autowired
    public EmployeeUserManagerView(EntityManagementService service) {
        this.service = service;
        configGrid();
        navbar.getEmployeeViewTabs().setSelectedIndex(1);
        add(navbar, grid);
    }

    public void configGrid(){
        ListDataProvider<User> dataProvider = new ListDataProvider<>(service.findAllUsers());
        grid.setItems(dataProvider);
        grid.setHeightFull();
        grid.setColumnReorderingAllowed(false);
        grid.setColumns("id", "firstName", "lastName", "email", "password");
        grid.getColumnByKey("id").setHeader("Číslo čtenáře");
        grid.getColumnByKey("firstName").setHeader("Jméno");
        grid.getColumnByKey("lastName").setHeader("Příjmení");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("password").setHeader("Heslo");
        grid.getColumns().forEach(column -> grid.setClassName("columns"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if(VaadinSession.getCurrent().getAttribute("employee") == null){
            UI.getCurrent().navigate("EmployeeLogin");
        }
    }



}
