package com.example.application.views;

import com.example.application.data.User;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    private TextField username;
    private PasswordField password;
    private Button loginButton;

    private final UserService userService;

    public LoginView(UserService userService) {
        this.userService = userService;

        VerticalLayout layout = new VerticalLayout();

        username = new TextField("Username");
        password = new PasswordField("Password");
        loginButton = new Button("Login");

        loginButton.addClickListener(e -> {
            String user = username.getValue();
            String pass = password.getValue();

            if (authenticate(user, pass)) {
                // Jika login berhasil, redirect ke halaman masterdata
                getUI().ifPresent(ui -> ui.navigate("MasterData"));
            } else {
                Notification.show("Invalid username or password",3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        layout.setSizeFull();
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(username, password, loginButton);


        add(layout);
    }

    private boolean authenticate(String username, String password) {
        User user = userService.findByUsername(username).orElse(null);
        return user != null && user.getPassword().equals(password);
    }
}
