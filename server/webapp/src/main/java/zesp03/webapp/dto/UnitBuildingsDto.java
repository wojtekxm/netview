/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.dto;

import zesp03.common.entity.Unit;

import java.util.List;
import java.util.stream.Collectors;

public class UnitBuildingsDto {
    private UnitDto unit;
    private List<BuildingDto> buildings;

    public UnitDto getUnit() {
        return unit;
    }

    public void setUnit(UnitDto unit) {
        this.unit = unit;
    }

    public List<BuildingDto> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingDto> buildings) {
        this.buildings = buildings;
    }

    public void wrap(Unit u) {
        this.unit = UnitDto.make(u);
        this.buildings = u.getLubList()
                .stream()
                .map(lub -> BuildingDto.make(lub.getBuilding()))
                .collect(Collectors.toList());
    }
        public static UnitBuildingsDto make(Unit u) {
        UnitBuildingsDto dto = new UnitBuildingsDto();
            dto.wrap(u);
            return dto;
        }
    }
