package com.projekt.dbs.service;


import com.projekt.dbs.entities.Employee;
import com.projekt.dbs.entities.Role;
import com.projekt.dbs.entities.User;
import com.projekt.dbs.viewsEmployee.EmployeeBookManagerView;
import com.projekt.dbs.viewsUser.LoggedInView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    public record AuthorizedRoute(String route, String name, Class<? extends Component> view) {

    }

    EntityManagementService service;

    @Autowired
    public AuthService(EntityManagementService service) {
        this.service = service;
    }

    public boolean authenticate(String email, String password){
        User user = service.searchByEmail(email);
        return user != null && user.checkPassword(password);
    }

    public boolean authenticate(Long employeeId, String password){
        Employee employee = service.findEmployeeById(employeeId);
        return employee != null && employee.getPassword().equals(password);
    }

    public boolean authenticate(String key){
        return key.equals("tulen");
    }

    public boolean authenticate(Integer employeeId, String password){
        Employee employee = service.findEmployeeByIntId(employeeId);
        return employee != null && employee.getPassword().equals(password);
    }


    private void createRoutes(Role role) {
        getAuthorizedRoutes(role).stream()
                .forEach(route ->
                        RouteConfiguration.forSessionScope().setRoute(route.
                                route, LoggedInView.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {
        var routes = new ArrayList<AuthorizedRoute>();

        if (role.equals(Role.USER)) {
            routes.add(new AuthorizedRoute("home", "Home", LoggedInView.class));

        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("home", "Home", LoggedInView.class));
            routes.add(new AuthorizedRoute("admin", "Admin", EmployeeBookManagerView.class));
        }

        return routes;
    }

    public void register(String firstName, String lastName, String email, String password){
            service.addUser(new User(firstName, lastName, email, password));

        }

    }
