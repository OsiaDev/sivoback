package com.coljuegos.sivo.service.notificacion;

public class Scratch {
    private enum ApuestaCodigoEnum {
        MET(new String[]{"1", "2", "3"}),
        MESAS(new String[]{"4"}),
        BINGO(new String[]{"6", "7", "8", "9", "10", "11", "12", "13"}),
        OTROS(new String[]{"5", "14"});

        private final String[] codigos;

        ApuestaCodigoEnum(String[] codigos) {
            this.codigos = codigos;
        }

        public static ApuestaCodigoEnum fromCodigo(String codigo) {
            if (codigo == null) return MET;
            for (ApuestaCodigoEnum e : values()) {
                for (String c : e.codigos) {
                    if (c.equals(codigo)) return e;
                }
            }
            return MET;
        }
    }

    private static class Counters {
        int registrados = 0;
        int apagados = 0;
        int noEncontrados = 0;
        int sinPlaca = 0;
        int serialDiferente = 0;
        int codigoApuestaDiferente = 0;
        int novedadesApagadas = 0;
        int novedadesOperando = 0;
    }
}
