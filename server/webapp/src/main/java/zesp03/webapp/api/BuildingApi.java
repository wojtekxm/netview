/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.*;
import zesp03.webapp.dto.input.BuildingAndUnitDto;
import zesp03.webapp.dto.input.CreateBuildingDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.BuildingService;
import zesp03.webapp.service.DeviceService;

/**
 * BuildingApi jest klasą, która służy do zarządzania budynkami.
 */
@RestController
@RequestMapping("/api/building")
public class BuildingApi {
    @Autowired
    private BuildingService buildingService;

    @Autowired
    private DeviceService deviceService;

    /**
     * Pobieranie wszystkich budynków.
     * @return Zwraca listę wszystkich budynków.
     */
    @GetMapping("/info/all")
    public ListDto<BuildingDto> getAllBuildings() {
        return ListDto.make( () -> buildingService.getAllBuildings() );
    }

    /**
     * Pobieranie danych budynku, znając jego ID.
     * @param buildingId id budynku, którego dane zostaną otrzymane.
     * @return Zwraca dane budynku @see BuildingDto.
     */
    @GetMapping("/info/{buildingId}")
    public ContentDto<BuildingDto> getBuilding(
            @PathVariable("buildingId") long buildingId) {
        return ContentDto.make( () -> buildingService.getOneBuilding(buildingId) );
    }

    /**
     * Uzyskanie wszystkich powiązanych z budynkiem jednostek.
     * @param buildingId ID wybranego budynku, z którym są połączone jednostki.
     * @return Zwraca listę jednostek, wraz z ich danymi(@see UnitDto) , z którymi jest powiązany wybrany budynek.
     */
    @GetMapping("/units/{buildingId}")
    public ListDto<UnitDto> getUnits(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getUnits(buildingId) );
    }

    /**
     * Pobieranie wszystkich kontrolerów, które znajdują się w danym budynku.
     * @param buildingId  ID danego budynku, do którego są przypisane kontrolery.
     * @return Zwraca listę kontrolerów(@see ControllerDto), do których jest przypisany dany budynek.
     */
    @GetMapping("/controllers/{buildingId}")
    public ListDto<ControllerDto> getControllersInfo(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getControllersInfo(buildingId) );
    }

    /**
     * Pobieranie szczegółów kontrolerów, znajdujących się w wybranym budynku.
     * @param buildingId  ID wybranego budynku, dla któego są przypisane kontrolery.
     * @return Zwraca szczegółową listę kontrolerów, do których jest przypisany budynek - @see ControllerDetailsDto( posiada też ilość urządzeń)
     */
    @GetMapping("/controllers-details/{buildingId}")
    public ListDto<ControllerDetailsDto> getControllersDetails(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> buildingService.getControllersDetails(buildingId) );
    }

    /**
     * Pobieranie dla wybranego budynku urządzeń (przypisanych do niego).
     * @param buildingId ID wybranego budynku, dla którego są pobierane urządzenia.
     * @return Zwraca szczegółowa lista urządzeń, przypisanych do wybranego budynku - @see DeviceDetailsDto(posiadają takie pola jak : id, nazwa, kontroler do którego należą, budynek do którego są przypisane, częstotliwość)
     */
    @GetMapping("/devices-details/{buildingId}")
    public ListDto<DeviceDetailsDto> getDevices(
            @PathVariable("buildingId") long buildingId) {
        return ListDto.make( () -> deviceService.checkDetailsByBuilding(buildingId) );
    }

    /**
     * Usuwa jednostkę/budynek z listy powiązanych.
     * @param dto Tutaj są przechowywane id budynku i jednostki (powiązanych ze sobą).
     * @return Wynik operacji usuwania powiązań.
     */
    @PostMapping("/unlink-unit")
    public BaseResultDto unlinkUnit(
            @RequestBody BuildingAndUnitDto dto) {
        return BaseResultDto.make( () ->
                buildingService.unlinkUnit(
                        dto.getBuildingId(), dto.getUnitId()
                )
        );
    }

    /**
     *  Usuwa konkretny budynek.
     * @param buildingId ID budynku, który należy usunąć.
     * @return Wynik operacji usuwania.
     */
    @PostMapping("/remove/{buildingId}")
    public BaseResultDto removeBuilding(
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> buildingService.removeBuilding(buildingId) );
    }

    /**
     * Dodaje budynek.
     * @param dto Dane nowego budynku (*kod, *nazwa, adres, szer. dł. geograficzna ; * - to pola obowiązkowe) @see CreateBuildingDto.
     * @return Wynik operacji dodawania.
     */

    @PostMapping(value = "/create", consumes = "application/json")
    public BaseResultDto createBuilding(
            @RequestBody CreateBuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.createBuilding(dto) );
    }


    /**
     * Modyfikuje dane konkretnego budynku.
     * @param dto Nowe dane konkretnego budynku. ( *kod, *nazwa, adres, szer., dł. geograficzna ; * nie mogą być puste; ) @see BuildingDto
     * @return dane , które zostały wprowadzone są zmienione dla budynku o tym id
     */
    @PostMapping(value = "/accept-modify", consumes = "application/json")
    public BaseResultDto acceptModifyBuilding(
            @RequestBody BuildingDto dto) {
        return BaseResultDto.make( () -> buildingService.acceptModifyBuilding(dto) );
    }
}


