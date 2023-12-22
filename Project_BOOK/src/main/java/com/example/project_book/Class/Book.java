package com.example.project_book.Class;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.Objects;


interface PrintedMatter {
    int id();
    String title();
    List<String> genres();
    String description();
    double prices();
    String author();
    double rating();
}

public class Book implements PrintedMatter {
    private int id;
    private final String title;
    private final String author;
    private final List<String> genres;
    private final String description;
    private final double prices;
    double rating ;

    public Book(String title, String author, List<String> genres, String description, double prices, double rating) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.description = description;
        this.prices = prices;
        this.rating = rating;

    }
    public SimpleStringProperty getTitle(){
        return new SimpleStringProperty(title);
    }
    public SimpleStringProperty getAuthor(){
        return new SimpleStringProperty(author);
    }
    public SimpleStringProperty getGenre(){
        return new SimpleStringProperty(genres.toString());
    }
    public SimpleStringProperty getDescription(){
        return new SimpleStringProperty(description);
    }

    public SimpleStringProperty getPrice(){
        return new SimpleStringProperty( Double.toString(prices));
    }
    public SimpleStringProperty getRating(){
        return new SimpleStringProperty(Double.toString(rating));
    }

    public void setId(int id){
        this.id = id;
    }

    public int id() {
        return id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String author() {
        return author;
    }

    @Override
    public double rating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public List<String> genres() {
        return genres;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public double prices() {
        return prices;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Book) obj;
        return Objects.equals(this.title, that.title) &&
                Objects.equals(this.author, that.author) &&
                Objects.equals(this.genres, that.genres) &&
                Objects.equals(this.description, that.description) &&
                Double.doubleToLongBits(this.prices) == Double.doubleToLongBits(that.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, genres, description, prices);
    }

    @Override
    public String toString() {
        return "Book[" +
                "title=" + title + ", " +
                "author=" + author + ", " +
                "genres=" + genres + ", " +
                "description=" + description + ", " +
                "prices=" + prices + ']';
    }


}
