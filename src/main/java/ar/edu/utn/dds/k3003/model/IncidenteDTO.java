package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IncidenteDTO {

        public String tipoIncidente;
        public Integer heladeraId;
        public String fechaIncidente;

        @JsonCreator
        public IncidenteDTO(@JsonProperty("tipoIncidente") String tipoIncidente,  @JsonProperty("heladeraId") Integer heladeraId, @JsonProperty("fechaIncidente") String fechaIncidente) {
            this.tipoIncidente = tipoIncidente;
            this.heladeraId = heladeraId;
            this.fechaIncidente = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public Integer getHeladeraId() {
        return heladeraId;
    }

    public String getFechaIncidente() {
        return fechaIncidente;
    }
}
