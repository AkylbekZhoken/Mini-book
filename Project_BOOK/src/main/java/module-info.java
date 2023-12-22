module com.example.project_book {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;

    opens com.example.project_book to javafx.fxml;

    exports com.example.project_book;
    exports com.example.project_book.Class;
    opens com.example.project_book.Class to javafx.fxml;


}
