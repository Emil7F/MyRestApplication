package pl.emil7f.myrestapi.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.emil7f.myrestapi.model.Comment;
import pl.emil7f.myrestapi.model.Post;
import pl.emil7f.myrestapi.repository.CommentRepository;
import pl.emil7f.myrestapi.repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    public static final int PAGE_SIZE = 20;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // wstrzyknięcie PostRepository do PostService   -> można też adnotacją lombok @RequiredArgsConstructor
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<Post> getPosts(int page, Sort.Direction sort) {
        return postRepository.findAllPosts(
                PageRequest.of(page, PAGE_SIZE,
                        Sort.by(sort, "id")));
    }

    @Cacheable(cacheNames = "SinglePost", key = "#id")
    public Post getSinglePost(Long id) {
        return postRepository.findById(id)
                .orElseThrow();
    }

    @Cacheable(cacheNames = "PostsWithComments")
    public List<Post> getPostsWithComments(int page, Sort.Direction sort) {
        List<Post> allPosts = postRepository.findAllPosts(
                PageRequest.of(
                        page, PAGE_SIZE, Sort.by(sort, "id")));

        List<Long> ids = allPosts.stream()
                .map(post -> post.getId())
                .collect(Collectors.toList());
        List<Comment> comments = commentRepository.findAllByPostIdIn(ids);
        allPosts.forEach(post -> post.setComment(extractComment(comments, post.getId())));
        return allPosts;
    }

    private List<Comment> extractComment(List<Comment> comments, long id) {
        return comments.stream()
                .filter(comment -> comment.getPostId() == id)
                .collect(Collectors.toList());
    }

    public Post addPost(Post post) {
        return postRepository.save(post);
    }

    /**
     * @CachePut - za kazdym razem gdy wywoływana jest metoda, jej wynik zostaje zapisany do cache'a
     * Taka adnotacja przydaje się kiedy edytujemy posty. Nalezy podać CacheName.
     * Taki cache zostaje przeładowany.
     * Koniecznie trzeba ustawić key, bo domyślnie naszym kluczem jest Post, a tego nie chcemy.
     * Pobieramy posta po id, dlatego też chcemy zapisywać posty po id
     * result to jest to co zwraca nasza metoda -> czyli posta
     */
    @Transactional
    @CachePut(cacheNames = "SinglePost", key = "#result.id")
    public Post editPost(Post post) {
        Post postEdited = postRepository.findById(post.getId()).orElseThrow();
        postEdited.setTitle(post.getTitle());
        postEdited.setContent(post.getContent());
        // return postRepository.save(postEdited);       // nie trzeba
        return postEdited;
    }

    /**
    *    Do czyszczenie Cache'a używamy @CacheEvict
    *   Należy dodać jej odpowiednią nazwe, metoda wyszukuje po id, także nie trzeba ustawiać klucza.
    *      Metoda ta jest ważna, bez jej zastosowania, po usunięciu posta w serwisie, dalej będzie on zapisany w cache'u
     */
    @CacheEvict(cacheNames = "SinglePost")
    public void deletePost(long id) {
        postRepository.deleteById(id);
    }

    /* Jeśli chcemy usunąć cały cache to można stworzyć całą metode dla tego cacha
    @CacheEvict(cacheNames = "PostsWithComments")
    public void clearPostsWithComments(){
    }
    */
}
