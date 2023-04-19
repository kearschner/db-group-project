package com.example.demo.database;

import com.example.demo.data.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom{

    @Autowired
    private EntityManager entityManager;

    @Override
    public Location insertLocationSafe(Location location) {
        Query ins = entityManager.createNativeQuery("INSERT INTO Location (campus, building, room_number) VALUES (?1,?2,?3) ON CONFLICT DO NOTHING RETURNING *",
                Location.class);
        ins.setParameter(1, location.campus());
        ins.setParameter(2,location.building());
        ins.setParameter(3, location.roomNumber());
        var results = ins.getResultList();

        if (!results.isEmpty())
            return (Location) results.get(0);

        Query oldLookup = entityManager.createNativeQuery("SELECT * from Location WHERE campus = ?1 AND building = ?2 AND room_number = ?3 LIMIT 1");
        oldLookup.setParameter(1, location.campus());
        oldLookup.setParameter(2,location.building());
        oldLookup.setParameter(3, location.roomNumber());
        var result = (Object[]) oldLookup.getSingleResult();
        return new Location((Long) result[0], (String) result[1], (String) result[2], (String) result[3]);
    }

}
