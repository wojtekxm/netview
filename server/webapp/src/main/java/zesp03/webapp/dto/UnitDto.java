package zesp03.webapp.dto;

import zesp03.common.entity.Unit;

public class UnitDto {
    private long id;
    private String code;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void wrap(Unit u) {
        this.id = u.getId();
        this.code = u.getCode();
        this.description = u.getDescription();
    }

    public static UnitDto make(Unit u) {
        UnitDto dto = new UnitDto();
        dto.wrap(u);
        return dto;
    }
}
