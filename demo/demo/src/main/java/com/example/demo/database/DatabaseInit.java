package com.example.demo.database;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class DatabaseInit {

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;
    private final SectionInitService sectionInitService;

    public DatabaseInit(DataSource dataSource, ResourceLoader resourceLoader, SectionInitService sectionInitService) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
        this.sectionInitService = sectionInitService;
    }

    @PostConstruct
    public void initStoredProcedures() throws SQLException {
        Resource script = resourceLoader.getResource("classpath:sql/sectionLookupFunctions.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new EncodedResource(script, "UTF-8"), false, false, ScriptUtils.DEFAULT_COMMENT_PREFIX, ";;",
                ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER, ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER);
    }

    public void initDatabase() throws IOException, SQLException {
        initStoredProcedures();
        sectionInitService.populateFromOasis();
    }

}
