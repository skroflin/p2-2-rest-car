/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author svenk
 */
@Entity(name = "servis")
public class Servis extends Entitet{
    private String opis;
    private Date datum;
    @Column(columnDefinition = "float", nullable = false)
    private BigDecimal cijena;
    @ManyToOne
    private Vozilo vozilo;

    public Servis(String opis, Date datum, BigDecimal cijena, Vozilo vozilo) {
        this.opis = opis;
        this.datum = datum;
        this.cijena = cijena;
        this.vozilo = vozilo;
    }

    public Servis() {
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
    
    public void postaviAutomatskiDatum(){
        this.datum = new Date();
    }

    public BigDecimal getCijena() {
        return cijena;
    }

    public void setCijena(BigDecimal cijena) {
        this.cijena = cijena;
    }

    public Vozilo getVozilo() {
        return vozilo;
    }

    public void setVozilo(Vozilo vozilo) {
        this.vozilo = vozilo;
    }
    
    
}
