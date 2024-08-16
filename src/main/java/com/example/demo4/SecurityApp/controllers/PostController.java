package com.example.demo4.SecurityApp.controllers;

import com.example.demo4.SecurityApp.dto.PostDTO;
import com.example.demo4.SecurityApp.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
    public PostDTO getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost) {
        return postService.createNewPost(inputPost);
    }

    @GetMapping("/free-content")
    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan(principal, 'FREE')")
    public String getFreeContent(){
        return "This is FREE content accessible to all users .";
    }

    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan(principal, 'BASIC')")
    @GetMapping("/basic-content")
    public String getBasicContent() {
        return "This is BASIC content accessible to premium and basic plan users";
    }

    @PreAuthorize("@subscriptionService.hasAccessBasedOnPlan(principal, 'PREMIUM')")
    @GetMapping("/premium-content")
    public String getPremiumContent() {
        return ("This is PREMIUM content accessible only premium users");
    }

}
