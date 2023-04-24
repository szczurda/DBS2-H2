package com.projekt.dbs.viewsEmployee;

import com.projekt.dbs.service.AuthService;
import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.Key;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "EmployeeLogin")
@CssImport("EmployeeLogin.css")
public class EmployeeLoginView extends VerticalLayout {
    private Label label = new Label("Přihlašovací formulář pro zaměstnance");
    private NumberField idField = new NumberField("Číslo zaměstnance");
    private PasswordField passwordField = new PasswordField("Heslo");
    private Button confirmButton = new Button("Přihlásit se");
    private Button switchLoginEntity = new Button();
    private VerticalLayout layout = new VerticalLayout();

    EntityManagementService service;

    AuthService authService;

    @Autowired
    public EmployeeLoginView(EntityManagementService service, AuthService authService) {
        this.service = service;
        this.authService = authService;
        initListener();
        setPadding(false);
        getStyle().set("background-color", "royalblue");
        label.getStyle().set("font-size", "30px");
        setId("page");
        switchLoginEntity.setIcon(new Icon(VaadinIcon.REFRESH));
        idField.setWidth("50%");
        idField.setPrefixComponent(new Icon(VaadinIcon.USER));
        passwordField.getElement().setAttribute("autocomplete", "new-password");
        passwordField.setWidth("50%");
        passwordField.setId("pass-field");
        passwordField.setPrefixComponent(new Icon(VaadinIcon.KEY));
        confirmButton.setWidth("30%");
        confirmButton.getStyle().set("background-color", "royalblue");
        confirmButton.addClickShortcut(Key.ENTER);
        switchLoginEntity.setId("swap-button");
        switchLoginEntity.addClickListener(e -> {
            UI.getCurrent().navigate("AdminLogin");
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setAlignItems(Alignment.CENTER);
        setHeightFull();
        setWidthFull();
        initLayout();
        add(layout);
    }

    private void initLayout(){
        layout.add(switchLoginEntity, label, idField, passwordField, confirmButton);
        layout.setWidth("60%");
        layout.setAlignItems(Alignment.CENTER);
        layout.getStyle().set("background-color", "ghostwhite");
        layout.setHeightFull();
        layout.getStyle().set("padding-top", "100px");
    }

    private void initListener(){
        confirmButton.addClickListener(e -> {
            if(idField.isEmpty()){
                Notification.show("Pole s ID zaměstnance nesmí být prázdné").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else if(passwordField.isEmpty()){
                Notification.show("Pole s heslem zaměstnance nesmí být prázdné").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }else if(authService.authenticate(idField.getValue().intValue(), passwordField.getValue())){
                VaadinSession.getCurrent().setAttribute("employee", idField.getValue().intValue());
                if(VaadinSession.getCurrent().getAttribute("currentUser") != null){
                    VaadinSession.getCurrent().setAttribute("currentUser", null);
                    VaadinSession.getCurrent().setAttribute("userName", null);
                    VaadinSession.getCurrent().setAttribute("userId", null);
                }
               UI.getCurrent().navigate("EmployeeViewBooks");
           } else{
                Notification.show("Nesprávné přihlašovací údaje").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

/*    public void test() throws Exception{
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/testdb2");
        PreparedStatement statement = connection.prepareStatement("SELECT FROM EMPLOYEE WHERE ID = ?");
        statement.setLong(1, 6);
        boolean hasResultSet = statement.execute();
        if(hasResultSet){
            ResultSet result = statement.getResultSet();
            while (result.next()){

            }
            result.close();
            connection.close();
            statement.close();
        }

    }*/

}
