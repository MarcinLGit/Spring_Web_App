package com.freelibrary.Paplibrary.user;

import com.freelibrary.Paplibrary.book.Book;
import com.freelibrary.Paplibrary.book.BookMapper;
import com.freelibrary.Paplibrary.book.BookRepository;
import com.freelibrary.Paplibrary.book.BookService;
import com.freelibrary.Paplibrary.comment.Comment;
import com.freelibrary.Paplibrary.comment.CommentRepository;
import com.freelibrary.Paplibrary.rating.Rating;
import com.freelibrary.Paplibrary.rating.RatingRepository;
import com.freelibrary.Paplibrary.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           BookRepository bookRepository,
                           CommentRepository commentRepository,
                           RatingRepository ratingRepository
                          ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setLogin(registrationDto.getFirstName() + " " + registrationDto.getLastName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword_hash(passwordEncoder.encode(registrationDto.getPassword()));
        Role role = roleRepository.findByRoleName("ROLE_GUEST"); //TODO
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }



    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserId(Long userId){
        return userRepository.findById(userId).get();
    }

   // @PreAuthorize("hasRole('ROLE_USER')")
   // public void deleteAccount() {
   //     Long userID = SecurityUtils.getCurrentUser().getAuthorities().;
   //     userRepository.deleteById(userID);
//
   // }

 //   public void updateBadgeUser(int id) {
 //       Badge badge = findBadgeById(id);
 //       badge.setUser(null);
 //   }



    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @Override
    public void deleteUserByID(Long id) {
        String email = SecurityUtils.getCurrentUser().getUsername();
        User currentUser = userRepository.findByEmail(email);
        User userToDelete = userRepository.findById(id).get();
        if(currentUser.getId().equals(userToDelete.getId())) {
            throw new SecurityException("You are not allowed to delete this user. You are trying to delete yours account");
        }

        List<Book> userBooks = bookRepository.findBooksByUser(id);
        for(Book book : userBooks) {
            List<Comment> comments= commentRepository.findCommentsByBookId(book.getBookId());
            List<Rating> ratings = ratingRepository.findByBook(bookRepository.findById(book.getBookId()).get());
            for(Comment comment : comments) {
                commentRepository.delete(comment);
            }
            for(Rating rating : ratings) {
                ratingRepository.delete(rating);
            }
            bookRepository.deleteById(book.getBookId());
        }
        userToDelete.getRoles().clear();
        userRepository.deleteById(id);
    }
}
