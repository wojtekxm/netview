package zesp03.data.row;

import zesp03.entity.Unit;

/**
 * Created by Berent on 2017-03-06.
 */
public class UnitRow {

    private Long id;
    private String code;
    private String description;

    public UnitRow(){

    }

    public UnitRow(Unit u){
        this.id = u.getId();
        this.code = u.getCode();
        this.description = u.getDescription();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

}
