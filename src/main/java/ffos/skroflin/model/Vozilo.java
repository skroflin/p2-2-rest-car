/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 *
 * @author svenk
 */
@Entity(name = "vozilo")
public class Vozilo extends Entitet{
    private String marka;
    private String model;
    @Column(name = "godina_proizvodnje")
    private int godinaProizvodnje;
    @Column(columnDefinition = "float", nullable = false)
    private BigDecimal cijena;
    @ManyToOne
    private Salon salon;

    public Vozilo(String marka, String model, int godinaProizvodnje, BigDecimal cijena, Salon salon) {
        this.marka = marka;
        this.model = model;
        this.godinaProizvodnje = godinaProizvodnje;
        this.cijena = cijena;
        this.salon = salon;
    }

    public Vozilo() {
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getGodinaProizvodnje() {
        return godinaProizvodnje;
    }

    public void setGodinaProizvodnje(int godinaProizvodnje) {
        this.godinaProizvodnje = godinaProizvodnje;
    }

    public BigDecimal getCijena() {
        return cijena;
    }

    public void setCijena(BigDecimal cijena) {
        this.cijena = cijena;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }
    
    
}
