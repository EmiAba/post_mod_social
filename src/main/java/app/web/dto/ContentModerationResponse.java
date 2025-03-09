package app.web.dto;

import app.model.ModerationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContentModerationResponse {
    @NotNull
    private UUID id;
    @NotNull
    private UUID postId;
    @NotBlank
    private String content;
    @NotNull
    private ModerationStatus status;
    @NotNull
    private LocalDateTime createdOn;
    private String moderationReason;
    @NotNull
    private UUID userId;


    public ContentModerationResponse() {
    }


    public ContentModerationResponse(UUID id, UUID postId, String content,
                                     ModerationStatus status, LocalDateTime createdOn,
                                     String moderationReason, UUID userId) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.status = status;
        this.createdOn = createdOn;
        this.moderationReason = moderationReason;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModerationStatus getStatus() {
        return status;
    }

    public void setStatus(ModerationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}