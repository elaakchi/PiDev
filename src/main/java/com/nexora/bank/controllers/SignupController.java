package com.nexora.bank.controllers;

import com.nexora.bank.SceneRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label lblMessage;

    @FXML
    private void initialize() {
        lblMessage.setVisible(false);
        lblMessage.setManaged(false);
    }

    @FXML
    private void handleSignup() {
        String fullName = value(txtFullName);
        String email = value(txtEmail);
        String phone = value(txtPhone);
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText() == null ? "" : txtConfirmPassword.getText();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please complete all fields.");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Account created");
        alert.setContentText("Your account was created successfully. Please login.");
        alert.show();

        SceneRouter.show("/fxml/Login.fxml", "NEXORA BANK - Login", 1200, 760, 980, 680);
    }

    @FXML
    private void openHome() {
        SceneRouter.show("/fxml/Home.fxml", "NEXORA BANK - Welcome", 1200, 760, 980, 680);
    }

    @FXML
    private void openLogin() {
        SceneRouter.show("/fxml/Login.fxml", "NEXORA BANK - Login", 1200, 760, 980, 680);
    }

    private String value(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private void showError(String message) {
        lblMessage.setText(message);
        lblMessage.setVisible(true);
        lblMessage.setManaged(true);
    }
}
