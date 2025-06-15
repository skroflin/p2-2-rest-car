/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Servis;
import ffos.skroflin.model.dto.ServisDTO;
import ffos.skroflin.service.ServisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Skroflin -> Servis", description = "Sve dostupne rute koje se odnose na entitet Vozilo. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/servis")
public class ServisController {
    private final ServisService servisService;

    public ServisController(ServisService servisService) {
        this.servisService = servisService;
    }
    
    @Operation(
            summary = "Dohvaća sve servise", tags = {"get", "servis"},
            description = "Dohvaća sve servise sa svim svojim pripadajućim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Servis.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(servisService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća servis po šifri",
            description = "Dohvaća servis danoj šifri sa svim svojim pripadajućim podacima. "
            + "Ukoliko ne postoji servis za danu šifru vraća prazan odgovor",
            tags = {"servis", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ servisa u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Servis.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji servis za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @GetMapping("/getBySifra")
    public ResponseEntity getBySifra(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule:" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Servis s = servisService.getBySifra(sifra);
            if (s == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novi servis",
            tags = {"post", "servis"},
            description = "Kreira novi servis. Opis i cijena servisa je obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Servis.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji opis ili cijena)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) ServisDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto + " ", HttpStatus.BAD_REQUEST);
            }
            if (dto.opis() == null || dto.opis().isEmpty()) {
                return new ResponseEntity<>("Opis je obavezan" + " " + dto.opis() + " ", HttpStatus.BAD_REQUEST);
            }
            if (dto.cijena() == null) {
                return new ResponseEntity<>("Cijena je obavezna!" + " " + dto.cijena() + " ", HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(servisService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja servis",
            tags = {"put", "servis"},
            description = "Mijenja podatke servisa. Opis i cijena servisa je obavezno!",
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ servisa u bazi podataka, mora biti veći od nula",
                        example = "1"
                )
            }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Promjenjeno", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji opis ili cijena servisa.)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity put(
            @RequestParam int sifra,
            @RequestBody(required = true) ServisDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Podaci nisu primljeni" + " " + dto, HttpStatus.BAD_REQUEST);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Servis s = servisService.getBySifra(sifra);
            if (s == null) {
                return new ResponseEntity<>("Ne postoji servis s navedenom šifrom" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            if (dto.opis() == null || dto.opis().isEmpty()) {
                return new ResponseEntity<>("Opis je obavezan" + " " + dto.opis() + " ", HttpStatus.BAD_REQUEST);
            }
            if (dto.cijena() == null) {
                return new ResponseEntity<>("Cijena je obavezna!" + " " + dto.cijena() + " ", HttpStatus.BAD_REQUEST);
            }
            
            servisService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjeno", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše servise po šifri",
            description = "Briše servis i sve njegove pripadajuće podatke sa sobom. ",
            tags = {"delete", "djelatnik"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ servisa u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obrisano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili djelatnik koji se želi brisati ne postoji ", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od nule" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Servis s = servisService.getBySifra(sifra);
            if (s == null) {
                return new ResponseEntity<>("Ne postoji servis s navedenom šifrom" +  " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            servisService.delete(sifra);
            return new ResponseEntity<>("Obrisan servis", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
