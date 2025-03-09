package app.repository;

import app.model.ModerationStatus;
import app.model.ContentModeration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ContentModerationRepository extends JpaRepository<ContentModeration, UUID> {
    List<ContentModeration> findByUserId(UUID userId);
    List<ContentModeration> findByStatus(ModerationStatus status);
    Optional<ContentModeration> findByPostId(UUID postId);

}