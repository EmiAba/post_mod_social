package app.web;

import app.model.ContentModeration;
import app.repository.ContentModerationRepository;
import app.service.ModerationService;
import app.web.dto.ContentModerationRequest;
import app.web.dto.ContentModerationResponse;
import app.web.dto.ModerationDecisionRequest;
import app.web.mapper.ContentModerationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/moderation")
public class ModerationController {

    private final ModerationService moderationService;
    private final ContentModerationMapper mapper;



    @Autowired
    public ModerationController(ModerationService moderationService, ContentModerationMapper mapper) {
        this.moderationService = moderationService;
        this.mapper = mapper;

    }

    @PostMapping("/posts")
    public ResponseEntity<ContentModerationResponse> submitPost(@RequestBody ContentModerationRequest request) {
        ContentModeration post = moderationService.submitPostForModeration(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(post));
    }

    @PostMapping("/moderate")
    public ResponseEntity<ContentModerationResponse> moderatePost(@RequestBody ModerationDecisionRequest request) {

        ContentModeration post = moderationService.moderatePost(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toResponse(post));
    }

    @GetMapping("/posts/original/{postId}")
    public ResponseEntity<ContentModerationResponse> getPostByOriginalId(@PathVariable UUID postId) {
        ContentModeration post = moderationService.getPostByOriginalId(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toResponse(post));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ContentModerationResponse> getPost(@PathVariable UUID postId) {
        ContentModeration post = moderationService.getPost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toResponse(post));
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<ContentModerationResponse>> getUserPosts(@PathVariable UUID userId) {
        List<ContentModerationResponse> posts = moderationService.getUserPosts(userId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(posts);
    }

    @GetMapping("/posts/pending")
    public ResponseEntity<List<ContentModerationResponse>> getPendingPosts() {
        List<ContentModerationResponse> posts = moderationService.getPendingPosts()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(posts);
    }



    @PostMapping("/update/{postId}")
    public ResponseEntity<ContentModerationResponse> updatePost(
            @PathVariable UUID postId,
            @RequestBody ModerationDecisionRequest request
    ) {
        request.setPostId(postId);
        ContentModeration post = moderationService.moderatePost(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapper.toResponse(post));
    }



    @GetMapping("/posts/history")
    public ResponseEntity<List<ContentModerationResponse>> getAllModerationHistory() {
        List<ContentModeration> moderationHistory = moderationService.getModerationHistory();

        List<ContentModerationResponse> responses = moderationHistory.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}