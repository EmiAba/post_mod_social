package app.web;

import app.service.ModerationService;
import app.web.dto.ContentModerationRequest;
import app.web.dto.ContentModerationResponse;
import app.web.dto.ModerationDecisionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/moderation")
public class ModerationController {

    private final ModerationService moderationService;

    @Autowired
    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping("/posts")
    public ResponseEntity<ContentModerationResponse> submitPost(@RequestBody ContentModerationRequest request) {
        ContentModerationResponse response = moderationService.submitPostForModeration(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/moderate")
    public ResponseEntity<ContentModerationResponse> moderatePost(@RequestBody ModerationDecisionRequest request) {
        ContentModerationResponse response = moderationService.moderatePost(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/posts/original/{postId}")
    public ResponseEntity<ContentModerationResponse> getPostByOriginalId(@PathVariable UUID postId) {
        ContentModerationResponse response = moderationService.getPostByOriginalId(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/posts/pending")
    public ResponseEntity<List<ContentModerationResponse>> getPendingPosts() {
        List<ContentModerationResponse> posts = moderationService.getPendingPosts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(posts);
    }

    @GetMapping("/posts/history")
    public ResponseEntity<List<ContentModerationResponse>> getAllModerationHistory() {
        List<ContentModerationResponse> responses = moderationService.getModerationHistory();
        return ResponseEntity.ok(responses);
    }
}