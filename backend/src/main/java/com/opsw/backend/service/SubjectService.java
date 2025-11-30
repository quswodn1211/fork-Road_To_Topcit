package com.opsw.backend.service;

import com.opsw.backend.domain.Subject;
import com.opsw.backend.dto.subject.request.SubjectCreateRequest;
import com.opsw.backend.dto.subject.request.SubjectIdRequest;
import com.opsw.backend.dto.subject.request.SubjectNameRequest;
import com.opsw.backend.dto.subject.request.SubjectUpdateRequest;
import com.opsw.backend.dto.subject.response.SubjectListResponse;
import com.opsw.backend.dto.subject.response.SubjectResponse;
import com.opsw.backend.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectResponse createSubject(SubjectCreateRequest request) {
        validateName(request.name());
        if (subjectRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 과목 이름입니다");
        }
        Subject subject = Subject.builder()
                .name(request.name())
                .build();
        subjectRepository.save(subject);
        return SubjectResponse.toDto(subject);
    }

    @Transactional
    public SubjectResponse renameSubject(SubjectUpdateRequest request) {
        Subject subject = loadSubject(request.subjectId());
        validateName(request.name());
        if (!subject.getName().equalsIgnoreCase(request.name())
                && subjectRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 과목 이름입니다");
        }
        subject.rename(request.name());
        return SubjectResponse.toDto(subject);
    }

    public SubjectResponse getSubject(SubjectIdRequest request) {
        return SubjectResponse.toDto(loadSubject(request.subjectId()));
    }

    public SubjectResponse getSubjectByName(SubjectNameRequest request) {
        validateName(request.name());
        Subject subject = subjectRepository.findByNameIgnoreCase(request.name())
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을수 없습니다"));
        return SubjectResponse.toDto(subject);
    }

    public SubjectListResponse listSubjects() {
        return SubjectListResponse.toDto(subjectRepository.findAll());
    }

    public void checkDuplicatedName(SubjectNameRequest request) {
        validateName(request.name());
        if (subjectRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 과목 이름입니다");
        }
    }

    @Transactional
    public void removeSubject(SubjectIdRequest request) {
        Subject subject = loadSubject(request.subjectId());
        subjectRepository.delete(subject);
    }

    private Subject loadSubject(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("과목을 찾을수 없습니다"));
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("과목 이름은 필수입니다");
        }
    }
}
