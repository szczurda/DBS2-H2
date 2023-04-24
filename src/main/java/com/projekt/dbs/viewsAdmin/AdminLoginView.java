package com.projekt.dbs.viewsAdmin;

import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "AdminLogin")
@CssImport("EmployeeLogin.css")
public class AdminLoginView extends VerticalLayout{
    Label label = new Label("Přihlašovací formulář pro administrátora");
    PasswordField adminKeyField = new PasswordField("Admin key");
    Button confirmButton = new Button("Přihlásit se");

    Button switchLoginEntity = new Button();
    VerticalLayout layout = new VerticalLayout();
    EntityManagementService service;
    AuthService authService;

    @Autowired
    public AdminLoginView(EntityManagementService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
        initListener();
        setPadding(false);
        getStyle().set("background-color", "darkblue");
        label.getStyle().set("font-size", "30px");
        setId("page");
        switchLoginEntity.setIcon(new Icon(VaadinIcon.REFRESH));
        adminKeyField.getElement().setAttribute("autocomplete", "new-password");
        adminKeyField.setWidth("50%");
        adminKeyField.setId("pass-field");
        adminKeyField.setPrefixComponent(new Icon(VaadinIcon.KEY));
        confirmButton.setWidth("40%");
        confirmButton.getStyle().set("background-color", "darkblue");
        switchLoginEntity.setId("swap-button");
        switchLoginEntity.addClickListener(e -> {
            UI.getCurrent().navigate("EmployeeLogin");
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setAlignItems(Alignment.CENTER);
        setHeightFull();
        setWidthFull();
        initLayout();
        add(layout);
    }

    private void initLayout() {
        layout.add(switchLoginEntity, label, adminKeyField, confirmButton);
        layout.setWidth("60%");
        layout.setAlignItems(Alignment.CENTER);
        layout.getStyle().set("background-color", "ghostwhite");
        layout.setHeightFull();
        layout.getStyle().set("padding-top", "100px");
    }

    private void initListener(){
        confirmButton.addClickListener(e -> {
            if(adminKeyField.isEmpty()){
                Notification.show("Pole s klíčem nesmí být prázdné").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else if(authService.authenticate(adminKeyField.getValue())){
                UI.getCurrent().navigate("");
            } else{
                Notification.show("Nesprávný klíč").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }
}
