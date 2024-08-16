package com.example.demo4.SecurityApp.utils;

import com.example.demo4.SecurityApp.dto.PostDTO;
import com.example.demo4.SecurityApp.entities.PostEntity;
import com.example.demo4.SecurityApp.entities.User;
import com.example.demo4.SecurityApp.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class PostSecurity {

    private  final PostService postService;

    public boolean isOwnerOfPost(Long postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }

}
