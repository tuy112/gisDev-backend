package mountain.vo;

import lombok.Data;

@Data
public class MountainVO {

    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String regDtime;
}