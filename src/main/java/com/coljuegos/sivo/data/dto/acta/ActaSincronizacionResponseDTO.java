package com.coljuegos.sivo.data.dto.acta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActaSincronizacionResponseDTO {

    private Boolean success;
    private String message;
    private Integer numActa;

    public static ActaSincronizacionResponseDTO success(Integer numActa, String message) {
        return new ActaSincronizacionResponseDTO(true, message, numActa);
    }

    public static ActaSincronizacionResponseDTO error(String message) {
        return new ActaSincronizacionResponseDTO(false, message, null);
    }

}