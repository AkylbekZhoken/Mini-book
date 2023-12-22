package com.example.project_book.Class;

import java.util.List;

interface PrintedMatterCreator {
    PrintedMatter create();
}
class BookCreator implements PrintedMatterCreator{
    private String title ;
    private String author ;
    private List<String> genres ;
    private String description ;
    private double prices ;
    private double rating ;
    BookCreator(String title,String author,List<String> genres , String description,double prices,double rating){
        this.title = title;
        this.author = author;
        this.genres = genres ;
        this.description = description;
        this.prices = prices;
        this.rating = rating;

    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPrices(double prices) {
        this.prices = prices;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;

    }

    @Override
    public PrintedMatter create() {
        return new Book(this.title, this.author,this.genres,this.description,this.prices,this.rating);
    }
}