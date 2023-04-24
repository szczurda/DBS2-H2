package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;

@CssImport("RegForm.css")
public class UserRegistrationDialog extends Dialog {

    Label label = new Label("Registrační formulář");

    TextField nameField = new TextField("Jméno");
    TextField lastNameField = new TextField("Příjmení");
    EmailField emailField = new EmailField("Email");
    PasswordField passwordField = new PasswordField("Heslo");
    PasswordField confirmPassField = new PasswordField("Potvrzení hesla");
    Button confirmRegistrationButton = new Button("Potvrdit registraci");

    private AuthService authService;
    private EntityManagementService service;

    public UserRegistrationDialog(AuthService authService, EntityManagementService service){
        this.service = service;
        VerticalLayout dialogLayout = new VerticalLayout();
        this.authService = authService;
        setClassName("user-reg-form");
        setHeightFull();
        setWidth("500px");
        initButtonListener();
        label.setClassName("reg-form-label");
        passwordField.getElement().setAttribute("autocomplete", "new-password");
        confirmRegistrationButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialogLayout.add(label,nameField, lastNameField, emailField, passwordField, confirmPassField, confirmRegistrationButton);
        add(dialogLayout);
    }
    public void initButtonListener(){
        confirmRegistrationButton.addClickListener(e -> register(nameField.getValue(), lastNameField.getValue(), emailField.getValue(), passwordField.getValue(), confirmPassField.getValue()));
    }

    private void register(String firstName, String lastName, String email, String password, String confirmPassword) {
        if(firstName.trim().isEmpty()){
            Notification.show("Pole se jménem nesmí být prázdné");
        } else if (lastName.trim().isEmpty()) {
            Notification.show("Pole s příjmením nesmí být prázdné");
        } else if (email.isEmpty()) {
            Notification.show("Pole s emailem nesmí být prázdné");
        } else if(password.trim().length() < 6){
            Notification.show("Heslo musí mít alespoň 6 znaků");
        } else if (password.isEmpty()) {
            Notification.show("Pole s heslem nesmí být prázdné");
        } else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            Notification.show("Zopakujte zvolené heslo");
        } else {
            authService.register(firstName, lastName, email, password);
            Notification success = new Notification();
            success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            success.setDuration(7500);
            success.setText("Registrace proběhla úspěšně");
            success.open();
            String userName = firstName + " " + lastName;
            VaadinSession.getCurrent().setAttribute("loggedIn", true);
            VaadinSession.getCurrent().setAttribute("userName", userName);
            VaadinSession.getCurrent().setAttribute("currentUser", service.searchByEmail(email));
            VaadinSession.getCurrent().setAttribute("userId", service.getIdByEmail(email));
            close();
            UI.getCurrent().navigate("LoggedInView");

        }
    }
}
