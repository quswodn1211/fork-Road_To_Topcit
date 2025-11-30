package com.opsw.backend.dto.usersubjectprogress.response;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.domain.user.User;
import com.opsw.backend.domain.user.UserSubjectProgress;

public record UserSubjectProgressResponse(
        Long id,
        Long userId,
        Long subjectId,
        int experience
) {

    public static UserSubjectProgressResponse toDto(UserSubjectProgress progress) {
        if (progress == null) {
            return null;
        }
        User user = progress.getUser();
        Subject subject = progress.getSubject();
        return new UserSubjectProgressResponse(
                progress.getId(),
                user != null ? user.getId() : null,
                subject != null ? subject.getId() : null,
                progress.getExperience()
        );
    }
}
