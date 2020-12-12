package pl.emil7f.myrestapi.controller;


import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.emil7f.myrestapi.controller.dto.PostDto;
import pl.emil7f.myrestapi.controller.dto.PostDtoMapper;
import pl.emil7f.myrestapi.model.Post;
import pl.emil7f.myrestapi.service.PostService;

import java.util.List;


@RestController
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<PostDto> getPosts(@RequestParam(required = false) Integer page, Sort.Direction sort,
                                  @AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {

        int pageNumber = page != null && page >= 0 ? page : 0;
        if (sort == null) {
            sort = Sort.Direction.ASC;
        }
        return PostDtoMapper.mapToPostDtos(postService.getPosts(pageNumber, sort));
    }

    //  example  without sorting       http://localhost:8080/posts/comments?page=0
    //  example  with sorting          http://localhost:8080/posts/comments?page=0&sort=ASC
    @GetMapping("/posts/comments")
    public List<Post> getPostsWithComment(@RequestParam(required = false) Integer page, Sort.Direction sort) {
        int pageNumber = page != null && page >= 0 ? page : 0;
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        return postService.getPostsWithComments(pageNumber, sortDirection);
    }

    //  example    http://localhost:8080/posts/1
    @GetMapping("/posts/{id}")
    public Post getSinglePost(@PathVariable Long id) {
        return postService.getSinglePost(id);
    }

    @PostMapping("/posts")
    public Post addPost(@RequestBody Post post){
    return  postService.addPost(post);
    }


    @PutMapping
    public Post editPost(@RequestBody Post post){
        return  postService.editPost(post);
    }


    // metoda nie usuwa tutaj kaskadowo, czyli w momencie gdy post będzie miał komentarze
    // dostaniemy 500 :)  Post bez komentarzy zostanie usunięty.
    // Aby to naprawić trzeba dodać parametr cascade do klucza obcego w adnotacji @OneToMany
    // @OneToMany(cascade = CascadeType.REMOVE) -> zostanie użyte tylko przy usuwaniu. ALL zezwoli na wszystko.
    @DeleteMapping("/posts{id}")
    public void deletePost(@PathVariable long id){

         postService.deletePost(id);
    }

}
