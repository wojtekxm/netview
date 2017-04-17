package zesp03.common.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zesp03.common.entity.LinkUnitBuilding;

public interface LinkUnitBuildingRepository extends CrudRepository<LinkUnitBuilding, Long> {
    @Modifying
    @Query("DELETE FROM LinkUnitBuilding lub WHERE lub.building.id = ?1 AND lub.unit.id = ?2")
    void deleteBuildingUnit(Long buildingId, Long unitId);
}
