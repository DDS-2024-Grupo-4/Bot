package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

public class RetiroDTODay {
	
    private String qrVianda;
    private String tarjeta;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime fechaRetiro;
    private Integer heladeraId;

    // Constructor
    @JsonCreator
    public RetiroDTODay(@JsonProperty("qrVianda") String qrVianda, @JsonProperty("tarjeta") String tarjeta, @JsonProperty("fechaRetiro") LocalDateTime fechaRetiro, @JsonProperty("heladeraId") Integer heladeraId) {
      this.qrVianda = qrVianda;
      this.tarjeta = tarjeta;
      this.fechaRetiro = fechaRetiro;
      this.heladeraId = heladeraId;
    }

    // Getters y Setters
    public String getQrVianda() {
      return qrVianda;
    }

    public void setQrVianda(String qrVianda) {
      this.qrVianda = qrVianda;
    }

    public String getTarjeta() {
      return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
      this.tarjeta = tarjeta;
    }

    public LocalDateTime getFechaRetiro() {
      return fechaRetiro;
    }

    public void setFechaRetiro(LocalDateTime fechaRetiro) {
      this.fechaRetiro = fechaRetiro;
    }

    public Integer getHeladeraId() {
      return heladeraId;
    }

    public void setHeladeraId(Integer heladeraId) {
      this.heladeraId = heladeraId;
    }

    // ToString
    @Override
    public String toString() {
      return "RetiroDTODay{" +
          "qrVianda='" + qrVianda + '\'' +
          ", tarjeta='" + tarjeta + '\'' +
          ", fechaRetiro=" + fechaRetiro +
          ", heladeraId=" + heladeraId +
          '}';
    }
}