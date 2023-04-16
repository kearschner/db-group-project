package com.example.demo.database;

import com.example.demo.data.Section;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

import static com.example.demo.parsing.OasisParser.createHTMLDocFromFile;
import static com.example.demo.parsing.OasisParser.parseSectionsFromDocument;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @PostConstruct
    public void populateFromOasis() throws IOException {
        Document doc = createHTMLDocFromFile("Look Up Classes.htm");
        List<Section> sections = parseSectionsFromDocument(doc);
        sectionRepository.saveAll(sections);
    }
}
