package com.opsw.backend.controller.docs;

import com.opsw.backend.dto.subject.request.SubjectCreateRequest;
import com.opsw.backend.dto.subject.request.SubjectUpdateRequest;
import com.opsw.backend.dto.subject.response.SubjectListResponse;
import com.opsw.backend.dto.subject.response.SubjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Subjects", description = "TOPCIT 과목 API")
public interface SubjectApi {

    @Operation(summary = "과목 생성", description = "새 과목 이름을 등록한다.")
    ResponseEntity<SubjectResponse> createSubject(SubjectCreateRequest request);

    @Operation(summary = "과목명 수정", description = "과목 ID를 기준으로 이름을 변경한다.")
    ResponseEntity<SubjectResponse> renameSubject(SubjectUpdateRequest request);

    @Operation(summary = "과목 단건 조회", description = "과목 ID로 과목을 조회한다.")
    ResponseEntity<SubjectResponse> getSubject(Long id);

    @Operation(summary = "이름으로 과목 조회", description = "과목 이름(대소문자 무시)으로 단일 과목을 조회한다.")
    ResponseEntity<SubjectResponse> getSubjectByName(String name);

    @Operation(summary = "과목 목록", description = "등록된 모든 과목을 조회한다.")
    ResponseEntity<SubjectListResponse> listSubjects();

    @Operation(summary = "과목명 중복 검사", description = "과목 이름 사용 가능 여부를 확인한다.")
    ResponseEntity<Void> checkDuplicate(String name);

    @Operation(summary = "과목 삭제", description = "과목 ID로 과목을 삭제한다.")
    ResponseEntity<Void> removeSubject(Long id);
}
