package com.example.sqlbank;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    private Stage primaryStage;
    private DataBase dataBase;
    int sessionID = 1;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        this.primaryStage.setTitle("sqlbank App");
        showDatabaseSetupScreen();
    }

    private void showDatabaseSetupScreen() {
        GridPane root = createRootPane();
        TextField nameField = createTextField("Database Name");
        TextField userField = createTextField("Username");
        PasswordField passwordField = createPasswordField();
        Button continueButton = createContinueButton();
        Label titleLabel = createLabel("Database Setup");
        root.add(titleLabel, 0, 0, 2, 1);
        root.add(nameField, 0, 1);
        root.add(userField, 0, 2);
        root.add(passwordField, 0, 3);
        root.add(continueButton, 0, 4);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        continueButton.setOnAction(event -> {
            String name = nameField.getText();
            String user = userField.getText();
            String password = passwordField.getText();
            try {
                dataBase = new DataBase(name, user, password);
                showLoginScreen();
            } catch (SQLException e) {
                titleLabel.setText("ERROR. TRY AGAIN");
            }

        });
    }

    private void showLoginScreen() {
        GridPane root = createRootPane();
        TextField loginField = createTextField("Login");
        PasswordField passwordField = createPasswordField();
        Button continueButton = createContinueButton();
        Button registrationButton = createRegistrationButton();
        registrationButton.setOnAction(event -> {
            showRegistrationScreenWithPasport();
        });
        Label loginLabel = createLabel("Please log in");
        root.add(loginLabel, 0, 0, 2, 1);
        root.add(loginField, 0, 1);
        root.add(passwordField, 0, 2);
        root.add(continueButton, 0, 3);
        root.add(registrationButton, 0, 4);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        continueButton.setOnAction(event -> {
            String login = loginField.getText();
            String password = passwordField.getText();
            sessionID = checkLogin(login, password);
            if (sessionID != -1) {
                showPersonalAccountScreenAccounts();
            } else {
                loginField.clear();
                passwordField.clear();
                loginLabel.setText("Wrong login or password");
            }
        });
    }

    private Button createRegistrationButton() {
        Button registrationButton = new Button("Registration");
        registrationButton.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(registrationButton, HPos.CENTER);
        return registrationButton;
    }

    private void showRegistrationScreenWithPasport() {
        GridPane root = createRootPane();
        TextField loginName = createTextField("Name");
        TextField loginSurname = createTextField("Surname");
        TextField loginSecondName = createTextField("Second name");
        TextField email = createTextField("Email");
        TextField passwordField = createTextField("Password");
        TextField phoneNumber = createTextField("Phone number");
        TextField Address = createTextField("Address");
        TextField CorrAccount = createTextField("Correspondent account");
        TextField passportNumber = createTextField("Passport");
        DatePicker birthday = new DatePicker();
        birthday.setPromptText("Birthday");


        Button continueButton = createContinueButton();
        Button loginButton = createLoginButton();
        loginButton.setOnAction(event -> {
            showLoginScreen();
        });
        Label loginLabel = createLabel("Registration");
        root.add(loginLabel, 0, 0, 2, 1);
        root.add(loginName, 0, 1);
        root.add(loginSurname, 0, 2);
        root.add(loginSecondName, 0, 3);
        root.add(email, 0, 4);
        root.add(passwordField, 0, 5);
        root.add(phoneNumber, 0, 6);
        root.add(Address, 0, 7);
        root.add(CorrAccount, 0, 8);
        root.add(passportNumber, 0, 9);
        root.add(birthday, 0, 10);

        root.add(continueButton, 0, 11);
        root.add(loginButton, 0, 12);

        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        continueButton.setOnAction(event -> {
            String name = loginName.getText();
            String surname = loginSurname.getText();
            String secondName = loginSecondName.getText();
            String email1 = email.getText();
            String password = passwordField.getText();
            String phone = phoneNumber.getText();
            String address = Address.getText();
            String corrAccount = CorrAccount.getText();
            String passport = passportNumber.getText();
            String birthday1 = birthday.getValue().toString();


            try {
                dataBase.addNewClient(name, surname, secondName, email1,
                password, phone, address, corrAccount, passport, birthday1);
                showLoginScreen();
            } catch (Exception e) {
                String s = e.getMessage();
                loginLabel.setText("error. Try again");
            }

        });
    }

    Button createLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(loginButton, HPos.CENTER);
        return loginButton;
    }
    private void showSendingScreen(String accountIBAN, int currencyID) {

        GridPane root = createRootPane();

        Label mainText = createLabel("Send money to another account");
        TextField ibanTextField = createTextField("Recipient IBAN");
        ibanTextField.getStyleClass().add("custom-text-field");

        TextField amountTextField = createTextField("Amount");
        amountTextField.getStyleClass().add("custom-text-field");

        TextField descriptionTextField = createTextField("Description");
        descriptionTextField.getStyleClass().add("custom-text-field");

        CheckBox commissionCheckBox = new CheckBox("Is commission paid by receiver?");
        commissionCheckBox.getStyleClass().add("custom-check-box");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("custom-button");
        sendButton.setOnAction(actionEvent -> {
            try {
                float amount;
                try {
                    amount = Float.parseFloat(amountTextField.getText());
                } catch (NumberFormatException e) {
                    amount = 0;
                }
                if (amount == 0) {
                    mainText.setText("Invalid amount. Try again.");
                    return;
                }
                if(ibanTextField.getText().length() == 0) {
                    mainText.setText("Invalid IBAN. Try again.");
                    return;
                }
                dataBase.sendMoney(accountIBAN, ibanTextField.getText(), amount, currencyID,
                        descriptionTextField.getText(), commissionCheckBox.isSelected());
                showPersonalAccountScreenAccounts();
            } catch (Exception e) {
                mainText.setText("Error. Try again.");
            }
        });
        sendButton.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(sendButton, HPos.CENTER);

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("custom-button");
        backButton.setOnAction(actionEvent -> {
            showPersonalAccountScreenAccounts();
        });
        root.add(mainText, 0, 1);
        root.add(ibanTextField, 0, 2);
        root.add(amountTextField, 0, 3);
        root.add(descriptionTextField, 0, 4);
        root.add(commissionCheckBox, 0, 5);
        root.add(sendButton, 0, 6);
        root.add(backButton, 0, 6);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    int accountID2 = 0, cardID2 = 0;
    private void showPersonalAccountScreenAccounts() {
        GridPane root = createRootPane();
        Button exitButton = createBackButton();
        String name = dataBase.getName(sessionID);
        Label nameLabel = createLabel(name);
        HBox buttons = buttonsPane();

        HBox userInfoBox = new HBox(10, nameLabel, exitButton);
        userInfoBox.setAlignment(Pos.CENTER_RIGHT);
        userInfoBox.setPadding(new Insets(10));
        GridPane.setHalignment(userInfoBox, HPos.RIGHT);
        TilePane accountsPane = createAccountsPane();
        ScrollPane transaction = createTransactionsPane();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        GridPane.setMargin(accountsPane, new Insets(10, 0, 0, 0));
        Label accountLabel = createLabel("Your Accounts");
        Label transactionHistoryLabel = createLabel("Transaction History");
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);
        root.add(buttons,0,0);
        root.add(userInfoBox, 1, 0);
        root.add(accountLabel, 0, 1, 2, 1);
        root.add(accountsPane, 0, 2, 2, 1);
        root.add(transactionHistoryLabel, 0, 3, 2, 1);
        root.add(transaction, 0, 4, 2, 1);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        exitButton.setOnAction(event -> showLoginScreen());
    }
    private void showPersonalAccountScreenCards() {
        GridPane root = createRootPane();
        Button exitButton = createBackButton();
        String name = dataBase.getName(sessionID);
        Label nameLabel = createLabel(name);
        HBox buttons = buttonsPane();

        HBox userInfoBox = new HBox(10, nameLabel, exitButton);
        userInfoBox.setAlignment(Pos.CENTER_RIGHT);
        userInfoBox.setPadding(new Insets(10));
        GridPane.setHalignment(userInfoBox, HPos.RIGHT);
        TilePane cardsPane = createCardsPane();
        ScrollPane transaction = createTransactionsPaneByCard();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        GridPane.setMargin(cardsPane, new Insets(10, 0, 0, 0));
        Label accountLabel = createLabel("Your Cards");
        Label transactionHistoryLabel = createLabel("Transaction History");
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);
        root.add(buttons,0,0);
        root.add(userInfoBox, 1, 0);
        root.add(accountLabel, 0, 1, 2, 1);
        root.add(cardsPane, 0, 2, 2, 1);
        root.add(transactionHistoryLabel, 0, 3, 2, 1);
        root.add(transaction, 0, 4, 2, 1);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        exitButton.setOnAction(event -> showLoginScreen());
    }




    private void showPersonalAccountScreenLoans() {
        GridPane root = createRootPane();
        Button exitButton = createBackButton();
        String name = dataBase.getName(sessionID);
        Label nameLabel = createLabel(name);
        HBox buttons = buttonsPane();

        HBox userInfoBox = new HBox(10, nameLabel, exitButton);
        userInfoBox.setAlignment(Pos.CENTER_RIGHT);
        userInfoBox.setPadding(new Insets(10));
        GridPane.setHalignment(userInfoBox, HPos.RIGHT);
        TilePane loansPane = createLoansPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        GridPane.setMargin(loansPane, new Insets(10, 0, 0, 0));
        Label accountLabel = createLabel("Your Loans");
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);
        root.add(buttons,0,0);
        root.add(userInfoBox, 1, 0);
        root.add(accountLabel, 0, 1, 2, 1);
        root.add(loansPane, 0, 2, 2, 1);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        exitButton.setOnAction(event -> showLoginScreen());
    }

    private void showPersonalAccountScreenDeposits() {
        GridPane root = createRootPane();
        Button exitButton = createBackButton();
        String name = dataBase.getName(sessionID);
        Label nameLabel = createLabel(name);
        HBox buttons = buttonsPane();

        HBox userInfoBox = new HBox(10, nameLabel, exitButton);
        userInfoBox.setAlignment(Pos.CENTER_RIGHT);
        userInfoBox.setPadding(new Insets(10));
        GridPane.setHalignment(userInfoBox, HPos.RIGHT);
        TilePane depositsPane = createDepositsPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        root.getColumnConstraints().addAll(columnConstraints, columnConstraints);
        GridPane.setMargin(depositsPane, new Insets(10, 0, 0, 0));
        Label accountLabel = createLabel("Your Deposits");
        root.setAlignment(Pos.TOP_RIGHT);
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);
        root.add(buttons,0,0);
        root.add(userInfoBox, 1, 0);
        root.add(accountLabel, 0, 1, 2, 1);
        root.add(depositsPane, 0, 2, 2, 1);
        Scene scene = createScene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        exitButton.setOnAction(event -> showLoginScreen());
    }
    private HBox buttonsPane() {
        HBox buttonsPane = new HBox(10);
        Button showTransactionsButton = new Button("Accounts");
        showTransactionsButton.setOnAction(event -> showPersonalAccountScreenAccounts());
        Button showCardsButton = new Button("Cards");
        showCardsButton.setOnAction(event -> showPersonalAccountScreenCards());
        Button showLoansButton = new Button("Loans Info");
        showLoansButton.setOnAction(event -> showPersonalAccountScreenLoans());

        Button showDepositsButton = new Button("Deposits Info");
        showDepositsButton.setOnAction(event -> showPersonalAccountScreenDeposits());

        Button showPesonalButton = new Button("Information");
        showPesonalButton.setOnAction(event -> showPersonalInformation());

        buttonsPane.getChildren().addAll(showTransactionsButton, showCardsButton, showLoansButton, showDepositsButton,
                showPesonalButton);
        return buttonsPane;
    }

    private void showTransactionDetails(int transactionID) {
        ResultSet res = dataBase.getTransactionDetails(transactionID);
        try {
            if (res.next()) {
                int transactionId = res.getInt("id");
                String dateTime = res.getString("date_time");
                double amount = res.getDouble("amount");
                String description = res.getString("description");
                int fromAccountId = res.getInt("from_account_id");
                int toAccountId = res.getInt("to_account_id");

                GridPane root = createRootPane();
                Button exitButton = createBackButton();
                Label transactionIdLabel = createLabel("Transaction ID: ");
                Label dateTimeLabel = createLabel("Date and Time: ");
                Label amountLabel = createLabel("Amount: ");
                Label descriptionLabel = createLabel("Description: ");
                Label fromAccountLabel = createLabel("From Account: ");
                Label toAccountLabel = createLabel("To Account: ");

                Label transactionIdLabel2;
                Label dateTimeLabel2;
                Label amountLabel2;
                Label descriptionLabel2;
                Label fromAccountLabel2;
                Label toAccountLabel2;
                if(transactionId != 0) transactionIdLabel2 = createLabel(String.valueOf(transactionId));
                else transactionIdLabel2 = createLabel("N/A");
                if(dateTime != null) dateTimeLabel2 = createLabel(dateTime);
                else dateTimeLabel2 = createLabel("N/A");
                if(amount != 0) amountLabel2 = createLabel(String.valueOf(amount));
                else amountLabel2 = createLabel("N/A");
                if(description != null) descriptionLabel2 = createLabel(description);
                else descriptionLabel2 = createLabel("N/A");
                if(fromAccountId != 0) fromAccountLabel2 = createLabel(String.valueOf(fromAccountId));
                else fromAccountLabel2 = createLabel("N/A");
                if(toAccountId != 0) toAccountLabel2 = createLabel(String.valueOf(toAccountId));
                else toAccountLabel2 = createLabel("N/A");


                HBox userInfoBox = new HBox(10, transactionIdLabel, exitButton);
                userInfoBox.setAlignment(Pos.CENTER_RIGHT);
                userInfoBox.setPadding(new Insets(10));

                GridPane.setHalignment(userInfoBox, HPos.RIGHT);
                GridPane.setHalignment(transactionIdLabel, HPos.LEFT);
                GridPane.setHalignment(dateTimeLabel, HPos.LEFT);
                GridPane.setHalignment(amountLabel, HPos.LEFT);
                GridPane.setHalignment(descriptionLabel, HPos.LEFT);
                GridPane.setHalignment(fromAccountLabel, HPos.LEFT);
                GridPane.setHalignment(toAccountLabel, HPos.LEFT);

                GridPane.setHalignment(transactionIdLabel2, HPos.LEFT);
                GridPane.setHalignment(dateTimeLabel2, HPos.LEFT);
                GridPane.setHalignment(amountLabel2, HPos.LEFT);
                GridPane.setHalignment(descriptionLabel2, HPos.LEFT);
                GridPane.setHalignment(fromAccountLabel2, HPos.LEFT);
                GridPane.setHalignment(toAccountLabel2, HPos.LEFT);


                root.add(userInfoBox, 1, 0);
                root.add(transactionIdLabel, 0, 1);
                root.add(dateTimeLabel, 0, 2);
                root.add(amountLabel, 0, 3);
                root.add(descriptionLabel, 0, 4);
                root.add(fromAccountLabel, 0, 5);
                root.add(toAccountLabel, 0, 6);

                root.add(transactionIdLabel2, 1, 1);
                root.add(dateTimeLabel2, 1, 2);
                root.add(amountLabel2, 1, 3);
                root.add(descriptionLabel2, 1, 4);
                root.add(fromAccountLabel2, 1, 5);
                root.add(toAccountLabel2, 1, 6);


                Scene scene = createScene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
                exitButton.setOnAction(event -> showPersonalAccountScreenAccounts());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showPersonalInformation() {
        ResultSet personalInfo = dataBase.getAccountInfo(sessionID);
        try {
            if (personalInfo.next()) {
                String firstName = personalInfo.getString("first_name");
                String lastName = personalInfo.getString("last_name");
                String secondName = personalInfo.getString("second_name");
                String phone = personalInfo.getString("phone");
                String email = personalInfo.getString("email");
                String passport = personalInfo.getString("passport");
                String pesel = personalInfo.getString("pesel");
                String address = personalInfo.getString("address");
                String correspondenceAddress = personalInfo.getString("correspondence_address");
                String birthDate = personalInfo.getString("birth_date");

                GridPane root = createRootPane();
                Button exitButton = createBackButton();
                Label firstNameLabel = createLabel("First Name: ");
                Label lastNameLabel = createLabel("Last Name: ");
                Label secondNameLabel = createLabel("Second Name: ");
                Label phoneLabel = createLabel("Phone: ");
                Label emailLabel = createLabel("Email: ");
                Label passportLabel = createLabel("Passport: ");
                Label peselLabel = createLabel("PESEL: ");
                Label addressLabel = createLabel("Address: ");
                Label correspondenceAddressLabel = createLabel("Correspondence Address: ");
                Label birthDateLabel = createLabel("Birth Date: ");

                Label idLabel2;
                Label firstNameLabel2;
                Label lastNameLabel2;
                Label secondNameLabel2;
                Label phoneLabel2;
                Label emailLabel2;
                Label passportLabel2;
                Label peselLabel2;
                Label addressLabel2;
                Label correspondenceAddressLabel2;
                Label birthDateLabel2;
                if (firstName != null) firstNameLabel2 = createLabel(firstName);
                else firstNameLabel2 = createLabel("N/A");
                if (lastName != null) lastNameLabel2 = createLabel(lastName);
                else lastNameLabel2 = createLabel("N/A");
                if (secondName != null) secondNameLabel2 = createLabel(secondName);
                else secondNameLabel2 = createLabel("N/A");
                if (phone != null) phoneLabel2 = createLabel(phone);
                else phoneLabel2 = createLabel("N/A");
                if (email != null) emailLabel2 = createLabel(email);
                else emailLabel2 = createLabel("N/A");
                if (passport != null) passportLabel2 = createLabel(passport);
                else passportLabel2 = createLabel("N/A");
                if (pesel != null) peselLabel2 = createLabel(pesel);
                else peselLabel2 = createLabel("N/A");
                if (address != null) addressLabel2 = createLabel(address);
                else addressLabel2 = createLabel("N/A");
                if (correspondenceAddress != null) correspondenceAddressLabel2 = createLabel(correspondenceAddress);
                else correspondenceAddressLabel2 = createLabel("N/A");
                if (birthDate != null) birthDateLabel2 = createLabel(birthDate);
                else birthDateLabel2 = createLabel("N/A");



                GridPane.setHalignment(firstNameLabel2, HPos.LEFT);
                GridPane.setHalignment(lastNameLabel2, HPos.LEFT);
                GridPane.setHalignment(secondNameLabel2, HPos.LEFT);
                GridPane.setHalignment(phoneLabel2, HPos.LEFT);
                GridPane.setHalignment(emailLabel2, HPos.LEFT);
                GridPane.setHalignment(passportLabel2, HPos.LEFT);
                GridPane.setHalignment(peselLabel2, HPos.LEFT);
                GridPane.setHalignment(addressLabel2, HPos.LEFT);
                GridPane.setHalignment(correspondenceAddressLabel2, HPos.LEFT);
                GridPane.setHalignment(birthDateLabel2, HPos.LEFT);
                GridPane.setHalignment(exitButton, HPos.LEFT);

                root.add(exitButton, 0, 0);
                root.add(firstNameLabel, 0, 2);
                root.add(lastNameLabel, 0, 3);
                root.add(secondNameLabel, 0, 4);
                root.add(phoneLabel, 0, 5);
                root.add(emailLabel, 0, 6);
                root.add(passportLabel, 0, 7);
                root.add(peselLabel, 0, 8);
                root.add(addressLabel, 0, 9);
                root.add(correspondenceAddressLabel, 0, 10);
                root.add(birthDateLabel, 0, 11);

                root.add(firstNameLabel2, 1, 2);
                root.add(lastNameLabel2, 1, 3);
                root.add(secondNameLabel2, 1, 4);
                root.add(phoneLabel2, 1, 5);
                root.add(emailLabel2, 1, 6);
                root.add(passportLabel2, 1, 7);
                root.add(peselLabel2, 1, 8);
                root.add(addressLabel2, 1, 9);
                root.add(correspondenceAddressLabel2, 1, 10);
                root.add(birthDateLabel2, 1, 11);
                root.setHgap(10);
                root.setVgap(10);
                root.setAlignment(Pos.CENTER);
                root.setPadding(new Insets(10));
                Scene scene = createScene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
                exitButton.setOnAction(event -> showPersonalAccountScreenAccounts());
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    ArrayList<Integer> accounts, cards, loans, deposits;

    private TilePane createAccountsPane() {
        TilePane accountsPane = new TilePane();
        accountsPane.setPadding(new Insets(10));
        accountsPane.setHgap(10);
        accountsPane.setVgap(10);
        accountsPane.setAlignment(Pos.CENTER_LEFT);
        Integer[] accountIDs = dataBase.getAccountIDs(sessionID);
        accounts = new ArrayList<>();
        for (int i = 1; i <= accountIDs.length; i++) {
            accounts.add(accountIDs[i - 1]);
            Rectangle accountRectangle = new Rectangle(270, 100);
            accountRectangle.setArcWidth(20);
            accountRectangle.setArcHeight(20);
            accountRectangle.getStyleClass().add("account-rectangle");


            String Iban = dataBase.getIban(accountIDs[i - 1]);
            Label accountNumberLabel = createLabel(Iban);


            accountNumberLabel.getStyleClass().add("account-number-label");
            Label balanceLabel = createLabel("Balance: " + dataBase.getBalance(accountIDs[i - 1]) +" "+
                    dataBase.getCurrencyCode(dataBase.getCurrencyID(accountIDs[i - 1])));
            balanceLabel.getStyleClass().add("balance-label");
            Button transferButton = new Button("Transfer");
            Button changeLimitButton = new Button("Change limit");
            transferButton.getStyleClass().add("transfer-button");
            balanceLabel.getStyleClass().add("balance-label");

            HBox contentBox = new HBox(10, balanceLabel, transferButton);
            contentBox.setAlignment(Pos.BASELINE_RIGHT);

            VBox accountBox = new VBox(10, accountNumberLabel, contentBox);
            accountBox.setAlignment(Pos.TOP_LEFT);
            accountBox.setPadding(new Insets(10));
            accountBox.getStyleClass().add("account-box");

            Pane completeAccountPane = new Pane(accountRectangle, accountBox);

            Integer I = i;
            completeAccountPane.setOnMouseEntered(event -> {
                accountRectangle.getStyleClass().add("account-rectangle-hover");
            });

            transferButton.setOnAction(event -> {
                showSendingScreen(Iban,dataBase.getCurrencyID(accountIDs[I - 1]));
            });
            completeAccountPane.setOnMouseExited(event -> {
                accountRectangle.getStyleClass().remove("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseClicked(event -> {
                accountID2 = I-1;
                showPersonalAccountScreenAccounts();
            });
            accountNumberLabel.setOnMouseClicked(event -> {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(Iban);
                clipboard.setContent(content);
            });

            completeAccountPane.setPadding(new Insets(10));
            completeAccountPane.getStyleClass().add("complete-account-pane");

            accountsPane.getChildren().add(completeAccountPane);

        }

        return accountsPane;
    }
    private TilePane createCardsPane() {
        TilePane accountsPane = new TilePane();
        accountsPane.setPadding(new Insets(10));
        accountsPane.setHgap(10);
        accountsPane.setVgap(10);
        accountsPane.setAlignment(Pos.BOTTOM_CENTER);
        Integer[] cardIDs = dataBase.getCardsIDs(sessionID);
        cards = new ArrayList<>();
        for (int i = 1; i <= cardIDs.length; i++) {
            cards.add(cardIDs[i - 1]);
            Rectangle accountRectangle = new Rectangle(280, 150);
            accountRectangle.setArcWidth(20);
            accountRectangle.setArcHeight(20);
            accountRectangle.getStyleClass().add("account-rectangle");

            ResultSet resultSet = dataBase.getCardInfo(cardIDs[i - 1]);
            String Number = null;
            java.sql.Date ExpireDate = null;
            String cardType = null;
            String cardPayment = null;
            String cardStatus = null;
            try {
                if(resultSet.next()) {
                    Number = resultSet.getString("card_number");
                    ExpireDate = resultSet.getDate("expiry_date");
                    int typeID = resultSet.getInt("type_id");
                    int typePaymentID = resultSet.getInt("payment_method_id");
                    cardType = dataBase.getCardType(typeID);
                    cardPayment = dataBase.getCardPayment(typePaymentID);
                    if(resultSet.getBoolean("is_active"))
                        cardStatus = "Active";
                    else
                        cardStatus = "Blocked";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Number = Number.substring(0, 4) + " " + Number.substring(4, 8) + " "
                    + Number.substring(8, 12) + " " + Number.substring(12, 16);
            Label accountNumberLabel = createLabel(Number);
            Label cardTypeLabel = createLabel(cardType);
            Label anotherInfo = createLabel(cardPayment + "    " + ExpireDate);
            Label activeLabel = createLabel(cardStatus);
            accountNumberLabel.getStyleClass().add("balance-label");

            VBox accountBox = new VBox(10, cardTypeLabel, accountNumberLabel, anotherInfo, activeLabel);
            accountBox.setAlignment(Pos.BOTTOM_LEFT);
            accountBox.setPadding(new Insets(10));
            accountBox.getStyleClass().add("account-box");

            Pane completeAccountPane = new Pane(accountRectangle, accountBox);

            Integer I = i;
            completeAccountPane.setOnMouseEntered(event -> {
                accountRectangle.getStyleClass().add("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseExited(event -> {
                accountRectangle.getStyleClass().remove("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseClicked(event -> {
                cardID2 = I-1;
            });

            completeAccountPane.setPadding(new Insets(10));
            completeAccountPane.getStyleClass().add("complete-account-pane");

            accountsPane.getChildren().add(completeAccountPane);

        }

        return accountsPane;
    }




    private ScrollPane createTransactionsPane() {
        int accountID = accounts.get(accountID2);
        VBox transactionsContainer = new VBox();
        transactionsContainer.setSpacing(10);
        transactionsContainer.setPadding(new Insets(10));

        try {
            ResultSet resultSet = dataBase.getTransactionsAccount(accountID);
            while (resultSet.next()) {
                int transactionID = resultSet.getInt("id");
                transactionsContainer.getChildren().add(createTransactionPane(transactionID, accountID, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(transactionsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("custom-scroll-pane");
        return scrollPane;
    }

    private ScrollPane createTransactionsPaneByCard() {
        int cardID = accounts.get(accountID2);
        VBox transactionsContainer = new VBox();
        transactionsContainer.setSpacing(10);
        transactionsContainer.setPadding(new Insets(10));

        try {
            ResultSet resultSet = dataBase.getTransactionsCard(cardID);
            while (resultSet.next()) {
                int transactionID = resultSet.getInt("id");
                transactionsContainer.getChildren().add(createTransactionPane(transactionID, null, cardID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ScrollPane scrollPane = new ScrollPane(transactionsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("custom-scroll-pane");
        return scrollPane;
    }

    int loanID2;
    private TilePane createLoansPane() {
        TilePane loansPane = new TilePane();
        loansPane.setPadding(new Insets(10));
        loansPane.setHgap(10);
        loansPane.setVgap(10);
        loansPane.setAlignment(Pos.BOTTOM_CENTER);
        Integer[] loanIDs = dataBase.getLoansIDs(sessionID);
        loans = new ArrayList<>();
        for (int i = 1; i <= loanIDs.length; i++) {
            loans.add(loanIDs[i - 1]);
            Rectangle accountRectangle = new Rectangle(250, 100);
            accountRectangle.setArcWidth(20);
            accountRectangle.setArcHeight(20);
            accountRectangle.getStyleClass().add("account-rectangle");

            ResultSet resultSet = dataBase.getLoanInfo(loanIDs[i - 1]);
            double amount = 0;
            java.sql.Date date = null;
            try {
                if(resultSet.next()) {
                    amount = resultSet.getDouble("amount");
                    date = resultSet.getDate("start_date");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Label accountNumberLabel = createLabel(String.format("%.2f", amount));
            accountNumberLabel.getStyleClass().add("balance-label");
            Label dateLabel = createLabel(date.toString());
            accountNumberLabel.getStyleClass().add("account-number-label");

            VBox accountBox = new VBox(10, dateLabel, accountNumberLabel);
            accountBox.setAlignment(Pos.BOTTOM_LEFT);
            accountBox.setPadding(new Insets(10));
            accountBox.getStyleClass().add("account-box");


            Pane completeAccountPane = new Pane(accountRectangle, accountBox);
            Integer I = i;
            completeAccountPane.setOnMouseEntered(event -> {
                accountRectangle.getStyleClass().add("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseExited(event -> {
                accountRectangle.getStyleClass().remove("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseClicked(event -> {
                loanID2 = I-1;
            });
            completeAccountPane.setPadding(new Insets(10));
            completeAccountPane.getStyleClass().add("complete-account-pane");
            loansPane.getChildren().add(completeAccountPane);
        }

        return loansPane;
    }

    private TilePane createDepositsPane() {
        TilePane DepositsPane = new TilePane();
        DepositsPane.setPadding(new Insets(10));
        DepositsPane.setHgap(10);
        DepositsPane.setVgap(10);
        DepositsPane.setAlignment(Pos.BOTTOM_CENTER);
        Integer[] deposIDs = dataBase.getDepositsIDs(sessionID);
        deposits = new ArrayList<>();
        for (int i = 1; i <= deposIDs.length; i++) {
            deposits.add(deposIDs[i - 1]);
            Rectangle accountRectangle = new Rectangle(250, 100);
            accountRectangle.setArcWidth(20);
            accountRectangle.setArcHeight(20);
            accountRectangle.getStyleClass().add("account-rectangle");

            ResultSet resultSet = dataBase.getDeposInfo(deposIDs[i - 1]);
            double amount = 0;
            java.sql.Date date = null;
            try {
                if(resultSet.next()) {
                    amount = resultSet.getDouble("amount");
                    date = resultSet.getDate("start_date");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Label accountNumberLabel = createLabel(String.format("%.2f", amount));
            accountNumberLabel.getStyleClass().add("balance-label");
            Label dateLabel = createLabel(date.toString());
            accountNumberLabel.getStyleClass().add("account-number-label");

            VBox accountBox = new VBox(10, dateLabel, accountNumberLabel);
            accountBox.setAlignment(Pos.BOTTOM_LEFT);
            accountBox.setPadding(new Insets(10));
            accountBox.getStyleClass().add("account-box");


            Pane completeAccountPane = new Pane(accountRectangle, accountBox);
            Integer I = i;
            completeAccountPane.setOnMouseEntered(event -> {
                accountRectangle.getStyleClass().add("account-rectangle-hover");
            });
            completeAccountPane.setOnMouseExited(event -> {
                accountRectangle.getStyleClass().remove("account-rectangle-hover");
            });
            completeAccountPane.setPadding(new Insets(10));
            completeAccountPane.getStyleClass().add("complete-account-pane");
            DepositsPane.getChildren().add(completeAccountPane);
        }

        return DepositsPane;
    }


    private VBox createTransactionPane(int transactionID, Integer accountID, Integer cardID) {
        VBox transactionPane = new VBox();
        transactionPane.getStyleClass().add("transaction-pane");

        try {
            String description = dataBase.getTransactionDescriptionById(transactionID);
            String date = dataBase.getTransactionDateById(transactionID);
            String amount2 = "";
            Integer senderID = dataBase.getSenderAccountByTransactionId(transactionID);
            Integer senderCardID = dataBase.getSenderCardByTransactionId(transactionID);

            if (senderID == accountID || senderCardID == cardID) {
               amount2 = "-" + String.format("%.2f", dataBase.getTransactionAmountByIdFrom(transactionID));
            } else {
                amount2 = "+" + String.format("%.2f", dataBase.getTransactionAmountByIdTo(transactionID));
            }
            if(accountID == null) {
                accountID = DataBase.getCardAccount(cardID);
            }
            amount2 += " " + dataBase.getCurrencyCode(dataBase.getCurrencyID(accountID));

            Boolean isSuccessful = dataBase.getTransactionIsSuccessful(transactionID);
            if(isSuccessful == null) {
                amount2 = "PENDING";
            } else
            if(!isSuccessful) {
                amount2 = "FAILED";
            }

            Label amountLabel = new Label(amount2);
            amountLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

            Label descriptionLabel = new Label(description);
            descriptionLabel.setStyle("-fx-font-size: 18px;");

            Label dateLabel = new Label(date);
            dateLabel.setStyle("-fx-font-size: 18px;");

            transactionPane.getChildren().addAll(amountLabel, descriptionLabel, dateLabel);

            transactionPane.setOnMouseClicked(event -> {
                showTransactionDetails(transactionID);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionPane;
    }

    private int checkLogin(String login, String password) {
        return dataBase.checkAuthorization(login, password);
    }

    private GridPane createRootPane() {
        GridPane root = new GridPane();
        root.getStyleClass().add("root");
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        return root;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        textField.setPromptText(promptText);
        GridPane.setHalignment(textField, HPos.CENTER);
        return textField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(passwordField, HPos.CENTER);
        return passwordField;
    }

    private Button createContinueButton() {
        Button continueButton = new Button("Continue");
        continueButton.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(continueButton, HPos.CENTER);
        return continueButton;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    private Button createBackButton() {
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Inter-Bold", FontWeight.BOLD, 16));
        GridPane.setHalignment(backButton, HPos.CENTER);
        return backButton;
    }

    private Scene createScene(GridPane root) {
        Scene scene = new Scene(root, 880, 700);
        URL fontUrl = getClass().getResource("/styles/Inter-Bold.ttf");
        Font font = Font.loadFont(fontUrl.toExternalForm(), 12);
        scene.getStylesheets().add(getClass().getResource("/styles/Bank.css").toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}
