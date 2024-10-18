package org.example.javafxdb_sql_shellcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.example.javafxdb_sql_shellcode.db.ConnDbOps;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    private static ConnDbOps cdbop;
    public int currentUserId;

    @FXML
    public CheckMenuItem themeLight;
    @FXML
    public CheckMenuItem themeDark;
    public Text welcomeText;
    public MenuItem logOut;

    @FXML
    TextField first_name, last_name, email, phone, address;
    @FXML
    PasswordField password;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_email, tv_phone, tv_address;

    @FXML
    ImageView img_view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cdbop = new ConnDbOps();
        cdbop.connectToDatabase();

        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tv_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tv_address.setCellValueFactory(new PropertyValueFactory<>("address"));

        img_view.setClip(new Circle(45,45,45));

        updateGUI();
    }


    private void updateGUI() {
        tv.setItems(cdbop.dataAsList());
    }

    @FXML
    protected void addNewRecord() {

        Person person = new Person(
            0,
            first_name.getText(),
            last_name.getText(),
            email.getText(),
            phone.getText(),
            address.getText()
        );
        cdbop.insertUser(person,password.getText());
        updateGUI();
    }

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        email.setText("");
        phone.setText("");
        address.setText("");
        password.setText("");
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void editRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        cdbop.editUser(
                p.getId(),
                first_name.getText()+(last_name.getText().isEmpty()?"":" "+last_name.getText()),
                email.getText(),
                phone.getText(),
                address.getText(),
                password.getText(),
                ""
        );
        updateGUI();
    }

    @FXML
    protected void deleteRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        cdbop.deleteUser(p.getId());
        updateGUI();
    }



    @FXML
    protected void editImage() throws MalformedURLException {
        File file= (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if(file!=null){
            img_view.setImage(new Image(file.toURI().toString()));
        }

        cdbop.editUser(currentUserId, "","","","","",file.toURI().toURL().toExternalForm());
    }



    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p= tv.getSelectionModel().getSelectedItem();
        if (p==null) return;
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        email.setText(p.getEmail());
        phone.setText(p.getPhone());
        address.setText(p.getAddress());
    }
}
