/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.Servis;
import ffos.skroflin.model.Vozilo;
import ffos.skroflin.model.dto.ServisDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class ServisService extends GlavniService{
    public List<Servis> getAll(){
        return session.createQuery("from servis", Servis.class).list();
    }
    
    public Servis getBySifra(int sifra){
        return session.get(Servis.class, sifra);
    }
    
    public Servis post(ServisDTO o){
        session.beginTransaction();
        Vozilo vozilo = session.get(Vozilo.class, o.voziloSifra());
        Servis servis = new Servis();
        servis.setOpis(o.opis());
        servis.setCijena(o.cijena());
        servis.setVozilo(vozilo);
        session.persist(servis);
        session.getTransaction().commit();
        return servis;
    }
    
    public void put(int sifra, ServisDTO o){
        session.beginTransaction();
        Servis s = (Servis) session.get(Servis.class, sifra);
        Vozilo vozilo = session.get(Vozilo.class, o.voziloSifra());
        s.setOpis(o.opis());
        s.setCijena(o.cijena());
        s.setVozilo(vozilo);
        session.persist(s);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Servis.class, sifra));
        session.getTransaction().commit();
    }
}
