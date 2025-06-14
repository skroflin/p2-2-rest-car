/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ffos.skroflin.service;

import ffos.skroflin.model.Salon;
import ffos.skroflin.model.Vozilo;
import ffos.skroflin.model.dto.SalonDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author svenk
 */
@Service
public class SalonService extends GlavniService{
    public List<Salon> getAll(){
        return session.createQuery("from salon", Salon.class).list();
    }
    
    public Salon getBySifra(int sifra){
        return session.get(Salon.class, sifra);
    }
    
    public List<Vozilo> getVozila(int sifra){
        return session.createQuery("from vozilo v where o.salon.sifra =:sifra", Vozilo.class)
                .setParameter("sifra", sifra)
                .list();
    }
    
    public Salon post(SalonDTO o){
        Salon salon = new Salon(o.naziv(), o.lokacija());
        session.beginTransaction();
        session.persist(salon);
        session.getTransaction().commit();
        return salon;
    }
    
    public void put(int sifra, SalonDTO o){
        session.beginTransaction();
        Salon s = (Salon) session.get(Salon.class, sifra);
        s.setNaziv(o.naziv());
        s.setLokacija(o.lokacija());
        session.persist(s);
        session.getTransaction().commit();
    }
    
    public void delete(int sifra){
        session.beginTransaction();
        session.remove(session.get(Salon.class, sifra));
        session.getTransaction().commit();
    }
}
