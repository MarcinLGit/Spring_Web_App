package com.freelibrary.Paplibrary.book;

import com.freelibrary.Paplibrary.user.User;

import jakarta.persistence.*;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long bookId;

     @Column(name = "title", nullable = false)
     private String title;

     @Column(name = "author", nullable = false)
     private String author;

     @Column(name = "publication_year", nullable = false)
     private String publicationYear;

     @Column(name = "genre", nullable = false)
     private String genre;

     @Column(name = "description", nullable = false)
     private String description;

     @Column(name = "cover_link")
     private String coverLink;

     @Column(name = "star_rating", nullable = false)
     private String starRating;

     @Column(name = "language", nullable = false)
     private String language;

     @Column(name = "path", nullable = false)
     private String path;
}
/*
     @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinTable(
             name = "user_book",
             joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "book_id")},
             inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")}
     )
     private List<User> users = new ArrayList<>();
     //  is it good with list of users?

     @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinTable(
             name = "book_comment",
             joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "book_id")},
             inverseJoinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "comment_id")}
     )
     private List<Comment> comments = new ArrayList<>();
*/


