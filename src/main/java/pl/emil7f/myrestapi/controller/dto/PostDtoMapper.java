package pl.emil7f.myrestapi.controller.dto;

import pl.emil7f.myrestapi.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostDtoMapper {

    private PostDtoMapper(){
            }

    public static List<PostDto> mapToPostDtos(List<Post> posts) {
        return posts.stream()
                .map(post -> mapToPostDto(post))
                .collect(Collectors.toList());
    }

    public static PostDto mapToPostDto(Post post ) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreated(post.getCreated());

        return  postDto;
    }
}
