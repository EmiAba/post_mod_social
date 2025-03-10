package app.web;

import app.model.ContentModeration;

import app.service.ModerationService;
import app.web.dto.ContentModerationRequest;
import app.web.dto.ContentModerationResponse;
import app.web.dto.ModerationDecisionRequest;
import app.web.mapper.ContentModerationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
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

//used
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





    @GetMapping("/posts/history")
    public ResponseEntity<List<ContentModerationResponse>> getAllModerationHistory() {


        List<ContentModeration> moderationHistory = moderationService.getModerationHistory();

        List<ContentModerationResponse> responses = moderationHistory.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}