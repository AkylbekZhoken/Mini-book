package com.example.project_book.Class;

import javafx.scene.control.Alert;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoreLogic {
    private final DatabaseConnection databaseConnection;
    EmailSender sender ;

    public CoreLogic(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        sender = new EmailSender();

    }

    public void addFeedBack(User sample_user , User author , String message){
        sender.sendEmail(author.getEmail(), "Feed Back From "+sample_user.getUsername(),message);
    }
    public List<String> getOrder(User user) {
        List<String> orders = new ArrayList<>();
        try {
            String selectQuery =
                    "SELECT matter.title, orders.amount, orders.date_of_order, orders.delivery_date, orders.location " +
                            "FROM orders JOIN matter ON orders.book_id = matter.id WHERE username = ? " +
                            "GROUP BY orders.order_id";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(selectQuery)) {
                preparedStatement.setString(1, user.getUsername());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String title = resultSet.getString("title");
                        double amount = resultSet.getDouble("amount");
                        String dateOfOrder = resultSet.getString("date_of_order");
                        String deliveryDate = resultSet.getString("delivery_date");
                        String location = resultSet.getString("location");

                        String orderInfo = String.format("Title: %s, Amount: %.2f, Date of Order: %s, Delivery Date: %s, Location: %s",
                                title, amount, dateOfOrder, deliveryDate, location);

                        orders.add(orderInfo);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
        }
        return orders;
    }


    public void record(PrintedMatter matter ,int quantity) throws Exception{
        String insertQuery = "INSERT into matter (title ,author , genres  ,description , prices ,quantity) VALUES (?, ?, ?, ? ,? , ?)";
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(insertQuery);
        preparedStatement.setString(1, matter.title());
        preparedStatement.setString(2,matter.author());
        preparedStatement.setArray(3, databaseConnection.getConnection().createArrayOf("VARCHAR", matter.genres().toArray()));
        preparedStatement.setString(4, matter.description());
        preparedStatement.setDouble(5, matter.prices());
        preparedStatement.setInt(6, quantity);

        preparedStatement.executeUpdate();
    }

    public boolean createPrintedMatter(String author, String title, List<String> genres, String description, double prices, int quantity , double rating) {
        PrintedMatterCreator creator = new BookCreator(title, author, genres,description,prices,rating );
        PrintedMatter matter = creator.create();
        try {
            record(matter,quantity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }







    public void subscribeLogic(User subscriber, User author) {
        try {
            if (isAlreadySubscribed(subscriber, author)) {
                System.out.println("User is already subscribed to this author.");
                return;
            }

            subscriber.subscribe(author);

            String insertQuery = "INSERT INTO subscribe (author, subscriber) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(insertQuery)) {
                preparedStatement.setString(1, author.getUsername());
                preparedStatement.setString(2, subscriber.getUsername());
                preparedStatement.executeUpdate();
            }

            System.out.println("Subscription successful!");
        } catch (Exception e) {
            System.out.println("Error in subscribeLogic: " + e.getMessage());
        }
    }

    private boolean isAlreadySubscribed(User subscriber, User author) {
        List<User> subscriptions = subscriber.getSubscriptions();
        return subscriptions.contains(author);
    }
    public void unsubscribeLogic(User subscriber, User author) {
        try {
            if (!isAlreadySubscribed(subscriber, author)) {
                System.out.println("User is not subscribed to this author.");
                return;
            }

            subscriber.unsubscribe(author);

            String deleteQuery = "DELETE FROM subscribe WHERE author = ? AND subscriber = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, author.getUsername());
                preparedStatement.setString(2, subscriber.getUsername());
                preparedStatement.executeUpdate();
            }

            System.out.println("Unsubscription successful!");
        } catch (Exception e) {
            System.out.println("Error in unsubscribeLogic: " + e.getMessage());
        }
    }
    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        try {
            String selectQuery = "select nickname , email from accounts where account_type not like 'Admin' ";

            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        String user = resultSet.getString("nickname");
                        String email = resultSet.getString("email");
                        users.add(new User(user,email));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }



    public List<User> getAllAuthors() {
        List<User> authors = new ArrayList<>();
        try {
            String selectQuery = "select nickname from accounts where account_type like 'author'";

            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String author = resultSet.getString("nickname");
                        authors.add(new User(author));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM matter";

            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        Array genresArray = resultSet.getArray("genres");
                        String[] genres = (String[]) genresArray.getArray();
                        List<String> genreList = Arrays.asList(genres);
                        String description = resultSet.getString("description");
                        double price = resultSet.getDouble("prices");
                        double rating = resultSet.getDouble("rating");

                        Book book = new Book(title, author, genreList ,description, price,rating);
                        book.setId(bookId);
                        availableBooks.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableBooks;
    }
    public boolean isBanned(String user) {
        String username = null;

        try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement("select nickname from ban where nickname = ?")) {
            preparedStatement.setString(1, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    username = resultSet.getString("nickname");
                }
            }

            return username != null;
        } catch (SQLException e) {
            System.out.println("Error checking ban status: " + e.getMessage());
            return false;
        }
    }


    public boolean BuyLogic(PrintedMatter matter, User user, String location) {
        try {
            if (user.getCoins() < matter.prices() || selectQuantity(matter) < 0) {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {

            String insertQuery = "INSERT INTO orders (username, book_id, amount, location) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = databaseConnection.getConnection().prepareStatement(insertQuery);
            insertStatement.setString(1, user.getUsername());
            insertStatement.setInt(2, matter.id());
            insertStatement.setDouble(3, matter.prices());
            insertStatement.setString(4, location);


            insertStatement.executeUpdate();
            updateCoins(user, user.getCoins() - matter.prices());
            updateQuantity(matter,selectQuantity(matter) - 1);

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void updateCoins(User user , double updateSum) throws Exception{
        String updateQuery = "UPDATE accounts SET coins = ? WHERE nickname = ?";
        PreparedStatement updateStatement = databaseConnection.getConnection().prepareStatement(updateQuery);
        updateStatement.setDouble(1, updateSum);
        updateStatement.setString(2, user.getUsername());


        updateStatement.executeUpdate();

    }
    public void updateQuantity(PrintedMatter matter , int quantity) throws Exception{
        String updateQuery = "UPDATE matter set quantity = ? where title = ? and author = ?";
        PreparedStatement updateStatement = databaseConnection.getConnection().prepareStatement(updateQuery);
        updateStatement.setInt(1,quantity);
        updateStatement.setString(2, matter.title());
        updateStatement.setString(3, matter.author());



        updateStatement.executeUpdate();

    }
    public int selectQuantity(PrintedMatter matter) throws Exception{
        int quantity = 0;
        String selectQuery = "select quantity from matter where title = ? and author = ?";
        PreparedStatement selectStatement = databaseConnection.getConnection().prepareStatement(selectQuery);
        selectStatement.setString(1, matter.title());
        selectStatement.setString(2, matter.author());
        ResultSet resultSet = selectStatement.executeQuery();
        while(resultSet.next()){
            quantity = resultSet.getInt("quantity");
        }
        return quantity;

    }



    public boolean initialization(User user){
        return initializationSubscriptions(user) && initializationSubscribers(user);
    }
    public boolean initializationSubscriptions(User user) {
        List<User> subscriptions = new ArrayList<>();
        try {
            String query = "SELECT subscriber FROM subscribe WHERE author = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String subscriberUsername = resultSet.getString("subscriber");
                        User subscriber = selectUser(subscriberUsername);

                        if (subscriber != null) {
                            subscriptions.add(subscriber);
                        }
                    }
                }
            }

            user.setSubscriptions(subscriptions);
            return true;
        } catch (Exception e) {
            System.out.println("Error initializing subscriptions: " + e.getMessage());
            return false;
        }
    }

    public boolean initializationSubscribers(User user) {
        List<User> subscribers = new ArrayList<>();
        try {
            String query = "SELECT author FROM subscribe WHERE subscriber = ?";
            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, user.getUsername());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String authorUsername = resultSet.getString("author");
                        User author = selectUser(authorUsername);

                        if (author != null) {
                            subscribers.add(author);
                        }
                    }
                }
            }

            user.setSubscribers(subscribers);
            return true;
        } catch (Exception e) {
            System.out.println("Error initializing subscribers: " + e.getMessage());
            return false;
        }
    }











    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    int checker = 0;
    public boolean check(int n){
        return n == checker;
    }
    public User Sign_UpLogic(String username, String password, String email) {
        try {
            checker = new Random().nextInt(1000,9999);

            if (!sender.sendEmail(email, "Mini Book Store", "Welcome.\nWe are pleased to welcome you to our Mini Book Store. Enjoy your use.This check code : "+checker)) {
                showErrorAlert("Error", "Problem with email", "Your email address is incorrect. Try again.");
                return null;
            }

            // User Object Creation
            User user = new User(username, hashPassword(password), email, 0, "sample user");

            // Database Insertion
            String insertQuery = "INSERT INTO accounts(nickname, password, email) VALUES (?, ?, ?)";

            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(insertQuery)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());

                preparedStatement.executeUpdate();
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            return null;
        }
    }

    private String hashPassword(String password) {

        return password;
    }

    public User selectUser(String username) {
        String account_type = "" ;
        String password = "";
        double coins = 0;
        String email = "";


        try {
            String selectQuery = "select * from accounts where nickname = ?";

            try (PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(selectQuery)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                        account_type = resultSet.getString("account_type");
                        email = resultSet.getString("email");
                        coins = resultSet.getDouble("coins");

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new User(username,password,email,coins,account_type);
    }
    public User loginLogic(String username,String Password){
        User user = selectUser(username);
        if(!isBanned(username) && Password.equals(user.getPassword())){
            if(initialization(user)){
                return user;
            }
        }
        return null;
    }
    public static boolean isStrongPassword(String password) {

        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return false;
        }
        if (!containsUppercase(password)) {
            System.out.println("Password must contain at least one uppercase letter.");
            return false;
        }

        if (!containsDigit(password)) {
            System.out.println("Password must contain at least one digit.");
            return false;
        }


        return true;
    }
    private static boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }
    private static boolean containsUppercase(String password) {
        return password.matches(".*[A-Z].*");
    }
    public void removeBook(Book book) {
        try{
            String remove="delete * from matter where id=?";

        }catch (Exception e){
            System.out.println(e);
        }
    }
    public boolean giveAuthorAccess(String username){
        try{        String query = "update accounts set account_type = 'author' where nickname like ?";
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(query);        preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();        return true;
        }    catch (Exception e){
            System.out.println(e.getMessage());        return false;
        }}


}

