package com.opsw.backend.controller;

import com.opsw.backend.controller.docs.WorldApi;
import com.opsw.backend.dto.world.request.WorldCreateRequest;
import com.opsw.backend.dto.world.request.WorldIdRequest;
import com.opsw.backend.dto.world.request.WorldNameRequest;
import com.opsw.backend.dto.world.request.WorldUpdateRequest;
import com.opsw.backend.dto.world.response.WorldListResponse;
import com.opsw.backend.dto.world.response.WorldResponse;
import com.opsw.backend.service.WorldService;
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
@RequestMapping("/api/worlds")
@RequiredArgsConstructor
public class WorldController implements WorldApi {

    private final WorldService worldService;

    @PostMapping
    @Override
    public ResponseEntity<WorldResponse> createWorld(@RequestBody WorldCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(worldService.createWorld(request));
    }

    @PatchMapping
    @Override
    public ResponseEntity<WorldResponse> updateWorld(@RequestBody WorldUpdateRequest request) {
        return ResponseEntity.ok(worldService.updateWorld(request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteWorld(@PathVariable Long id) {
        worldService.removeWorld(new WorldIdRequest(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<WorldResponse> getWorld(@PathVariable Long id) {
        return ResponseEntity.ok(worldService.getWorld(new WorldIdRequest(id)));
    }

    @GetMapping("/by-name")
    @Override
    public ResponseEntity<WorldResponse> getWorldByName(@RequestParam String name) {
        return ResponseEntity.ok(worldService.getWorldByName(new WorldNameRequest(name)));
    }

    @GetMapping
    @Override
    public ResponseEntity<WorldListResponse> listWorlds() {
        return ResponseEntity.ok(worldService.listWorlds());
    }

    @GetMapping("/duplicate")
    @Override
    public ResponseEntity<Void> checkWorldName(@RequestParam String name) {
        worldService.checkDuplicatedName(new WorldNameRequest(name));
        return ResponseEntity.ok().build();
    }
}
