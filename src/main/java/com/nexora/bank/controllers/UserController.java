package com.nexora.bank.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML private Label lblTotalUsers, lblUsersActifs, lblAdmins;
    @FXML private TextField txtNom, txtPrenom, txtEmail, txtTelephone, txtRecherche;
    @FXML private ComboBox<String> cmbRole, cmbStatut;
    @FXML private Button btnAjouter;
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colNom, colPrenom, colEmail, colTelephone, colRole, colStatut;
    @FXML private TableColumn<User, Void> colActions;
    @FXML private Label lblTableInfo;

    private ObservableList<User> usersList = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;
    private User selectedUser = null;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTable();
        initializeSearch();
        loadSampleData();
        updateStats();
    }

    private void initializeTable() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        colStatut.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                Label badge = new Label(item);
                badge.getStyleClass().add("nx-badge");
                switch (item) {
                    case "Actif": badge.getStyleClass().add("nx-badge-success"); break;
                    case "Inactif": badge.getStyleClass().add("nx-badge-warning"); break;
                    case "Suspendu": badge.getStyleClass().add("nx-badge-error"); break;
                }
                setGraphic(badge);
            }
        });

        colRole.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                Label badge = new Label(item);
                badge.getStyleClass().add("nx-badge");
                if (item.equals("Administrateur")) badge.getStyleClass().add("nx-badge-purple");
                else badge.getStyleClass().add("nx-badge-info");
                setGraphic(badge);
            }
        });

        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button edit = new Button(), delete = new Button();
            private final HBox box = new HBox(8, edit, delete);
            {
                edit.getStyleClass().addAll("nx-table-action", "nx-table-action-edit");
                delete.getStyleClass().addAll("nx-table-action", "nx-table-action-delete");
                SVGPath eIcon = new SVGPath(), dIcon = new SVGPath();
                eIcon.setContent("M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z");
                dIcon.setContent("M19 7l-.867 12.142A2 2 0 0 1 16.138 21H7.862a2 2 0 0 1-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v3M4 7h16");
                eIcon.getStyleClass().add("nx-action-icon"); dIcon.getStyleClass().add("nx-action-icon");
                edit.setGraphic(eIcon); delete.setGraphic(dIcon);
                box.setAlignment(Pos.CENTER);
                edit.setOnAction(e -> editUser(getTableView().getItems().get(getIndex())));
                delete.setOnAction(e -> deleteUser(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : box); }
        });

        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) { selectedUser = n; populateForm(n); isEditMode = true; btnAjouter.setText("Modifier"); }
        });
    }

    private void initializeSearch() {
        filteredData = new FilteredList<>(usersList, p -> true);
        txtRecherche.textProperty().addListener((obs, o, n) -> {
            filteredData.setPredicate(u -> n == null || n.isEmpty() ||
                u.getNom().toLowerCase().contains(n.toLowerCase()) ||
                u.getEmail().toLowerCase().contains(n.toLowerCase()));
            updateTableInfo();
        });
        SortedList<User> sorted = new SortedList<>(filteredData);
        sorted.comparatorProperty().bind(tableUsers.comparatorProperty());
        tableUsers.setItems(sorted);
    }

    private void loadSampleData() {
        usersList.addAll(
            new User("Ben Ali", "Ahmed", "ahmed.benali@nexora.tn", "+216 98 123 456", "Administrateur", "Actif"),
            new User("Trabelsi", "Fatma", "fatma.trabelsi@nexora.tn", "+216 55 789 012", "Gestionnaire", "Actif"),
            new User("Gharbi", "Mohamed", "m.gharbi@nexora.tn", "+216 22 345 678", "Agent", "Actif"),
            new User("Sassi", "Sarra", "sarra.sassi@nexora.tn", "+216 99 456 789", "Client", "Inactif"),
            new User("Mejri", "Karim", "k.mejri@nexora.tn", "+216 50 567 890", "Agent", "Suspendu"),
            new User("Hammami", "Leila", "leila.hammami@nexora.tn", "+216 21 654 321", "Gestionnaire", "Actif"),
            new User("Ben Youssef", "Nour", "nour.benyoussef@nexora.tn", "+216 93 210 987", "Client", "Actif"),
            new User("Chouikha", "Rami", "rami.chouikha@nexora.tn", "+216 29 871 234", "Agent", "Actif"),
            new User("Khemiri", "Wassim", "wassim.khemiri@nexora.tn", "+216 27 555 111", "Client", "Suspendu"),
            new User("Zouari", "Amira", "amira.zouari@nexora.tn", "+216 26 333 444", "Gestionnaire", "Inactif")
        );
        updateTableInfo();
    }

    private void updateStats() {
        lblTotalUsers.setText(String.valueOf(usersList.size()));
        lblUsersActifs.setText(String.valueOf(usersList.stream().filter(u -> u.getStatut().equals("Actif")).count()));
        lblAdmins.setText(String.valueOf(usersList.stream().filter(u -> u.getRole().equals("Administrateur")).count()));
    }

    private void updateTableInfo() {
        lblTableInfo.setText(String.format("Affichage de %d sur %d entrées", filteredData.size(), usersList.size()));
    }

    private void populateForm(User u) {
        txtNom.setText(u.getNom()); txtPrenom.setText(u.getPrenom());
        txtEmail.setText(u.getEmail()); txtTelephone.setText(u.getTelephone());
        cmbRole.setValue(u.getRole()); cmbStatut.setValue(u.getStatut());
    }

    private void clearForm() {
        txtNom.clear(); txtPrenom.clear(); txtEmail.clear(); txtTelephone.clear();
        cmbRole.setValue(null); cmbStatut.setValue(null);
        selectedUser = null; isEditMode = false; btnAjouter.setText("Ajouter");
        tableUsers.getSelectionModel().clearSelection();
    }

    @FXML private void handleAjouter() {
        String nom = txtNom.getText(), prenom = txtPrenom.getText(), email = txtEmail.getText();
        String tel = txtTelephone.getText(), role = cmbRole.getValue(), statut = cmbStatut.getValue();
        if (nom.isEmpty() || email.isEmpty() || role == null || statut == null) {
            showAlert("Remplissez les champs obligatoires."); return;
        }
        if (isEditMode && selectedUser != null) {
            selectedUser.setNom(nom); selectedUser.setPrenom(prenom); selectedUser.setEmail(email);
            selectedUser.setTelephone(tel); selectedUser.setRole(role); selectedUser.setStatut(statut);
            tableUsers.refresh();
        } else {
            usersList.add(new User(nom, prenom, email, tel, role, statut));
        }
        clearForm(); updateStats(); updateTableInfo();
    }

    @FXML private void handleSupprimer() { if (selectedUser != null) deleteUser(selectedUser); }
    @FXML private void handleAnnuler() { clearForm(); }
    private void editUser(User u) { selectedUser = u; populateForm(u); isEditMode = true; btnAjouter.setText("Modifier"); }
    private void deleteUser(User u) {
        if (new Alert(Alert.AlertType.CONFIRMATION, "Supprimer?", ButtonType.OK, ButtonType.CANCEL).showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            usersList.remove(u); clearForm(); updateStats(); updateTableInfo();
        }
    }
    @FXML private void exporterPDF() { showAlert("En développement."); }
    @FXML private void envoyerSMS() { showAlert("En développement."); }
    private void showAlert(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }

    public static class User {
        private String nom, prenom, email, telephone, role, statut;
        public User(String nom, String prenom, String email, String telephone, String role, String statut) {
            this.nom = nom; this.prenom = prenom; this.email = email;
            this.telephone = telephone; this.role = role; this.statut = statut;
        }
        public String getNom() { return nom; } public void setNom(String v) { this.nom = v; }
        public String getPrenom() { return prenom; } public void setPrenom(String v) { this.prenom = v; }
        public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
        public String getTelephone() { return telephone; } public void setTelephone(String v) { this.telephone = v; }
        public String getRole() { return role; } public void setRole(String v) { this.role = v; }
        public String getStatut() { return statut; } public void setStatut(String v) { this.statut = v; }
    }
}
