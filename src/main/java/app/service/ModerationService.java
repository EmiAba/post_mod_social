package app.service;

import app.model.ContentModeration;
import app.model.ModerationStatus;
import app.repository.ContentModerationRepository;
import app.web.dto.ContentModerationRequest;
import app.web.dto.ModerationDecisionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ModerationService {

    private final ContentModerationRepository contentModerationRepository;

    @Autowired
    public ModerationService(ContentModerationRepository contentModerationRepository) {
        this.contentModerationRepository = contentModerationRepository;
    }

    public ContentModeration submitPostForModeration(ContentModerationRequest request) {
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

        return contentModerationRepository.save(post);
    }

    public ContentModeration moderatePost(ModerationDecisionRequest request) {
        // FIX: Look up by postId instead of ID
        ContentModeration post = contentModerationRepository.findByPostId(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Moderation for post ID " + request.getPostId() + " not found"));

        post.setStatus(request.isApproved() ? ModerationStatus.APPROVED : ModerationStatus.REJECTED);

        if (!request.isApproved() && request.getReason() != null) {
            post.setModerationReason(request.getReason());
        }

        return contentModerationRepository.save(post);
    }

    public ContentModeration getPost(UUID postId) {
        return contentModerationRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public ContentModeration getPostByOriginalId(UUID postId) {
        return contentModerationRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("Moderation for post ID " + postId + " not found"));
    }

    public List<ContentModeration> getUserPosts(UUID userId) {
        return contentModerationRepository.findByUserId(userId);
    }

    public List<ContentModeration> getPendingPosts() {
        return contentModerationRepository.findByStatus(ModerationStatus.PENDING);
    }
}