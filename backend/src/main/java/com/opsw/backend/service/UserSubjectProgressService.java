package com.opsw.backend.service;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.domain.user.User;
import com.opsw.backend.domain.user.UserSubjectProgress;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressGainRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressListQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressQuery;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressResetRequest;
import com.opsw.backend.dto.usersubjectprogress.request.UserSubjectProgressStartRequest;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressListResponse;
import com.opsw.backend.dto.usersubjectprogress.response.UserSubjectProgressResponse;
import com.opsw.backend.repository.SubjectRepository;
import com.opsw.backend.repository.UserRepository;
import com.opsw.backend.repository.UserSubjectProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSubjectProgressService {

    private final UserSubjectProgressRepository userSubjectProgressRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public UserSubjectProgressResponse startProgress(UserSubjectProgressStartRequest request) {
        UserSubjectProgress progress = userSubjectProgressRepository
                .findByUserIdAndSubjectId(request.userId(), request.subjectId())
                .orElseGet(() -> {
                    User user = loadUser(request.userId());
                    Subject subject = loadSubject(request.subjectId());
                    UserSubjectProgress created = UserSubjectProgress.start(user, subject);
                    userSubjectProgressRepository.save(created);
                    return created;
                });
        return UserSubjectProgressResponse.toDto(progress);
    }

    @Transactional
    public UserSubjectProgressResponse gainExperience(UserSubjectProgressGainRequest request) {
        UserSubjectProgress progress = loadProgress(request.userId(), request.subjectId());
        progress.gain(request.amount());
        return UserSubjectProgressResponse.toDto(progress);
    }

    @Transactional
    public UserSubjectProgressResponse resetExperience(UserSubjectProgressResetRequest request) {
        UserSubjectProgress progress = loadProgress(request.userId(), request.subjectId());
        progress.reset();
        return UserSubjectProgressResponse.toDto(progress);
    }

    public UserSubjectProgressResponse getProgress(UserSubjectProgressQuery request) {
        return UserSubjectProgressResponse.toDto(loadProgress(request.userId(), request.subjectId()));
    }

    public UserSubjectProgressListResponse listProgresses(UserSubjectProgressListQuery request) {
        return UserSubjectProgressListResponse.toDto(
                userSubjectProgressRepository.findByUserIdOrderBySubjectNameAsc(request.userId())
        );
    }

    private UserSubjectProgress loadProgress(Long userId, Long subjectId) {
        return userSubjectProgressRepository.findByUserIdAndSubjectId(userId, subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Progress not found"));
    }

    private User loadUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Subject loadSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
    }
}
