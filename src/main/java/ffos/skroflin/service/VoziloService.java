/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.Salon;
import ffos.skroflin.model.Servis;
import ffos.skroflin.model.Vozilo;
import ffos.skroflin.model.dto.VoziloDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class VoziloService extends GlavniService{
    public List<Vozilo> getAll(){
        return session.createQuery("from vozilo", Vozilo.class).list();
    }
    
    public Vozilo getBySifra(int sifra){
        return session.get(Vozilo.class, sifra);
    }
    
    public Vozilo post(VoziloDTO o){
        session.beginTransaction();
        Salon salon = session.get(Salon.class, o.salonSifra());
        Vozilo vozilo = new Vozilo();
        vozilo.setMarka(o.marka());
        vozilo.setModel(o.model());
        vozilo.setGodinaProizvodnje(o.godinaProizvodnje());
        vozilo.setCijena(o.cijena());
        vozilo.setSalon(salon);
        session.persist(vozilo);
        session.getTransaction().commit();
        return vozilo;
    }
    
    public void put(int sifra, VoziloDTO o){
        session.beginTransaction();
        Vozilo v = (Vozilo) session.get(Vozilo.class, sifra);
        Salon salon = session.get(Salon.class, o.salonSifra());
        v.setMarka(o.marka());
        v.setModel(o.model());
        v.setGodinaProizvodnje(o.godinaProizvodnje());
        v.setCijena(o.cijena());
        v.setSalon(salon);
        session.persist(v);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Vozilo.class, sifra));
        session.getTransaction().commit();
    }
    
    public boolean isBrisanje(int sifra){
        List<Servis> servisi = session.createQuery("from servis s join s.vozilo.sifra=:sifra", Servis.class)
                .setParameter("sifra", sifra)
                .list();
        
        return servisi == null || servisi.isEmpty();
    }
}
