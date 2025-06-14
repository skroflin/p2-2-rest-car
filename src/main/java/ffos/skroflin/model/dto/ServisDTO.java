/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
public record ServisDTO(
        @Schema(example = "Glavna i sigurna podrška svim mehaničarima o vozačima više od 35 godina") String opis,
        @Schema(example = "350.55") BigDecimal cijena,
        @Schema(example = "1") int voziloSifra
        ) {
    
}
