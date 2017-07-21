/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zesp03.webapp.dto.ControllerDetailsDto;
import zesp03.webapp.dto.ControllerDto;
import zesp03.webapp.dto.DeviceDetailsDto;
import zesp03.webapp.dto.input.CreateControllerDto;
import zesp03.webapp.dto.result.BaseResultDto;
import zesp03.webapp.dto.result.ContentDto;
import zesp03.webapp.dto.result.ListDto;
import zesp03.webapp.service.ControllerService;
import zesp03.webapp.service.DeviceService;

/**
 * ControllerApi jest klasą, która służy do zarządzania kontrolerami.
 */
@RestController
@RequestMapping("/api/controller")
public class ControllerApi {

    private static final Logger log = LoggerFactory.getLogger(ControllerApi.class);

    @Autowired
    private ControllerService controllerService;

    @Autowired
    private DeviceService deviceService;

    /**
     * Pobieranie wszystkich kontrolerów.
     * @return Zwraca listę kontrolerów - @see ControllerDto.
     */
    @GetMapping("/info/all")
    public ListDto<ControllerDto> getAll() {
        return ListDto.make( () -> controllerService.getAll() );
    }

    /**
     * Pobieranie konkretnego kontrolera.
     * @param controllerId ID kontrolera, który ma zostać pobrany.
     * @return Zwraca kontroler o podanym ID - @see ControllerDto.
     */
    @GetMapping("/info/{controllerId}")
    public ContentDto<ControllerDto> getOne(
            @PathVariable("controllerId") long controllerId) {
        return ContentDto.make( () -> controllerService.getOne(controllerId) );
    }

    /**
     * Pobieranie szczegółów wszystkich kontrolerów.
     * @return Zwraca listę ze szczegółami kontrolerów - @see ControllerDetailsDto.
     */
    @GetMapping("/details/all")
    public ListDto<ControllerDetailsDto> getDetailsAll() {
        return ListDto.make( () -> controllerService.getDetailsAll() );
    }

    /**
     * Pobieranie szczegółów danego kontrolera.
     * @param controllerId ID kontrolera, którego szczegóły mają zostać pobrane.
     * @return Zwraca szczegóły kontrolera o podanym ID - @see ControllerDetailsDto.
     */
    @GetMapping("/details/{controllerId}")
    public ContentDto<ControllerDetailsDto> getDetailsOne(
            @PathVariable("controllerId") long controllerId) {
        return ContentDto.make( () -> controllerService.getDetailsOne(controllerId) );
    }

    /**
     * Pobieranie szczegółow wszystkich urządzeń danego kontrolera.
     * @param controllerId ID kontrolera
     * @return Zwraca listę szczegółów wszystkich urządzeń konkretnego kontrolera.
     */
    @GetMapping("/devices-details/{controllerId}")
    public ListDto<DeviceDetailsDto> getDevicesDetails(
            @PathVariable("controllerId") long controllerId) {
        return ListDto.make( () -> deviceService.checkDetailsByController(controllerId) );
    }

    /**
     * Usuwa konkretny kontroler.
     * @param id ID kontrolera, który należy usunąć.
     * @return Wynik operacji usuwania.
     */
    @PostMapping("/remove/{controllerId}")
    public BaseResultDto remove(
            @PathVariable("controllerId") long id) {
        return BaseResultDto.make( () -> controllerService.remove(id) );
    }

    /**
     * Dodaje kontroler.
     * @param dto Dane nowego kontrolera - @see CreateControllerDto .
     * @return Wynik operacji dodawania.
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public BaseResultDto create(
            @RequestBody CreateControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.create(dto) );
    }

    /**
     * Łączy konkretny kontroler z danym budynkiem.
     * @param controllerId ID kontrolera, który ma zostać połączony z danym budynkiem.
     * @param buildingId ID budynku, który ma zostać połączony z danym kontrolerem.
     * @return Wynik operacji łączenia konkretnego kontrolera z danym budynkiem.
     */
    @PostMapping("/link-building/{controllerId}/{buildingId}")
    public BaseResultDto linkBuilding(
            @PathVariable("controllerId") long controllerId,
            @PathVariable("buildingId") long buildingId) {
        return BaseResultDto.make( () -> controllerService.linkBuilding(controllerId, buildingId) );
    }

    /**
     * Usuwa powiązanie konkretnego kontrolera z budynkiem.
     * @param controllerId ID kontrolera, któremu należy usunąć powiązanie z budynkiem.
     * @return Wynik operacji usuwania połączenie konkretnego kontrolera z budynkiem.
     */
    @PostMapping("/unlink-building/{controllerId}")
    public BaseResultDto unlinkBuilding(
            @PathVariable("controllerId") long controllerId) {
        return BaseResultDto.make( () -> controllerService.unlinkBuilding(controllerId) );
    }

    /**
     * Modyfikuje dane konkretnego kontrolera.
     * @param dto Nowe dane konkretnego kontrolera - @see ControllerDto .
     * @return Wynik operacji modyfikacji.
     */
    @PostMapping(value = "/accept-modify-controller", consumes = "application/json")
    public BaseResultDto acceptModifyController(
            @RequestBody ControllerDto dto) {
        return BaseResultDto.make( () -> controllerService.acceptModifyController(dto) );
    }
}