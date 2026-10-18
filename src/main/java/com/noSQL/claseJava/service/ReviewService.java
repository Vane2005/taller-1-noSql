package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.ReviewRequest;
import com.noSQL.claseJava.entity.Book;
import com.noSQL.claseJava.entity.Review;
import com.noSQL.claseJava.entity.User;
import com.noSQL.claseJava.repository.ReviewRepository;
import com.noSQL.claseJava.repository.UserRepository;
import com.noSQL.claseJava.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Review createReview(ReviewRequest request) {
        Review review = Review.builder()
                .rating(request.getRating())
                .feedback(request.getFeedback())
                .user(request.getUser())
                .book(request.getBook())
                .build();

        review = reviewRepository.save(review);

        // Update user activity
        if (review.getUser() != null && review.getUser().getUserId() != null) {
            addReviewToUserActivity(review.getUser().getUserId(), review.getId());
        }

        // Update book average califications and total reviews
        if (review.getBook() != null && review.getBook().getBookId() != null) {
            updateBookReviewData(review.getBook().getBookId());
        }

        return review;
    }

    private void addReviewToUserActivity(String userId, String reviewId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResumeActivity() == null) {
                user.setResumeActivity(new java.util.ArrayList<>(5));
            }
            user.getResumeActivity().add(User.activityRef.builder()
                    .activityId(reviewId)
                    .nameActivity("review")
                    .build());
            userRepository.save(user);
        }
    }

    private void updateBookReviewData(String bookId) {
        List<Review> reviews = reviewRepository.findByBookBookId(bookId);
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            AtomicInteger totalRating = new AtomicInteger(0);
            reviews.forEach(r -> totalRating.addAndGet(Integer.parseInt(r.getRating())));
            float average = reviews.isEmpty() ? 0 : (float) totalRating.get() / reviews.size();
            book.setAverageCalifications(average);
            book.setReviewsTotal(reviews.size());
            bookRepository.save(book);
        }
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    public List<Review> getReviewsByUserId(String userId) {
        return reviewRepository.findByUserUserId(userId);
    }

    public List<Review> getReviewsByBookId(String bookId) {
        return reviewRepository.findByBookBookId(bookId);
    }

    public Review updateReview(String id, ReviewRequest request) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isPresent()) {
            Review review = existingReview.get();
            review.setRating(request.getRating());
            review.setFeedback(request.getFeedback());
            review.setUser(request.getUser());
            review.setBook(request.getBook());
            review = reviewRepository.save(review);

            // Update book average califications and total reviews after update
            if (review.getBook() != null && review.getBook().getBookId() != null) {
                updateBookReviewData(review.getBook().getBookId());
            }
            return review;
        }
        return null;
    }

    public boolean deleteReview(String id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            Review r = review.get();
            // Remove activity from user
            if (r.getUser() != null && r.getUser().getUserId() != null) {
                removeReviewFromUserActivity(r.getUser().getUserId(), id);
            }
            reviewRepository.deleteById(id);

            // Update book average califications and total reviews after deletion
            if (r.getBook() != null && r.getBook().getBookId() != null) {
                updateBookReviewData(r.getBook().getBookId());
            }
            return true;
        }
        return false;
    }

    private void removeReviewFromUserActivity(String userId, String reviewId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResumeActivity() != null) {
                user.getResumeActivity().removeIf(activity -> reviewId.equals(activity.getActivityId()));
                userRepository.save(user);
            }
        }
    }


}
