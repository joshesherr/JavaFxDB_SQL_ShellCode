package org.example.javafxdb_sql_shellcode;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static ConnDbOps cdbop;
    public Person currentPerson;

    private Stage primaryStage;
    private String theme = "light_theme.css";
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setResizable(false);
        this.primaryStage.setTitle("User Database");
        settingsLoad();
        showSplashScreen();
    }

    private void settingsLoad() {
        try {
            File settingsFile = new File("src/main/resources/settings.txt");
            Scanner textReader = new Scanner(settingsFile);
            while (textReader.hasNextLine()) {
                String line = textReader.nextLine();
                int index = line.indexOf(':');//set index to the ':' character
                String settingName = index != -1 ? line.substring(0, index) : "";//everything before the ':' is the setting name
                String settingValue = index != -1 ? line.substring(index + 1).replaceAll("\\s", "") : "";//everything after is the setting value

                switch (settingName) {
                    case "theme":
                        theme = settingValue+"_theme.css";
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settingsSave(String settingToSave, String value) {
        try {
            File settingsFile = new File("src/main/resources/settings.txt");
            Scanner textReader = new Scanner(settingsFile);
            StringBuilder saveFile = new StringBuilder();
            while (textReader.hasNextLine()) {
                String line = textReader.nextLine();
                int index = line.indexOf(':');//set index to the ':' character
                String settingName = index != -1 ? line.substring(0, index) : "";//everything before the ':' is the setting name

                if (settingName.equals(settingToSave)) {
                    line = settingName + ":" + value;
                }

                saveFile.append(line).append(System.lineSeparator());
            }
            FileWriter writer = new FileWriter("src/main/resources/settings.txt");
            String data =saveFile.toString();
            writer.write(data);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showSplashScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("splash_screen.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(theme);
            primaryStage.setScene(scene);
            primaryStage.show();
            showLogIn(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLogIn(double fadeInTime) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("log_in.fxml"));
            Parent newRoot = fxmlLoader.load();
            LogInController logInController = fxmlLoader.getController();

            logInController.logInBtn.setOnAction(e->{
                if(logInController.logInQuery()) {
                    currentPerson=cdbop.getPerson(logInController.currentUserId);
                    showInterface();
                }
            });
            if (theme.equals("light_theme.css")) {
                logInController.themeToggle.setText("\uD83C\uDF19");
            } else {
                logInController.themeToggle.setText("☀");
            }
            logInController.themeToggle.setOnAction(e->{
                if (theme.equals("light_theme.css")) {
                    logInController.themeToggle.setText("☀");
                    theme = "dark_theme.css";
                    settingsSave("theme","dark");
                } else {
                    logInController.themeToggle.setText("\uD83C\uDF19");
                    theme = "light_theme.css";
                    settingsSave("theme","light");
                }
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(theme);
            });


            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(fadeInTime), currentRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setInterpolator(Interpolator.EASE_IN);
            fadeOut.setOnFinished(e -> {
                Scene newScene = new Scene(newRoot);
                newScene.getStylesheets().add(theme);
                primaryStage.setScene(newScene);
            });
            fadeOut.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInterface() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("db_interface_gui.fxml"));
            Parent newRoot = fxmlLoader.load();
            GUIController guiController = fxmlLoader.getController();
            guiController.currentUserId = currentPerson.getId();
            if(!currentPerson.getPhoto().isEmpty()) guiController.img_view.setImage(new Image(currentPerson.getPhoto()));
            guiController.welcomeText.setText("Welcome " + (currentPerson.getFirstName().isEmpty()
                    ?"User"
                    :currentPerson.getFirstName()) + "!");
            //Update themes
            if (theme.equals("light_theme.css")) {
                guiController.themeLight.setSelected(true);
                guiController.themeDark.setSelected(false);
            } else {
                guiController.themeLight.setSelected(false);
                guiController.themeDark.setSelected(true);
            }
            guiController.themeLight.setOnAction(e->{
                primaryStage.getScene().getStylesheets().clear();
                theme = "light_theme.css";
                primaryStage.getScene().getStylesheets().add(theme);
                guiController.themeDark.setSelected(false);
                settingsSave("theme","light");
            });
            guiController.themeDark.setOnAction(e->{
                primaryStage.getScene().getStylesheets().clear();
                theme="dark_theme.css";
                primaryStage.getScene().getStylesheets().add(theme);
                guiController.themeLight.setSelected(false);
                settingsSave("theme","light");
            });
            guiController.logOut.setOnAction(e->{
                showLogIn(0);
            });

            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.75), currentRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setInterpolator(Interpolator.EASE_OUT);
            fadeOut.setOnFinished(e -> {
                Scene newScene = new Scene(newRoot);
                newScene.getStylesheets().add(theme);
                primaryStage.setScene(newScene);
            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        cdbop = new ConnDbOps();
        Scanner scan = new Scanner(System.in);

        char input;
        do {
            System.out.println(" ");
            System.out.println("=============== Menu ================");
            System.out.println("| To start GUI,           press 'g' |");
            System.out.println("| To connect to DB,       press 'c' |");
            System.out.println("| To display all users,   press 'a' |");
            System.out.println("| To insert to the DB,    press 'i' |");
            System.out.println("| To delete from the DB,  press 'd' |");
            System.out.println("| To edit from the DB,    press 'l' |");
            System.out.println("| To query by name,       press 'q' |");
            System.out.println("| To exit,                press 'e' |");
            System.out.println("=====================================");
            System.out.print("Enter your choice: ");
            input = scan.nextLine().charAt(0);

            switch (input) {
                case 'g': // Start GUI
                    launch(args);
                    break;
                case 'c': // Connect to data base
                    cdbop.connectToDatabase(); //Your existing method
                    break;
                case 'a': // Show all users
                    cdbop.listAllUsers(); //all users in DB
                    break;
                case 'i': // Insert User
                    System.out.print("Enter Name: ");
                    String name = scan.nextLine();
                    int index = name.indexOf(' ');
                    String fname = index!=-1?name.substring(0, index):name;
                    String lname = index!=-1?name.substring(index + 1):"";
                    System.out.print("Enter Email: ");
                    String email = scan.nextLine();
                    System.out.print("Enter Phone: ");
                    String phone = scan.nextLine();
                    System.out.print("Enter Address: ");
                    String address = scan.nextLine();
                    System.out.print("Enter Date of Birth: ");
                    String DoB = scan.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scan.nextLine();
                    Person user = new Person(0,fname,lname,email,phone,address,DoB);
                    cdbop.insertUser(user, password); //Your insertUser method
                    break;
                case 'd':// Delete User
                    System.out.print("Enter User ID to Delete: ");
                    cdbop.deleteUser(Integer.parseInt(scan.nextLine()));
                    break;
                case 'l':// Edit User
                    System.out.print("Enter User ID to Edit: ");
                    int idE = Integer.parseInt(scan.nextLine());
                    System.out.println("Enter new values for user (Leave blank to keep unchanged) ");
                    System.out.print("Enter new Name: ");
                    String nameE = scan.nextLine();
                    System.out.print("Enter new Email: ");
                    String emailE = scan.nextLine();
                    System.out.print("Enter new Phone: ");
                    String phoneE = scan.nextLine();
                    System.out.print("Enter new Address: ");
                    String addressE = scan.nextLine();
                    System.out.print("Enter new Password: ");
                    String passwordE = scan.nextLine();
                    cdbop.editUser(idE, nameE, emailE, phoneE, addressE, passwordE, null); //Your insertUser method
                    break;
                case 'q': // Query by name
                    System.out.print("Enter the name to query: ");
                    String queryName = scan.next();
                    cdbop.queryUserByName(queryName); //Your queryUserByName method
                    break;
                case 'e': // Exit
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println(" ");
        } while (input != 'e');

        scan.close();
       
    }

}
