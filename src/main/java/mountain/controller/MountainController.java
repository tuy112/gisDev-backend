package mountain.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mountain.service.MountainService;
import mountain.vo.MountainVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mountains")
@RequiredArgsConstructor
public class MountainController {

    private final MountainService mountainService;

    // 산행 목록 조회
    @GetMapping
    public ResponseEntity<List<MountainVO>> getMountainList() {

        return ResponseEntity.ok(
                mountainService.getMountainList()
        );
    }

    // 산 좌표 검색
    @GetMapping("/geocode")
    public ResponseEntity<MountainVO> searchCoordinates(
            @RequestParam String name) {

        return ResponseEntity.ok(
                mountainService.searchCoordinates(name)
        );
    }

    // 산행 추가
    @PostMapping
    public ResponseEntity<MountainVO> addMountain(
            @RequestBody MountainVO mountain) {

        return ResponseEntity.ok(
                mountainService.addMountain(mountain)
        );
    }
}