package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Component;


@CssImport("LoginForm.css")
@Component
public class UserLoginForm extends Dialog {

    Label label = new Label("Přihlašovací formulář");

    EmailField emailField = new EmailField("Email");

    PasswordField passwordField = new PasswordField("Heslo");

    Button logInButton = new Button("Přihlásit se");

    Span regFormRedirect = new Span("Chci se registrovat");

    private AuthService authService;

    private EntityManagementService service;

    public UserLoginForm(AuthService authService, EntityManagementService service) {
        this.service = service;
        this.authService = authService;
        VerticalLayout layout = new VerticalLayout();
        label.addClassName("login-form-label");
        setClassName("user-login-form");
        passwordField.getStyle().set("margin-bottom", "20px");
        passwordField.getElement().setAttribute("autocomplete", "new-password");
        setHeight("420px");
        setWidth("500px");
        logInButton.addClickShortcut(Key.ENTER);
        initButtonListeners();
        regFormRedirect.getStyle().set("color", "blue").set("text-align", "center").set("font-size", "17.5px");
        regFormRedirect.getElement().addEventListener("mouseover", e -> {
           regFormRedirect.getElement().getStyle().set("cursor", "pointer");
        });
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(label, emailField, passwordField, logInButton, regFormRedirect);
        add(layout);

    }

    private void initButtonListeners() {
        UserRegistrationDialog registrationDialog = new UserRegistrationDialog(authService, service);
        logInButton.addClickListener(e -> login(emailField.getValue(), passwordField.getValue()));
        regFormRedirect.addClickListener(e -> {
            this.close();
            registrationDialog.open();
        });
    }

    private void login(String email, String password) {
        if(email.isEmpty()){
            Notification.show("Pole s emailem nesmí být prázdné");
        } else if(password.isEmpty()){
            Notification.show("Pole s heslem nesmí být prázdné");
        } else if(authService.authenticate(email, password)){
            VaadinSession.getCurrent().setAttribute("loggedIn", true);
            String userName = service.searchByEmail(email).getFirstName() + " " + service.searchByEmail(email).getLastName();
            VaadinSession.getCurrent().setAttribute("userName", userName);
            VaadinSession.getCurrent().setAttribute("currentUser", service.searchByEmail(email));
            VaadinSession.getCurrent().setAttribute("userId", service.getIdByEmail(email));
            UI.getCurrent().navigate("LoggedInView");
            close();
        }
        else {
            Notification.show("Zadali jste neplatný email nebo heslo").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
