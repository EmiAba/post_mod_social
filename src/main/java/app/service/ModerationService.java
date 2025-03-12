package app.service;

import app.model.ContentModeration;
import app.model.ModerationStatus;
import app.repository.ContentModerationRepository;
import app.web.dto.ContentModerationRequest;
import app.web.dto.ContentModerationResponse;
import app.web.dto.ModerationDecisionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ModerationService {

    private final ContentModerationRepository contentModerationRepository;

    @Autowired
    public ModerationService(ContentModerationRepository contentModerationRepository) {
        this.contentModerationRepository = contentModerationRepository;
    }


    private ContentModerationResponse convertToResponse(ContentModeration entity) {
        if (entity == null) {
            return null;
        }

        ContentModerationResponse response = new ContentModerationResponse();
        response.setId(entity.getId());
        response.setPostId(entity.getPostId());
        response.setContent(entity.getContent());
        response.setStatus(entity.getStatus());
        response.setCreatedOn(entity.getCreatedOn());
        response.setModerationReason(entity.getModerationReason());
        response.setUserId(entity.getUserId());

        return response;
    }



    public ContentModerationResponse submitPostForModeration(ContentModerationRequest request) {
        List<String> inappropriateWords = Arrays.asList(
                "offensive", "inappropriate", "hate",
                "violent", "racist"
        );

        String contentLowerCase = request.getContent().toLowerCase();

        boolean potentiallyInappropriate = inappropriateWords.stream()
                .anyMatch(contentLowerCase::contains);

        ContentModeration post = new ContentModeration();
        post.setContent(request.getContent());
        post.setUserId(request.getUserId());
        post.setPostId(request.getPostId());
        post.setCreatedOn(LocalDateTime.now());
        post.setStatus(potentiallyInappropriate ? ModerationStatus.PENDING : ModerationStatus.APPROVED);

        ContentModeration savedPost = contentModerationRepository.save(post);
        return convertToResponse(savedPost);
    }

    public ContentModerationResponse moderatePost(ModerationDecisionRequest request) {
        ContentModeration post = contentModerationRepository.findByPostId(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Moderation for post ID " + request.getPostId() + " not found"));

        post.setStatus(request.isApproved() ? ModerationStatus.APPROVED : ModerationStatus.REJECTED);

        if (!request.isApproved() && request.getReason() != null) {
            post.setModerationReason(request.getReason());
        }

        ContentModeration savedPost = contentModerationRepository.save(post);
        return convertToResponse(savedPost);
    }

    public ContentModerationResponse getPostByOriginalId(UUID postId) {
        ContentModeration post = contentModerationRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Moderation for post ID " + postId + " not found"));
        return convertToResponse(post);
    }

    public List<ContentModerationResponse> getPendingPosts() {
        List<ContentModeration> pendingPosts = contentModerationRepository.findByStatus(ModerationStatus.PENDING);
        return pendingPosts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ContentModerationResponse> getModerationHistory() {
        List<ContentModeration> moderationHistory = contentModerationRepository.findAll();
        return moderationHistory.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}