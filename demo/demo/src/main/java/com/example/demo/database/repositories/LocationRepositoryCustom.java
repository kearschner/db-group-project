package com.example.demo.database.repositories;

import com.example.demo.data.Location;

public interface LocationRepositoryCustom {

    public Location insertLocationSafe(Location location);
}
