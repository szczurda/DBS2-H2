package com.projekt.dbs.viewsAdmin;

import com.projekt.dbs.service.EntityManagementService;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "AdminView")
public class AdminMainView extends VerticalLayout {
    private HorizontalLayout labelLayout;
    private VerticalLayout pageLayout;
    private EntityManagementService service;
    
    private HorizontalLayout hl1, hl2, hl3, hl4;
    private Label label = new Label();

    public AdminMainView(EntityManagementService service) {
        this.service = service;
        configMainLayouts();
        add(labelLayout, pageLayout);

    }

    private void configMainLayouts() {
        label.setText("ADMIN");
        label.getStyle().set("text-align", "center").set("font-size", "40px").set("color", "white");
        labelLayout = new HorizontalLayout(label);
        labelLayout.setSizeFull();
        labelLayout.getStyle().set("background-color", "darkblue");
        labelLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        labelLayout.setAlignItems(Alignment.BASELINE);
        pageLayout = new VerticalLayout();
        pageLayout.setSizeFull();

    }


}
