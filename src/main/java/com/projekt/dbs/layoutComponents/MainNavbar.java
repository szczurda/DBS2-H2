package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

@CssImport("CustomNavbar.css")
public class MainNavbar extends HorizontalLayout {
    TextField searchField = new TextField();
    Button loginButton = new Button("Login");
    Label label = new Label("Katalog knihovny ");

    UserLoginForm userLoginForm;
    private EntityManagementService service;
    private AuthService authService;

    public MainNavbar(EntityManagementService service, AuthService authService){
        this.service = service;
        this.authService = authService;
        userLoginForm = new UserLoginForm(authService, service);
        setClassName("navbar");
        setVisible(true);
        configButtons();
        configSearchField();
        label.setClassName("header-label");
        this.add(label, searchField, loginButton);
        this.setSpacing(false);
        this.getThemeList().add("spacing-s");
        this.setWidthFull();
        this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.getStyle().set("border-top", "5px solid royalblue")
                .set("padding", "10px")
                .set("border-bottom", "5px solid royalblue");

    }
    private void configButtons() {
        loginButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        loginButton.addClickListener(e -> userLoginForm.open());
        searchField.getStyle().set("margin-left", "auto").set("width", "30%");
        }
    public TextField getSearchField() {
        return searchField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
    private void configSearchField(){
        searchField.setClearButtonVisible(true);
        searchField.getElement().getThemeList().add("search-field-theme");
        searchField.setPlaceholder("Vyhledat");
        searchField.setClassName("searchbar");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
    }
}
