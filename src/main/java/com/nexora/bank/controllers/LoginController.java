package com.nexora.bank.controllers;

import com.nexora.bank.SceneRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;

    @FXML
    private void initialize() {
        lblMessage.setVisible(false);
        lblMessage.setManaged(false);
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill email and password.");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }

        SceneRouter.show("/fxml/MainView.fxml", "NEXORA BANK - Systeme de Gestion Bancaire", 1400, 900, 1200, 800);
    }

    @FXML
    private void openHome() {
        SceneRouter.show("/fxml/Home.fxml", "NEXORA BANK - Welcome", 1200, 760, 980, 680);
    }

    @FXML
    private void openSignup() {
        SceneRouter.show("/fxml/Signup.fxml", "NEXORA BANK - Sign Up", 1200, 760, 980, 680);
    }

    private void showError(String message) {
        lblMessage.setText(message);
        lblMessage.setVisible(true);
        lblMessage.setManaged(true);
    }
}
