package app.web.mapper;

import app.model.ContentModeration;
import app.web.dto.ContentModerationResponse;
import org.springframework.stereotype.Component;

@Component
public class ContentModerationMapper {

    public ContentModerationResponse toResponse(ContentModeration entity) {
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
}