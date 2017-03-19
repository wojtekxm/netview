package zesp03.webapp.data.row;

import zesp03.common.entity.Unit;



public class UnitRow {

    private long id;
    private String code;
    private String description;

    public UnitRow(){

    }

    public UnitRow(Unit u){
        this.id = u.getId();
        this.code = u.getCode();
        this.description = u.getDescription();

    }
    public UnitRow(long id, String code, String description){

        this.id = id;
        this.code = code;
        this.description = description;
    }

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

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        UnitRow other = (UnitRow)obj;
        if( !(Long.valueOf( id ).equals( Long.valueOf( other.getId() ) )) )
            return false;

        return true;
    }
    public int hashCode(){
        return (int)id;
    }
}