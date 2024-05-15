package com.freelibrary.Paplibrary.book;

import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.user.User;

import jakarta.persistence.*;
import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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




     @Column(name = "star_rating")
     private String starRating;

     @Column(name = "language", nullable = false)
     private String language;

     @Column(name = "hash")
     private String hash ;




//     @ManyToOne
//     @JoinColumn(name = "added_by", nullable = false)
//     private User createdBy;

     @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
     private Set<Comment> comments = new HashSet<>();
}


