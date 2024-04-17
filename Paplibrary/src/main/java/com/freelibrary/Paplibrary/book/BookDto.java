package com.freelibrary.Paplibrary.book;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long bookId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    @NotEmpty
    private String publicationYear;

    @NotEmpty
    private String genre;

    @NotEmpty
    private String description;

    private String coverLink;

    @NotEmpty
    private String starRating;

    @NotEmpty
    private String language;

    @NotEmpty
    private String path;}
