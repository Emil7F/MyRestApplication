package pl.emil7f.myrestapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.emil7f.myrestapi.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Odpytac baze można za pomocą adnotacji Query
    // @Query("SELECT p FROM Post p WHERE title = :title")

    // Tworzenie metod przy pomocy konwencji nazewniczej SpringDataJpa
   //  Post findPostByCommentContainingAndCreated();

    // Dodawanie stronnicowania za pomocą Pageable

    @Query("Select p From Post p")
    List<Post> findAllPosts(Pageable page);

}
