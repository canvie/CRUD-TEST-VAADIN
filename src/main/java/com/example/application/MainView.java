package com.example.application;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "MasterData", layout = MainLayout.class)
@PageTitle("Master Data")
public class MainView extends VerticalLayout {

    private Grid<User> userGrid;
    private TextField tfId;
    private TextField tfUsername;
    private PasswordField tfPassword1;
  //Confirm Password
    private PasswordField tfPassword2;
    private TextField tfRole;
    private com.vaadin.flow.component.button.Button btnSubmit;
    private com.vaadin.flow.component.button.Button btnAdd;
    private com.vaadin.flow.component.button.Button btnUpdate;
    private com.vaadin.flow.component.button.Button btnDelete;

    private final transient UserRepository userRepository;

    public MainView(UserRepository userRepository) {
        this.userRepository = userRepository;
        setupComponents();
//        setupListeners();
    }

    public void setupComponents() {
        // Add components here
        tfId = new TextField("Id");
        tfUsername = new TextField("Username");
        tfPassword1 = new PasswordField("Password");
        tfRole = new TextField("Role");

        // Setting Component properties
        tfId.setReadOnly(true);

        //Inisialisasi Title
        H2 title = new H2("Submit New User");
        setAlignSelf(Alignment.CENTER, title);
        add(title);

        //Inisialisasi Form Layout
        final FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        setAlignSelf(Alignment.START, formLayout);

        //Inisialisasi Button
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        btnAdd = new Button("Add");
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdd.addClickListener(e -> openForm(new User()));
        buttonLayout.add(btnAdd);
        add(buttonLayout);

        //Inisialisasi Grid
        userGrid = new Grid<>(User.class, false);
        userGrid.addColumn(User::getId).setHeader("Id").setVisible(false);
        userGrid.addColumn(User::getUsername).setHeader("Username");
        userGrid.addColumn(User::getPassword).setHeader("Password");
        userGrid.addColumn(User::getRole).setHeader("Role");
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            btnUpdate = new Button("Update", clickEvent -> {
                showDialog(user);
                System.out.println("Button Updated clicked for user: " + user.getUsername());
            });
            return btnUpdate;
        })).setHeader("Actions");
        userGrid.addColumn(new ComponentRenderer<>(user -> {
            btnDelete = new Button("Delete", clickEvent -> {
                deleteUser(user);
            });
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return btnDelete;
        }));
        final List<User> userList = userRepository.findAll();
        userGrid.setItems(userList);
        add(userGrid);

    }

    private void openForm(User user) {
        Dialog dialog = new com.vaadin.flow.component.dialog.Dialog();

        tfUsername = new TextField("Username");
        tfPassword1 = new PasswordField("Password");
        tfPassword2 = new PasswordField("Confirm Password");
        tfRole = new TextField("Role");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.add(tfUsername);
        formLayout.add(tfPassword1);
        formLayout.add(tfPassword2);
        formLayout.add(tfRole);

        Button saveButton = new Button("Save", event -> {
            if (!tfPassword1.getValue().equals(tfPassword2.getValue())) {
                Notification.show("Passwords do not match",3000, Notification.Position.MIDDLE);
            }else {
                user.setUsername(tfUsername.getValue());
                user.setPassword(tfPassword1.getValue());
                user.setRole(tfRole.getValue());
                userRepository.save(user);
                userGrid.setItems(userRepository.findAll()); // Refresh grid
                dialog.close();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button closeButton = new Button("Close", event -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, saveButton, closeButton);
        dialog.add(dialogLayout);

        dialog.open();
    }

    private void showDialog(User user) {
        Dialog dialog = new Dialog();

        tfUsername.setValue(user.getUsername());
        tfPassword1.setValue(user.getPassword());
        tfPassword2.setValue(user.getPassword());
        tfRole.setValue(user.getRole());

        Button saveButton = new Button("Save", event -> {
            if (!tfPassword1.getValue().equals(tfPassword2.getValue())) {
                Notification.show("Passwords do not match");
            }else {
                user.setUsername(tfUsername.getValue());
                user.setPassword(tfPassword1.getValue());
                user.setRole(tfRole.getValue());
                userRepository.save(user);
                userGrid.setItems(userRepository.findAll()); // Refresh grid to show updated data
                dialog.close();
                Notification.show("User updated successfully!");
            }
        });

        Button closeButton = new Button("Close", event -> dialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, closeButton);
        FormLayout formLayout = new FormLayout(tfUsername, tfPassword1,tfPassword2, tfRole);
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void deleteUser(User user) {
        userRepository.delete(user);  // Delete the user from the database
        userGrid.setItems(userRepository.findAll()); // Refresh grid to show updated data
    }

}
