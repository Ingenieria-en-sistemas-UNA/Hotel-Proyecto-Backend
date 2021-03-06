package app.controller;

import app.dto.FilterDate;
import app.entity.Room;
import app.exeption.EntityNotFoundException;
import app.service.FileStorageService;
import app.service.RoomService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/room")
@Api(tags = "Room")
public class RoomController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RoomService roomService;

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Crea una habitación", response = ResponseEntity.class, notes = "Retorna la habitación añadida")
    @ResponseBody
    public ResponseEntity<Room> save(
            @ApiParam(value = "Un objeto Habitacion tipo Json", required = true) @Valid @RequestPart("file") MultipartFile file,
            @RequestPart Room room)
            throws DataIntegrityViolationException, IOException, EntityNotFoundException {

        Room roomSave = roomService.save(room);
        String fileName = fileStorageService.storeFile(file, "room", roomSave.getId());
        roomSave.setImg(fileName);
        Room roomResponse = roomService.update(roomSave.getId(), roomSave);

        return ResponseEntity.ok().body(roomResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Busca una Habitación", response = Room.class, notes = "Retorna una habitación por ID")
    public ResponseEntity<Room> get(@ApiParam(value = "El ID de la habitación a buscar", required = true) @PathVariable("id") int id)
            throws EntityNotFoundException {

        Room room = roomService.get(id);
        return ResponseEntity.ok().body(room);

    }

    @PostMapping("/list")
    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "Busca todas personas", response = List.class, notes = "Retorna una lista de objetos Room")
    @ResponseBody
    public ResponseEntity<List<Room>> list(@RequestParam String filter,
                                           @RequestBody FilterDate filterDate) {

        List<Room> rooms = roomService.list(filter, filterDate);
        return ResponseEntity.ok().body(rooms);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Actualiza una habitación", response = ResponseEntity.class, notes = "Restorna la habitación actualizada")
    @ApiResponses({
            @ApiResponse(code = 500, message = "The room does not exist")})
    public ResponseEntity<Room> update(@ApiParam(value = "El ID de la habitación a actualizar", required = true) @PathVariable("id") int id,
                                       @ApiParam(value = "Un objeto Room tipo Json", required = true) @Valid @RequestPart("file") MultipartFile file,
                                       @RequestPart Room room)
            throws EntityNotFoundException {

        String fileName = fileStorageService.storeFile(file, "room", id);
        room.setImg(fileName);
        Room roomResponse = roomService.update(id, room);
        return ResponseEntity.ok().body(roomResponse);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Elimina una Habitación", response = ResponseEntity.class, notes = "Retorna una respuesta OK")
    @ApiResponses({
            @ApiResponse(code = 500, message = "The Room does not exist")})
    public ResponseEntity<List<Integer>> delete(
            @ApiParam(value = "El ID de la persona a eliminar", required = true) @RequestBody List<Integer> idRooms)
            throws EntityNotFoundException {

        roomService.delete(idRooms);
        return ResponseEntity.ok().body(idRooms);
    }
}
