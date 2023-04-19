package com.example.demo.database;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class DatabaseInit {

    private final SectionInitService sectionInitService;

    public DatabaseInit(SectionInitService sectionInitService) {
        this.sectionInitService = sectionInitService;
    }

    public void initDatabase() throws IOException {
        sectionInitService.populateFromOasis();
    }

}
