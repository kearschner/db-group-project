package com.example.demo.database;

import com.example.demo.data.Location;
import com.example.demo.data.Meeting;
import com.example.demo.data.Section;
import com.example.demo.database.repositories.LocationRepository;
import com.example.demo.database.repositories.MeetingRepository;
import com.example.demo.database.repositories.SectionRepository;
import jakarta.transaction.Transactional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.demo.parsing.OasisParser.createHTMLDocFromFile;
import static com.example.demo.parsing.OasisParser.parseSectionsFromDocument;

@Transactional
@Service
public class SectionInitService {

    private final SectionRepository sectionRepository;
    private final LocationRepository locationRepository;
    private final MeetingRepository meetingRepository;

    public SectionInitService(SectionRepository sectionRepository, LocationRepository locationRepository, MeetingRepository meetingRepository) {
        this.sectionRepository = sectionRepository;
        this.locationRepository = locationRepository;
        this.meetingRepository = meetingRepository;
    }

    public void populateFromOasis() throws IOException {

        sectionRepository.deleteAll();

        Document doc = createHTMLDocFromFile("Look Up Classes.htm");
        List<Section> sections = parseSectionsFromDocument(doc);

        for (Section sec : sections) {
            Set<Meeting> identifiedMeetings = new HashSet<>();
            for (Meeting meet : sec.meets()) {
                Location loc = meet.location();
                if (loc == null) continue;
                meet.setLocation(locationRepository.insertLocationSafe(loc));

                identifiedMeetings.add(meetingRepository.insertMeetingSafe(meet));
            }
            sec.setMeets(identifiedMeetings);
        }

        sectionRepository.saveAll(sections);
    }
}
