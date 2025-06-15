/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Salon;
import ffos.skroflin.model.Vozilo;
import ffos.skroflin.model.dto.VoziloDTO;
import ffos.skroflin.service.VoziloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author svenk
 */
@Tag(name = "Skroflin -> Vozilo", description = "Sve dostupne rute koje se odnose na entitet Vozilo. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/vozilo")
public class VoziloController {
    private final VoziloService voziloService;

    public VoziloController(VoziloService voziloService) {
        this.voziloService = voziloService;
    }
    
    @Operation(
            summary = "Dohvaća sva vozila", tags = {"get", "vozilo"},
            description = "Dohvaća sva vozila sa svim podacima"
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Vozilo.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(voziloService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća vozilo po šifri",
            description = "Dohvaća vozilo po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji vozilo za danu šifru vraća prazan odgovor",
            tags = {"vozilo", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ vozila u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Vozilo.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji vozilo za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Vozilo v = voziloService.getBySifra(sifra);
            if (v == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(v, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novo vozilo",
            tags = {"post", "vozilo"},
            description = "Kreira novo vozilo. Model, marka i cijena vozila obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Vozilo.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji model ili marka ili cijena)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) VoziloDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci", HttpStatus.BAD_REQUEST);
            }
            if (dto.model() == null || dto.model().isEmpty()) {
                return new ResponseEntity<>("Model je obavezan!" + dto.model(), HttpStatus.BAD_REQUEST);
            }
            if (dto.marka() == null || dto.marka().isEmpty()) {
                return new ResponseEntity<>("Marka je obavezna!" + dto.marka(), HttpStatus.BAD_REQUEST);
            }
            if (dto.cijena() == null) {
                return new ResponseEntity<>("Cijena je obavezna!" + dto.cijena(), HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(voziloService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja podatke o vozilu",
            tags = {"put", "vozilo"},
            description = "Mijenja podatke o vozilu. Model, marka i cijena vozila obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ salona u bazi podataka, mora biti veći od nula!",
                        example = "1"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji model ili marka ili cijena vozila)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity<String> put(
            @RequestParam int sifra,
            @RequestBody(required = true) VoziloDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci", HttpStatus.BAD_REQUEST);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Vozilo v = voziloService.getBySifra(sifra);
            if (v == null) {
                return new ResponseEntity<>("Ne postoji vozilo s navedenom šifrom" + " " + sifra + " ", HttpStatus.BAD_REQUEST);
            }
            
            if (dto.model() == null || dto.model().isEmpty()) {
                return new ResponseEntity<>("Model je obavezan!" + dto.model(), HttpStatus.BAD_REQUEST);
            }
            if (dto.marka() == null || dto.marka().isEmpty()) {
                return new ResponseEntity<>("Marka je obavezna!" + dto.marka(), HttpStatus.BAD_REQUEST);
            }
            if (dto.cijena() == null) {
                return new ResponseEntity<>("Cijena je obavezna!" + dto.cijena(), HttpStatus.BAD_REQUEST);
            }
            
            voziloService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjeno", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše vozilo po šifri",
            description = "Briše vozilo koji nema ni jedan servis. "
            + "Ukoliko postoji jedan ili više servisa na kojima je postavljeno zadano vozilo vraća poruku o grešci",
            tags = {"delete", "vozilo"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ vozila u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili vozilo koji se želi brisati ne postoji "
                + "ili se ne može obrisati jer ima jedan ili više servisa postavljeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Vozilo v = voziloService.getBySifra(sifra);
            if (v == null) {
                return new ResponseEntity<>("Ne postoji vozilo s navedenom šifrom" + " " + sifra + " " + ", nije moguće obrisati!", HttpStatus.BAD_REQUEST);
            }
            
            if (!voziloService.isBrisanje(sifra)) {
                return new ResponseEntity<>("Vozilo se ne može obrisati jer ima 1 ili više servisa!", HttpStatus.BAD_REQUEST);
            }
            
            voziloService.delete(sifra);
            return new ResponseEntity<>("Obrisano vozilo!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
