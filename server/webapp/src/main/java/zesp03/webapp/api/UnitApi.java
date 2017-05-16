package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.BuildingDto;
import zesp03.webapp.dto.LinkUnitBuildingDto;
import zesp03.webapp.dto.UnitBuildingsDto;
import zesp03.webapp.dto.UnitDto;
import zesp03.webapp.dto.input.BuildingAndUnitDto;
import zesp03.webapp.dto.input.CreateUnitDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.UnitService;

/**
 * UnitApi jest klasą, która służy do zarządzania jednostkami(jednostkami organizacyjnymi).
 */
@RestController
public class UnitApi {
    @Autowired
    private UnitService unitService;

    /**
     * Pobieranie wszystkich jednostek.
     * @return Zwraca listę jednostek zawierająca (id, kod, nazwa(description) jednostki).
     */
    @GetMapping("/api/unit/all")
    public ListDto<UnitDto> getAll() {
        return ListDto.make( () -> unitService.getAll() );
    }

    /**
     * Pobieranie danych jednostki, znając jej ID.
     * @param id ID jednostki, której dane zostaną otrzymane.
     * @return Zwraca dane jednostki (id, kod, nazwa(description) ).
     */
    @GetMapping("/api/unit")
    public ContentDto<UnitBuildingsDto> getUnitBuildings(
            @RequestParam("id") long id ) {
        return ContentDto.make( () -> unitService.getUnitBuildings(id) );
    }

    /**
     * Pobranie połączonej jednostki z połączonym budynkiem
     * @param id ID, przechowujące połączoną jednostkę i połączony budynek @see LinkUnitBuildingDto.
     * @return Zwraca połączoną jednostkę z budynkiem, które są przypisane do danego ID.
     */

    @GetMapping("/api/link-unit-building")
    public ContentDto<LinkUnitBuildingDto> getLinkUnitBuilding(
            @RequestParam("id") long id) {
        return ContentDto.make( () -> unitService.getLinkUnitBuilding(id) );
    }

    /**
     * Pobranie wszystkich połączonych ze sobą jednostek i budynków.
     * @return Zwraca listę połączonych ze sobą jednostek i budynków.
     */
    @GetMapping("/api/all-link-units-buildings")
    public ListDto<LinkUnitBuildingDto> getAllLinkUnitsBuildings() {
        return ListDto.make( () -> unitService.getAllLinkUnitBuildings() );
    }

    /**
     * Dodaje jednostkę.
     * @param dto Dane nowej jednostki - @see CreateUnitDto.
     * @return Wynik operacji dodawania.
     */
    @PostMapping(value = "/api/unit/create", consumes = "application/json")
    public BaseResultDto create(
            @RequestBody CreateUnitDto dto) {
        return BaseResultDto.make( () -> unitService.create(dto) );
    }

    /**
     * Usuwa konkretną jednostkę.
     * @param id ID jednostki, którą należy usunąć.
     * @return Wynik operacji usuwania.
     */

    @PostMapping(value = "/api/unit/remove/{unitId}")
    public BaseResultDto removeUnit(
            @PathVariable("unitId") long id) {
        return BaseResultDto.make( () -> unitService.removeUnit(id) );
    }

    /**
     * Pobieranie szczegółów danej jednostki.
     * @param unitId ID jednostki, której szczegóły mają zostać pobrane.
     * @return Zwraca szczegóły jednostki, o podanym ID @see UnitBuildingsDto .
     */
    @GetMapping("/api/unit/details/{unitId}")
    public ContentDto<UnitBuildingsDto> getDetailsOne(
            @PathVariable("unitId") long unitId) {
        return ContentDto.make( () -> unitService.getDetailsOne(unitId) );
    }

    /**
     * Pobieranie wszystkich budynków połączonych z daną jednostką.
     * @param unitId ID jednostki, dla której mają zostać pobrane budynki.
     * @return Zwraca listę połączonych z jednostką budynków.Wraz z ich danymi (@see BuildingDto)
     */
    @GetMapping("/api/unit/buildings/{unitId}")
    public ListDto<BuildingDto> getBuilding(
            @PathVariable("unitId") long unitId) {
        return ListDto.make( () -> unitService.getBuildings(unitId) );
    }

    /**
     * Modyfikuje dane konkretnej jednostki.
     * @param dto Nowe dane konkretnej jednostki. @see UnitDto .
     * @return Wynik operacji modyfikacji.
     */

    @PostMapping(value = "/api/accept-modify-unit", consumes = "application/json")
    public BaseResultDto acceptModifyUnit(
            @RequestBody UnitDto dto) {
        return BaseResultDto.make( () -> unitService.acceptModifyUnit(dto) );
    }

    /**
     * Pobieranie wszystkich niepołączonych z jednostką budynków.
     * @param id ID jednostki, dla której ma zostać pobrana lista budynków niepowiązanych z nią.
     * @return Lista budynków niepowiązanych z jednostką( wraz z ich danymi).
     */

    @GetMapping("/api/link-unit-all-buildings")
    public ContentDto<UnitBuildingsDto> getAllBuildings(
            @RequestParam("id") long id ) {
        return ContentDto.make( () -> unitService.linkUnitAllBuildings(id) );
    }
}
