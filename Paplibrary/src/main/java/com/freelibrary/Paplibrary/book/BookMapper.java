package com.freelibrary.Paplibrary.book;


public class BookMapper {
    // convert book entity to book dto
    public static BookDto mapToBookDto(Book book){
        return BookDto.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publicationYear(book.getPublicationYear())
                .genre(book.getGenre())
                .description(book.getDescription())
                .starRating(book.getStarRating())
                .language(book.getLanguage())
                .hash((book.getHash()))
                .build();
    }

    // convert book dto to book entity
    public static Book mapToBook(BookDto bookDto){
        return Book.builder()
                .bookId(bookDto.getBookId())
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publicationYear(bookDto.getPublicationYear())
                .genre(bookDto.getGenre())
                .description(bookDto.getDescription())
                .starRating(bookDto.getStarRating())
                .language(bookDto.getLanguage())
                .hash(bookDto.getHash())
                .build();
    }
}