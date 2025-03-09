package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ContentModerationRequest {
    @NotNull
    private UUID userId;

    @NotBlank
    private String content;

    @NotNull
    private UUID postId;


    public ContentModerationRequest() {
    }


    public ContentModerationRequest(UUID userId, String content, UUID postId) {
        this.userId = userId;
        this.content = content;
        this.postId = postId;
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }
}