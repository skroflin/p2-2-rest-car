/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 *
 * @author svenk
 */
public record VoziloDTO(
        @Schema(example = "Volkswagen") String marka,
        @Schema(example = "Golf 7") String model,
        @Schema(example = "2018") int godinaProizvodnje,
        @Schema(example = "31000,55") BigDecimal cijena,
        @Schema(example = "1") int salonSifra
        ) {
    
}
