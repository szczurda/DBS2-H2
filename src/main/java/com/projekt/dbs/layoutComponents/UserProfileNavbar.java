package com.projekt.dbs.layoutComponents;

import com.projekt.dbs.viewsUser.UserProfileView;
import com.projekt.dbs.viewsUser.UserReturnedBooksView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;


public class UserProfileNavbar extends HorizontalLayout {
    Tabs userProfileTabs = new Tabs();
    Button homeButton = new Button("Zpět na katalog");

    public UserProfileNavbar() {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        add(homeButton, createTabs());
        setVerticalComponentAlignment(Alignment.CENTER);
        homeButton.getStyle().set("position", "absolute").set("top", "10px").set("left", "20px");
    }

    public Tabs createTabs() {
        userProfileTabs.addClassName("tabs");
        userProfileTabs.getStyle().set("display", "flex").set("justify-content", "center");
        userProfileTabs.setWidthFull();
        Tab profileTab = createTab("Profil", UserProfileView.class);
        Tab borrowedBooksTab = createTab("Vrácené knihy", UserReturnedBooksView.class);
        setAlignItems(Alignment.CENTER);
        userProfileTabs.add(profileTab, borrowedBooksTab);
        return userProfileTabs;
    }


    private Tab createTab(String name, Class<? extends Component> path) {
        RouterLink link = new RouterLink();
        link.add(name);
        link.setRoute(path);
        return new Tab(link);
    }

    public Tabs getUserProfileTabs() {
        return userProfileTabs;
    }
}
