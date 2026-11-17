package mountain.service.impl;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import tools.jackson.databind.JsonNode;

import mountain.mapper.MountainMapper;
import mountain.service.MountainService;
import mountain.vo.MountainVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {

    private final MountainMapper mountainMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<MountainVO> getMountainList() {

        return mountainMapper.selectMountainList();
    }

    @Override
    public MountainVO searchCoordinates(String name) {

        try {

            String encodedName = URLEncoder.encode(
                    name + " 대한민국",
                    StandardCharsets.UTF_8.toString()
            );

            String url =
                    "https://nominatim.openstreetmap.org/search"
                            + "?format=jsonv2"
                            + "&limit=5"
                            + "&countrycodes=kr"
                            + "&q=" + encodedName;

            HttpHeaders headers = new HttpHeaders();

            headers.set(
                    "User-Agent",
                    "JayMountainRecord/1.0 (contact: YOUR_EMAIL)"
            );

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response =
                    restTemplate.exchange(
                            URI.create(url),
                            HttpMethod.GET,
                            entity,
                            JsonNode.class
                    );

            JsonNode results = response.getBody();

            if (results == null || !results.isArray()
                    || results.isEmpty()) {

                throw new IllegalArgumentException(
                        "해당 산의 좌표를 찾을 수 없습니다."
                );
            }

            JsonNode result = results.get(0);

            MountainVO mountain = new MountainVO();

            mountain.setName(name);
            mountain.setLatitude(
                    result.get("lat").asDouble()
            );
            mountain.setLongitude(
                    result.get("lon").asDouble()
            );

            return mountain;

        } catch (Exception e) {

            throw new RuntimeException(
                    "산 좌표 검색에 실패했습니다.",
                    e
            );
        }
    }

    @Override
    public MountainVO addMountain(MountainVO mountain) {

        if (mountain.getName() == null
                || mountain.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "산 이름을 입력해주세요."
            );
        }

        if (mountain.getLatitude() == null
                || mountain.getLongitude() == null
                || mountain.getLatitude() < -90
                || mountain.getLatitude() > 90
                || mountain.getLongitude() < -180
                || mountain.getLongitude() > 180) {

            throw new IllegalArgumentException(
                    "올바른 좌표가 아닙니다."
            );
        }

        mountain.setName(mountain.getName().trim());

        if (mountainMapper.selectMountainByName(
                mountain.getName()) != null) {

            throw new IllegalArgumentException(
                    "이미 등록된 산입니다."
            );
        }

        mountainMapper.insertMountain(mountain);

        return mountain;
    }
}