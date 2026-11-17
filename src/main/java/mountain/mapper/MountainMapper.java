package mountain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import mountain.vo.MountainVO;

@Mapper
public interface MountainMapper {

    List<MountainVO> selectMountainList();

    MountainVO selectMountainByName(
            @Param("name") String name
    );

    int insertMountain(MountainVO mountain);
}