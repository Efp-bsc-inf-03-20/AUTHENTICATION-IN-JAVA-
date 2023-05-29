import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.sql.ResultSet;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Loginpage extends Application {


    private static final String DB_URL = "jdbc:postgresql://localhost:5432/javabase";
    private static final String DB_USERNAME = " ";
    private static final String DB_PASSWORD = " ";

    @Override
    public void start(Stage primaryStage) throws Exception {



        primaryStage.setTitle("SignUp Form");


        Label headerLabel = new Label("SHIPPING FEE CALCULATOR");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label descriptionLabel = new Label("Welcome and sign up to our app.Estimating shipping price from China to Malawi");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        Label nameLabel = new Label("Enter Name:");
        TextField nameTextField = new TextField();

        Label emailLabel = new Label("Enter Email Address:");
        TextField emailTextField = new TextField();

        Label passwordLabel = new Label("Generate password:");
        TextField passwordTextField = new TextField();

        Label confirmPasswordLabel = new Label("Confirm password:");
        TextField confirmPasswordTextField = new TextField();

        Button signupButton = new Button("Signup");

        Label logLabel = new Label("Already have an account?");
        logLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        Button loginButton = new Button("Log in");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.setTitle("log in");
                Label loginemailLabel = new Label("Enter Email Address:");
                TextField loginemailTextField = new TextField();

                Label passwordLabel = new Label("Enter password:");
                TextField loginpasswordTextField = new TextField();
                String loginpass=loginpasswordTextField.getText();
                Button btn=new Button("submit");


                VBox thisvbox=new VBox();
                thisvbox.setSpacing(10);
                Scene myscene=new Scene(thisvbox,500,500);

                thisvbox.getChildren().addAll(loginemailLabel,loginemailTextField,passwordLabel,loginpasswordTextField,btn);
                primaryStage.setScene(myscene);

           thisvbox.setAlignment(Pos.CENTER);
           btn.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent actionEvent) {
                   String loginEmail = loginemailTextField.getText();
                   String loginPassword = loginpasswordTextField.getText();
                   boolean isLoginSuccessful = verifyLogin(loginEmail, loginPassword);
                   if (isLoginSuccessful) {
                       showAlert("Success", "Login Successful");
                       VBox mainbox=new VBox();
                       Scene mainscene=new Scene(mainbox,500,500);

                       mainbox.setSpacing(10);
                       primaryStage.setScene(mainscene);
                       primaryStage.setTitle("CALCULATE SHIPPING FEE");
                       Label headerLabels = new Label("AIR SHIPPING CALCULATING");
                       headerLabels.setStyle("-fx-font-size: 18px; -fx-font-weight: bolder;");

                       Label descriptionLabel = new Label("NB: if you have non-batteries items you can use this section to calculate");
                       descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

                       Label myKgs = new Label("Enter kgs of goods");
                       TextField MyLtextfield = new TextField();

                       Label myLL = new Label("Enter  Volume weight");
                       TextField MyLLtextfield = new TextField();



                       Button calculate = new Button("Estimate Price");
                       double total = 0.0; // Declare total variable outside the handle method
                      calculate.setOnAction(new EventHandler<ActionEvent>() {
                          @Override
                          public void handle(ActionEvent actionEvent) {

                              if (MyLtextfield.getText().isEmpty()||MyLLtextfield.getText().isEmpty())
                                  showAlert("error","enter all fields");


                              else {
                                  double kgs = Double.parseDouble(MyLtextfield.getText());
                                  double volume = Double.parseDouble(MyLLtextfield.getText());

                                  final  double onekg=99.0;

                                  double total = kgs * volume*onekg;

                                  showFee("Fee", "Your fee will be " + Math.round(total) + " yuan");
                                  showFee("Fee","your fee in  will be "+   Math.round(total*230)+"mwk");
                              }
                          }
                      });
                      thisvbox.setSpacing(10);
                       Label headerLabel = new Label("AIR SHIPPING FOR BATTERY ITEM CALCULATING");
                       headerLabels.setStyle("-fx-font-size: 18px; -fx-font-weight: bolder;");
                       headerLabel.setLineSpacing(1.5);
                       Label descriptionLabels = new Label("NB: if you have battery items you can use this section to estimate");
                       descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
                       Label batteryL=new Label("enter  Kgs of battery items");
                       TextField batteryText=new TextField();
                       Label batteryV=new Label("enter  volume of battery items");
                       TextField batteryVText=new TextField();
                       Button calc=new Button("Estimate fee");
                       calc.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                           public void handle(ActionEvent actionEvent) {
                               String batt=batteryText.getText();
                               String volume=batteryVText.getText();

                               if (batt.isEmpty()&& volume.isEmpty())
                                   showAlert("error","enter all fields");

                           }
                       });





                       mainbox.getChildren().addAll(headerLabels, myKgs, MyLtextfield, myLL, MyLLtextfield, calculate,descriptionLabel,headerLabel,descriptionLabels,batteryL,batteryText,batteryV,batteryVText,calc);


                   } else {
                       showAlert("Error", "Invalid email or password");
                   }

               }
           });

                primaryStage.show();








            }
        });









        signupButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                String name = nameTextField.getText();
                String email = emailTextField.getText();
                String password = passwordTextField.getText();
                String confirmPassword = confirmPasswordTextField.getText();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showAlert("Error", "Please enter all fields");



                } else if (emailExists(email)) {
                    showAlert("Email Exists", "The email address is already registered.");


                } else if (!email.contains("@")) {
                    showAlert("Invalid email '@'missing??","invalid email must contain @ ");



                } else if (password.equals(confirmPassword)) {
                    insertData(email, name, password);
                    showAlert("Registration Successful✅", "Passwords match \uD83D\uDC4D!");

                } else if (password.length()>10) {
                    showAlert("password error","password must be less than 10 characters");



              } else {
                    showAlert("Registration Failed\uD83D\uDEAB", "Passwords do not match ⚠,make sure password equal to confirm");
                }
            }
        });

        VBox vbox = new VBox(10);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20px; -fx-background-color: #f2f2f2;");

        vbox.getChildren().addAll(headerLabel, descriptionLabel, nameLabel, nameTextField,
                emailLabel, emailTextField, passwordLabel, passwordTextField,
                confirmPasswordLabel, confirmPasswordTextField, signupButton, logLabel, loginButton);

        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void insertData(String email, String name, String  passsworde ) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javabase", " ", " ")) {
            String sql = "INSERT INTO javatable (email, name,  password ) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,email);
            statement.setString(2, name);
            statement.setString(3,  passsworde);
            statement.executeUpdate();

            System.out.println("Welcome, " + name + "! Your account has been created successfully.");
            showAlert("confirmation message","Your account has been created successfully you can log in now");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean verifyLogin(String email, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javabase", " ", " ")) {
            String sql = "SELECT * FROM javatable WHERE email = ? AND password= ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next(); // Return true if a row was found matching the email and password
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean emailExists(String email) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/javabase", " ", " "))
             {
            String sql = "SELECT * FROM javatable WHERE email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            boolean exists = resultSet.next();
            System.out.println("Email: " + email + " exists in database: " + exists);
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void showFee(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}



