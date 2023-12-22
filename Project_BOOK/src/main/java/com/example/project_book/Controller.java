package com.example.project_book;

import com.example.project_book.Class.Book;
import com.example.project_book.Class.CoreLogic;
import com.example.project_book.Class.DatabaseConnection;
import com.example.project_book.Class.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Controller  {
    Stage stage;
    Scene scene;
    User user;
    CoreLogic logic=new CoreLogic(DatabaseConnection.getInstance());
// login page


    @FXML
    private PasswordField pass_login;


    @FXML
    private TextField user_login;


    @FXML
    void login_button1(MouseEvent event) throws IOException {

     this.user=logic.loginLogic(user_login.getText(),pass_login.getText());
     if(user.getAccountType().equals("admin")){
        login(event,"AdminPage.fxml");
      }
     else if(user!=null){
       login(event,"UserPage.fxml");
     }

    }



  @FXML
  void registration(MouseEvent event) throws IOException {
    login(event,"RegistrationPage.fxml");
  }
  @FXML
  void login_forgot(MouseEvent event) throws IOException {
      login(event,"ForgotPassword.fxml");
  }
  private void login(MouseEvent event,String s) throws IOException {
    Parent root = (Parent) FXMLLoader.load(Objects.requireNonNull(getClass().getResource(s)));
    this.stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    this.scene = new Scene(root);
    this.stage.setScene(this.scene);
    this.stage.show();
  }
  private void login2(ActionEvent event,String as) throws IOException {
    Parent root = (Parent) FXMLLoader.load(Objects.requireNonNull(getClass().getResource(as)));
    this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    this.scene = new Scene(root);
    this.stage.setScene(this.scene);
    this.stage.show();
  }


//
// registration page

  @FXML
  private TextField email_regis;

  @FXML
  private TextField number_sent;

  @FXML
  private PasswordField pass_regis;

  @FXML
  private AnchorPane registration_page;

  @FXML
  private AnchorPane registration_page_sent;


  @FXML
  private TextField user_regis;



  @FXML
  void back_button(MouseEvent event) {
    registration_page.setVisible(true);
    registration_page_sent.setVisible(false);
  }

  @FXML
  void login_button(MouseEvent event) throws IOException {
     login(event,"loginPage.fxml");
  }

  @FXML
  void registration_button(ActionEvent event) {
        if(logic.isStrongPassword(pass_regis.getText())){
        this.user = logic.Sign_UpLogic(user_regis.getText(),pass_regis.getText(),email_regis.getText());
        if(user != null){
        registration_page.setVisible(false);
        registration_page_sent.setVisible(true);}
  }}

  @FXML
  void send_button(ActionEvent event) throws IOException {
    if(logic.check(Integer.parseInt(number_sent.getText()))){
      login2(event,"UserPage.fxml");
    }

  }
//
// user page
@FXML
private AnchorPane admin_label_all_books;

  @FXML
  private AnchorPane admin_label_profile;

  @FXML
  private TableView<?> admin_profile_author;

  @FXML
  private TableColumn<?, ?> admin_profile_author_name;

  @FXML
  private AnchorPane admin_table_view_allBooks;

  @FXML
  private AnchorPane admin_table_view_recommend;

  @FXML
  private Label coins;

  @FXML
  private ComboBox<String> comboBox_genre;

  @FXML
  private AnchorPane profile_button;

  @FXML
  private TableView<Book> table_all_book;

  @FXML
  private TableView<User> table_author;

  @FXML
  private TableView<?> table_recommend;

  @FXML
  private AnchorPane user_add_book_anchor;

  @FXML
  private Button user_author_subscribe_but;

  @FXML
  private TableColumn<Book, String> user_bookName;

  @FXML
  private Button user_book_buy_but;

  @FXML
  private AnchorPane user_label_recommed;

  @FXML
  private AnchorPane user_labels_author;

  @FXML
  private TableColumn<User, String> user_ltable_view_author;

  @FXML
  private TableView<?> user_orders;

  @FXML
  private TableColumn<?,?> user_orders_details;

  @FXML
  private TableColumn<Book,String> user_price;

  @FXML
  private TableColumn<?, ?> user_recommend_book_name;

  @FXML
  private TableColumn<?, ?> user_recommend_price;

  @FXML
  private Label user_status;

  @FXML
  private AnchorPane user_table_view_author;
  @FXML
  void comboBox_genre(ActionEvent event) {

  }

  @FXML
  void user_author_subscribe_but(ActionEvent event) {

  }

  @FXML
  void user_book_author_but(ActionEvent event) {

    admin_label_all_books.setVisible(false);
    admin_table_view_allBooks.setVisible(false);
    user_label_recommed.setVisible(false);
    admin_table_view_recommend.setVisible(false);
    user_labels_author.setVisible(true);
    user_table_view_author.setVisible(true);
    admin_label_profile.setVisible(false);
    profile_button.setVisible(false);
    table_author.getItems().clear();
    initialize2();
  }

  private void initialize2() {
    List<User> list=logic.getAllAuthors();
    if(user_ltable_view_author!=null){
      user_ltable_view_author.setCellValueFactory(cellData->cellData.getValue().Username());
      table_author.getItems().addAll(list);
    }
  }

  @FXML
  void user_book_but(ActionEvent event) {
    admin_label_all_books.setVisible(true);
    admin_table_view_allBooks.setVisible(true);
    user_label_recommed.setVisible(false);
    admin_table_view_recommend.setVisible(false);
    user_labels_author.setVisible(false);
    user_table_view_author.setVisible(false);
    admin_label_profile.setVisible(false);
    profile_button.setVisible(false);
    user_book_buy_but.setVisible(true);

    List<Book> books = logic.getAvailableBooks();
    if (user_bookName != null) {
      user_bookName.setCellValueFactory(cellData->cellData.getValue().getTitle());

      user_price.setCellValueFactory(cellData->cellData.getValue().getPrice());
      table_all_book.getItems().addAll(books);}
  }

  @FXML
  void user_book_buy_but(ActionEvent event) throws IOException {
    login2(event,"Location.fxml");
  }

  @FXML
  void user_book_logout_but(ActionEvent event) throws IOException {
    login2(event,"loginPage.fxml");
  }

  @FXML
  void user_book_profile_but(ActionEvent event) {
    admin_label_all_books.setVisible(false);
    admin_table_view_allBooks.setVisible(false);
    user_label_recommed.setVisible(false);
    admin_table_view_recommend.setVisible(false);
    user_labels_author.setVisible(false);
    user_table_view_author.setVisible(false);
    admin_label_profile.setVisible(true);
    profile_button.setVisible(true);


  }

  @FXML
  void user_book_recommend_but(ActionEvent event) {
    admin_label_all_books.setVisible(false);
    admin_table_view_allBooks.setVisible(false);
    user_label_recommed.setVisible(true);
    admin_table_view_recommend.setVisible(true);
    user_labels_author.setVisible(false);
    user_table_view_author.setVisible(false);
    admin_label_profile.setVisible(false);
    profile_button.setVisible(false);
  }
  @FXML
  void user_add_book(ActionEvent event) {

  }
  //
  // admin page
  @FXML
  private TableColumn<Book, String> admin_avail_Author;

  @FXML
  private TableColumn<Book, String> admin_avail_Description;

  @FXML
  private TableColumn<Book, String> admin_avail_Genre;

  @FXML
  private TableColumn<Book, String> admin_avail_Price;

  @FXML
  private TableColumn<Book, String> admin_avail_Quantity;

  @FXML
  private TableColumn<Book, String> admin_avail_Rating;

  @FXML
  private TableColumn<?, ?> admin_avail_bookId;

  @FXML
  private TableColumn<Book, String> admin_avail_bookName;

  @FXML
  private TextField admin_text_Author;

  @FXML
  private TextField admin_text_Genre;

  @FXML
  private TextField admin_text_bookId;

  @FXML
  private TextField admin_text_bookName;

  @FXML
  private TextField admin_text_description;

  @FXML
  private TextField admin_text_price;

  @FXML
  private TextField admin_text_quantity;

  @FXML
  private TextField admin_text_rating;

  @FXML
  private TableColumn<User, String> admin_userName;

  @FXML
  private TableColumn<User, String> admin_user_email;

  @FXML
  private AnchorPane available_book1;

  @FXML
  private AnchorPane available_book2;

  @FXML
  private TableView<User> table_admin_user_information;

  @FXML
  private TableView<Book> table_amin_available;

  @FXML
  private AnchorPane user_infornation;

  @FXML
  void Admin_user_lock(ActionEvent event) {
    User selectedUser=table_admin_user_information.getSelectionModel().getSelectedItem();
    if(selectedUser!=null){
      logic.isBanned(selectedUser.getUsername());
    }
  }

  @FXML
  void Admin_user_unlock(ActionEvent event) {

  }

  @FXML
  void authors_button(ActionEvent event) {
     User selectedUser=table_admin_user_information.getSelectionModel().getSelectedItem();
     if(selectedUser!=null){
       logic.giveAuthorAccess(selectedUser.getUsername());
     }
  }

  @FXML
  private void initialize( )  {
    List<Book> books = logic.getAvailableBooks();
    if (admin_avail_bookName != null) {
    admin_avail_bookName.setCellValueFactory(cellData->cellData.getValue().getTitle());
    admin_avail_Author.setCellValueFactory(cellData->cellData.getValue().getAuthor());
    admin_avail_Genre.setCellValueFactory(cellData->cellData.getValue().getGenre());
    admin_avail_Description.setCellValueFactory(cellData->cellData.getValue().getDescription());
    admin_avail_Price.setCellValueFactory(cellData->cellData.getValue().getPrice());
    admin_avail_Rating.setCellValueFactory(cellData->cellData.getValue().getRating());
    table_amin_available.getItems().addAll(books);}
  }
  @FXML
  void admin_add_button(ActionEvent event) {
    logic.createPrintedMatter(
            admin_text_Author.getText(),
            admin_text_bookName.getText(),
            Arrays.asList(admin_text_Genre.getText()),
            admin_text_description.getText(),
            Double.parseDouble(admin_text_price.getText()),
            Integer.parseInt(admin_text_quantity.getText()),
            Double.parseDouble(admin_text_rating.getText())
    );
    table_amin_available.getItems().clear();
    table_amin_available.getItems().addAll(logic.getAvailableBooks());



  }


  @FXML
  void admin_delete_button(ActionEvent event) {
    Book selectedBook = table_amin_available.getSelectionModel().getSelectedItem();
    if (selectedBook != null) {

    }
    table_amin_available.getItems().clear();
    table_amin_available.getItems().addAll(logic.getAvailableBooks());
  }

  @FXML
  void admin_update_button(ActionEvent event) {

  }

  @FXML
  void avail_book_admin(ActionEvent event) {
      available_book1.setVisible(true);
      available_book2.setVisible(true);
      user_infornation.setVisible(false);

  }

  @FXML
  void logout_admin(ActionEvent event) throws IOException {
     login2(event,"loginPage.fxml");
  }

  @FXML
  void user_info_admin(ActionEvent event) {
    available_book1.setVisible(false);
    available_book2.setVisible(false);
    user_infornation.setVisible(true);table_admin_user_information.getItems().clear();
    initialize1();

  }
  public void initialize1(){
    List<User> list=logic.getAllUser();
    if (table_admin_user_information != null) {
    admin_userName.setCellValueFactory(cellData->cellData.getValue().Username());
    admin_user_email.setCellValueFactory(cellData->cellData.getValue().Email());
    table_admin_user_information.getItems().addAll(list);
    }
  }
  //
  // forgot password
  @FXML
  private TextField forgot_email;

  @FXML
  private PasswordField forgot_new_password;

  @FXML
  private TextField forgot_number;

  @FXML
  private AnchorPane forgot_password;

  @FXML
  private AnchorPane forgot_sent_password;

  @FXML
  private TextField forgot_username;

  @FXML
  void forgot_button(ActionEvent event) {

  }

  @FXML
  void forgot_login_button(MouseEvent event) throws IOException {
     login(event,"LoginPage.fxml");
  }

  @FXML
  void forgot_sent(ActionEvent event) {

  }


// LOCATION PAGE
  @FXML
  private TextField location_text;
  List<Book> books=logic.getAvailableBooks();


  @FXML
  void confirm_button(ActionEvent event) {
    table_all_book.getItems().addAll(books);
    if (table_all_book != null) {


      Book selectedBook = table_all_book.getSelectionModel().getSelectedItem();

      if (selectedBook != null) {
        boolean purchaseSuccessful = logic.BuyLogic(selectedBook, user, location_text.getText());

        if (purchaseSuccessful) {
          System.out.println("Purchase successful!");
          table_all_book.getItems().clear();
          table_all_book.getItems().addAll(logic.getAvailableBooks());
        } else {
          System.out.println("Purchase failed. Insufficient coins or quantity.");
        }
      }
    }
  }


}
