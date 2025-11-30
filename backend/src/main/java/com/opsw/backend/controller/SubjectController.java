package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.SubjectApi;
import com.opsw.backend.dto.subject.request.SubjectCreateRequest;
import com.opsw.backend.dto.subject.request.SubjectIdRequest;
import com.opsw.backend.dto.subject.request.SubjectNameRequest;
import com.opsw.backend.dto.subject.request.SubjectUpdateRequest;
import com.opsw.backend.dto.subject.response.SubjectListResponse;
import com.opsw.backend.dto.subject.response.SubjectResponse;
import com.opsw.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController implements SubjectApi {

    private final SubjectService subjectService;

    @PostMapping
    @Override
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody SubjectCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(request));
    }

    @PatchMapping
    @Override
    public ResponseEntity<SubjectResponse> renameSubject(@RequestBody SubjectUpdateRequest request) {
        return ResponseEntity.ok(subjectService.renameSubject(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<SubjectResponse> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubject(new SubjectIdRequest(id)));
    }

    @GetMapping("/by-name")
    @Override
    public ResponseEntity<SubjectResponse> getSubjectByName(@RequestParam String name) {
        return ResponseEntity.ok(subjectService.getSubjectByName(new SubjectNameRequest(name)));
    }

    @GetMapping
    @Override
    public ResponseEntity<SubjectListResponse> listSubjects() {
        return ResponseEntity.ok(subjectService.listSubjects());
    }

    @GetMapping("/duplicate")
    @Override
    public ResponseEntity<Void> checkDuplicate(@RequestParam String name) {
        subjectService.checkDuplicatedName(new SubjectNameRequest(name));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> removeSubject(@PathVariable Long id) {
        subjectService.removeSubject(new SubjectIdRequest(id));
        return ResponseEntity.noContent().build();
    }
}
