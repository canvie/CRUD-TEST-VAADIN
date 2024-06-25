package com.example.application;

import com.example.application.User;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createDrawer();
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("My Application");
        logo.addClassNames("text-l", "m-m");


        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logo);
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout navbar = new VerticalLayout();

        RouterLink masterData = new RouterLink("Master Data", MainView.class);
        Anchor logout = new Anchor("/login", "Log out");

        navbar.add(masterData,logout);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);

        addToDrawer(navbar);
    }
}
