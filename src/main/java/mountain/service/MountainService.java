package mountain.service;

import java.util.List;

import mountain.vo.MountainVO;

public interface MountainService {

    List<MountainVO> getMountainList();

    // 산 검색
    MountainVO searchCoordinates(String name);

    // 산행 추가
    MountainVO addMountain(MountainVO mountain);
}