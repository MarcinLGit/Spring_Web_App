package com.freelibrary.Paplibrary.book;


import com.freelibrary.Paplibrary.Language;

public class Book {
     private int id;
     private  String name;
     private  String author;
     private Language language;

     private  String publishing_house;
     private  int publication_year;
     private float rating;
     private int votes;
     private float data_size;

     public Book(int id, String name, String author, Language language, String publishing_house,
                 int publication_year, float rating,int votes, float data_size) {
          this.id = id;
          this.name = name;
          this.author = author;
          this.language = language;
          this.publishing_house = publishing_house;
          this.publication_year = publication_year;
          this.rating = rating;
          this.votes= votes;
          this.data_size = data_size;
     }

     public int getVotes() {
          return votes;
     }

     public void setVotes(int votes) {
          this.votes = votes;
     }

     public int getId() {
          return id;
     }

     public String getName() {
          return name;
     }

     public String getAuthor() {
          return author;
     }

     public Language getLanguage() {
          return language;
     }

     public String getPublishing_house() {
          return publishing_house;
     }

     public int getPublication_year() {
          return publication_year;
     }

     public float getRating() {
          return rating;
     }

     public float getData_size() {
          return data_size;
     }

     public void setId(int id) {
          this.id = id;
     }

     public void setName(String name) {
          this.name = name;
     }

     public void setAuthor(String author) {
          this.author = author;
     }

     public void setLanguage(Language language) {
          this.language = language;
     }

     public void setPublishing_house(String publishing_house) {
          this.publishing_house = publishing_house;
     }

     public void setPublication_year(int publication_year) {
          this.publication_year = publication_year;
     }

     public void setRating(float rating) {
          this.rating = rating;
     }

     public void setData_size(float data_size) {
          this.data_size = data_size;
     }

     @Override
     public String toString() {
          return "Book{" +
                  "id=" + id +
                  ", name='" + name + '\'' +
                  ", author='" + author + '\'' +
                  ", language=" + language +
                  ", publishing_house='" + publishing_house + '\'' +
                  ", publication_year=" + publication_year +
                  ", rating=" + rating +
                  ", votes=" + votes +
                  ", data_size=" + data_size +
                  '}';
     }
}

