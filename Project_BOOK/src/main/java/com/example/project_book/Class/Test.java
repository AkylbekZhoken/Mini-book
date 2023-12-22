package com.example.project_book.Class;

public class Test {
    public static void main(String[] args) {
        CoreLogic logic=new CoreLogic(DatabaseConnection.getInstance());
        logic.getAvailableBooks().stream().forEach(System.out::println);
    }
}
