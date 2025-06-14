/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author svenk
 */
public record SalonDTO(
        @Schema(example = "Tokić d.o.o.") String naziv,
        @Schema(example = "Ulica Ivana Huttlera 31") String lokacija
        ) {
    
}
