package com.example.project_book.Class;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class User {
    private List<User>  subscriptions;
    private List<User> subscribers;
    private String username;
    private String password;
    private String email;
    private double coins;
    private EmailSender sender ;
    private String accountType;

    User(String username){
        this.username = username;

    }
    User(String username,String email){
        this.username = username;
        this.email = email;

    }

    User(String username, String password, String email, double coins, String accountType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.coins = coins;

        sender = new EmailSender();
        subscriptions = new ArrayList<>();

        this.accountType = accountType;

        if (accountType.equals("author")) {
            subscribers = new ArrayList<>();
        }
    }

    public void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions;
    }
    public SimpleStringProperty Username(){
        return new SimpleStringProperty(username);
    }
    public SimpleStringProperty Email(){
        return new SimpleStringProperty(email);
    }
    public SimpleStringProperty Password(){
        return new SimpleStringProperty(password);
    }
    public SimpleDoubleProperty Coins(){
        return new SimpleDoubleProperty(coins);
    }
    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSender(EmailSender sender) {
        this.sender = sender;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountType() {
        return accountType;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }
    public void setSubscribers(List<User> subscribers){
        this.subscribers = subscribers;
    }



    public List<User> getSubscriptions(){
        return subscriptions;
    }

    public void subscribe(User author) {
        author.addToSubscribers(this);
        subscriptions.add(author);
    }

    public void unsubscribe(User author) {
        author.removeFromSubscribers(this);
        subscriptions.remove(author);
    }
    public EmailSender getSender(){
        return sender;
    }

    public double getCoins() {return coins;}


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String title ,String message) {
        sender.sendEmail(getEmail(),title,message);
    }
    public void notifySubscribers(String title,String text) {
        if(!subscribers.isEmpty())
            for (User subscriber : subscribers) {subscriber.update(title,text);}
    }


    public void addToSubscribers(User user){
        if(!subscribers.contains(user)) subscribers.add(user);
    }
    public void removeFromSubscribers(User user) {
        subscribers.remove(user);
    }






}