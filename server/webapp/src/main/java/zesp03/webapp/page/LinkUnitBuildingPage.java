package zesp03.webapp.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.service.BuildingService;
import zesp03.webapp.service.UnitService;

import java.util.List;

@Controller
public class LinkUnitBuildingPage {
    @Autowired
    private UnitService unitService;

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/unitsbuildings")
    public String get(ModelMap model) {
        List<LinkUnitBuildingDto> list = unitService.getAllLinkUnitBuildings();
        model.put("list", list);
        return "link-unit-building";
    }

    @GetMapping("/link-unit-all-buildings")
    public String get(@RequestParam("id") long id,
                      ModelMap model) {
        UnitDto u = unitService.getOne(id);
        List<BuildingDto> b1 = unitService.listForLinkUnitAllBuildings(id);
        model.put("unit", u);
        model.put("buildings",b1);
        return "link-unit-all-buildings";
    }

    @GetMapping("/link-unit-buildings")
    public String get(
            @RequestParam("id_building") long id_building,
            @RequestParam("id_unit") long id_unit ,
            ModelMap model) {
        unitService.connect(id_building, id_unit);
        return  get( id_unit, model);
    }

    @GetMapping("/remove-unit-all-buildings")
    public String getRemove(
            @RequestParam("id") long id,
            ModelMap model) {
        UnitDto u = unitService.getOne(id);
        List<BuildingDto> b1 = unitService.LinkUnitBuildingPage_GET_link_unit_all_buildings(id);
        model.put("unit", u);
        model.put("buildings",b1);
        return "link-unit-all-buildings";
    }

    @GetMapping("/remove-unit-buildings")
    public String remove(
            @RequestParam("id_unit") long id_unit ,
            @RequestParam("id_building") long id_building,
            ModelMap model) {
        unitService.LinkUnitBuildingPage_GET_remove_unit_buildings(id_unit, id_building);
        return getRemove( id_unit, model );
    }

    @GetMapping("/link-building-all-units")
    public String getb(@RequestParam("id") long id,
                       ModelMap model) {
        BuildingDto b = buildingService.getOneBuilding(id);
        List<UnitDto> u1 = unitService.LinkUnitBuildingPage_GET_link_building_all_units(id);
        model.put("building", b);
        model.put("units",u1);
        return "link-building-all-units";
    }

    @GetMapping("/link-building-units")
    public String getb(
            @RequestParam("id_unit") long id_unit ,
            @RequestParam("id_building") long id_building,
            ModelMap model) {
        unitService.LinkUnitBuildingPage_GET_link_building_units(id_unit, id_building);
        return  getb( id_building, model);
    }

    @GetMapping("/remove-building-all-units")
    public String getRemoveB(
            @RequestParam("id") long id,
            ModelMap model) {
        BuildingDto b = buildingService.getOneBuilding(id);
        List< UnitDto > u = unitService.LinkUnitBuildingPage_GET_remove_building_all_units(b.getId());
        model.put( "building", b );
        model.put( "units", u );
//ktory jsp wywolac
        return "remove-building-all-units";
    }

    @GetMapping("/remove-building-units")
    public String removeB(
            @RequestParam("id_building") long id_building,
            @RequestParam("id_unit") long id_unit ,
            ModelMap model) {
        unitService.LinkUnitBuildingPage_GET_remove_buildings_units(id_building, id_unit);
        return getRemoveB( id_building, model );
    }
}
