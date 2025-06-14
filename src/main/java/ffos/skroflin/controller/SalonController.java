/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.controller;

import ffos.skroflin.model.Salon;
import ffos.skroflin.model.Vozilo;
import ffos.skroflin.model.dto.SalonDTO;
import ffos.skroflin.service.SalonService;
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
@Tag(name = "Skroflin -> Salon", description = "Sve dostupne rute koje se odnose na entitet Salon. Rute se odnose na CRUD metode - create (post), read (get), update (put) i delete.")
@RestController
@RequestMapping("/api/skroflin/salon")
public class SalonController {
    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }
    
    @Operation(
            summary = "Dohvaća sve salone iz tablice salon", tags = {"get", "salon"},
            description = "Dohvaća sve salone iz tablice salon, sa svojim podacima."
    )
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Salon.class)))),
                @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
            })
    @GetMapping("/get")
    public ResponseEntity get(){
        try {
            return new ResponseEntity<>(salonService.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Dohvaća salon po šifri",
            description = "Dohvaća salon po danoj šifri sa svim podacima. "
            + "Ukoliko ne postoji salon za danu šifru vraća prazan odgovor",
            tags = {"salon", "getBy"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ salona u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Salon.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Ne postoji salon za danu šifru", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
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
            
            Salon salon = salonService.getBySifra(sifra);
            if (salon == null) {
                return new ResponseEntity<>("Salon s navedenom šifrom" + " " + sifra + " " + "ne postoji!", HttpStatus.NO_CONTENT);
            }
            
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Kreira novi salon",
            tags = {"post", "salon"},
            description = "Kreira nov salon - novi unos u tablicu salon. Ime/naziv i lokacija salona obavezno!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kreirano", content = @Content(schema = @Schema(implementation = Salon.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljen dto objekt ili ne postoji naziv ili lokacija salona)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PostMapping("/post")
    public ResponseEntity post(
            @RequestBody(required = true) SalonDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci", HttpStatus.BAD_REQUEST);
                
            }
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv salona je obavezan!", HttpStatus.BAD_REQUEST);
            }
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija salona je obavezna!", HttpStatus.BAD_REQUEST);
            }
            
            return new ResponseEntity<>(salonService.post(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Mijenja podatke o salonu",
            tags = {"put", "salon"},
            description = "Mijenja podatke o salonu. Naziv i lokacija tvrtke salona!",
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
        @ApiResponse(responseCode = "400", description = "Loš zahtjev (nije primljena šifra dobra ili dto objekt ili ne postoji naziv ili lokacija salona)", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @PutMapping("/put")
    public ResponseEntity<String> put(
            @RequestParam int sifra,
            @RequestBody(required = true) SalonDTO dto
    ){
        try {
            if (dto == null) {
                return new ResponseEntity<>("Nisu uneseni traženi podaci!", HttpStatus.BAD_REQUEST);
            }
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0!" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Salon s = salonService.getBySifra(sifra);
            if (s == null) {
                return new ResponseEntity<>("Ne postoji Salon s navedenom šifrom" + " " + sifra + " " + "promjena nije moguća!", HttpStatus.BAD_REQUEST);
            }
            if (dto.naziv() == null || dto.naziv().isEmpty()) {
                return new ResponseEntity<>("Naziv salona je obavezan!", HttpStatus.BAD_REQUEST);
            }
            if (dto.lokacija() == null || dto.lokacija().isEmpty()) {
                return new ResponseEntity<>("Lokacija salona je obavezna!", HttpStatus.BAD_REQUEST);
            }
            
            salonService.put(sifra, dto);
            return new ResponseEntity<>("Promijenjen salon!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Briše salon po šifri",
            description = "Briše salon u kojoj se ne nalazi niti jedno vozilo. "
            + "Ukoliko postoji jedno ili više vozila unutar jednog salona vraća poruku o grešci!",
            tags = {"delete", "salon"},
            parameters = {
                @Parameter(
                        name = "sifra",
                        allowEmptyValue = false,
                        required = true,
                        description = "Primarni ključ salona u bazi podataka, mora biti veći od nula",
                        example = "1"
                )})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Obrisano", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "Šifra mora biti veća od nula ili student koji se želi brisati ne postoji "
                + "ili se ne može obrisati jer ima jedan ili više vozila unuta salona", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html")),
        @ApiResponse(responseCode = "500", description = "Interna pogreška servera", content = @Content(schema = @Schema(implementation = String.class), mediaType = "text/html"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam int sifra
    ){
        try {
            if (sifra <= 0) {
                return new ResponseEntity<>("Šifra mora biti veća od 0" + " " + sifra, HttpStatus.BAD_REQUEST);
            }
            
            Salon s = salonService.getBySifra(sifra);
            if (s == null) {
                return new ResponseEntity<>("Ne postoji salon s navedenom šifrom" + " " + sifra + " " +  ",nije moguće obrisati!", HttpStatus.BAD_REQUEST);
            }
            
            List<Vozilo> vozila = salonService.getVozila(sifra);
            if (vozila != null && !vozila.isEmpty()) {
                return new ResponseEntity<>("Salon se ne može obrisati jer ima jedan ili više automobila!", HttpStatus.BAD_REQUEST);
            }
            
            salonService.delete(sifra);
            return new ResponseEntity<>("Salon obrisan!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
