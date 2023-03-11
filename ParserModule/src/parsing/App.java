package parsing;

import data.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeFilter;


class EmptyTextFilter implements NodeFilter {
    public NodeFilter.FilterResult head(Node node, int depth) {

        if (node instanceof TextNode && ((TextNode) node).isBlank())
            return NodeFilter.FilterResult.REMOVE; // Removes any text nodes that are blank

        return NodeFilter.FilterResult.CONTINUE; // and keeps everything else.
    }
}


enum RowType {
    TITLE,
    HEADER,
    CONTENT;

    static RowType fromNode(Node rowNode) throws IllegalArgumentException {

        Node firstColNode = rowNode.firstChild();
        if (firstColNode == null) {
            throw new IllegalArgumentException("Row node contains no columns");
        }

        String columnType = firstColNode.attr("class");
        switch (columnType) {
            case "ddtitle":
                return RowType.TITLE;
            case "ddheader":
                return RowType.HEADER;
            case "dddefault":
                return RowType.CONTENT;
            default:
                throw new IllegalArgumentException("Passed node does not contain valid columns or is not a row.");
        }
    }
}

public class App {

    // Recursively extract the text of a node and its children
    public static String extractString(Node node) {
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }

        String childrenResult = "";

        for (Node childNode : node.childNodes()) {
            childrenResult += extractString(childNode);
        }
        
        return childrenResult;
    }

    public static String getDepartment(Node rowNode) {
        // We're going to assume that if this is called rowNode is title row (contains a nested TextNode with a department)
        String dirtyTitle = extractString(rowNode); // Title from node that may contain suffix: "***Continued***"

        int contIndex = dirtyTitle.indexOf("***");
        if (contIndex == -1) // If dirtyTitle doesn't have the *** suffix we can just return it
            return dirtyTitle;

        return dirtyTitle.substring(0, contIndex - 1); // otherwise we cut out the suffix and return
    }

    public static LocalTime parseTimeFromMorningAfternoonRep(String str) {

        if (str.contains("N/A") || str.contains("TBA"))
            return null;

        String[] timeParts = str.trim().split(" ");

        LocalTime time = LocalTime.parse(timeParts[0]);

        if (timeParts[1] == "PM") {
            return time.plusHours(12);
        }

        return time;
    }

    public static boolean isAdditionalMeetingRow(Node row) {
        if (row.childNodeSize() < 7) {
            return false;
        }

        String titleString = extractString(row.childNode(6));

        if (titleString.length() <= 1)
            return true;

        return false;
    }

    public static void populateMeetings(List<Meeting> meetings, Node meetingRow) {

        EnumSet<Day> days = Day.setFromString(extractString(meetingRow.childNode(10)));

        int spanOffset = days.isEmpty() && !extractString(meetingRow.childNode(11)).contains("N/A") ? 1 : 0;

        String[] timeStrings = extractString(meetingRow.childNode(11 - spanOffset)).split("-");
        LocalTime startTime = parseTimeFromMorningAfternoonRep(timeStrings[0]);

        LocalTime endTime = (startTime == null) ? null : parseTimeFromMorningAfternoonRep(timeStrings[1]);

        String campus = extractString(meetingRow.childNode(17 + spanOffset));
        String[] locationStrings = extractString(meetingRow.childNode(18 + spanOffset)).trim().split(" ");

        Building building;
        Location meetingLoc;

        if (locationStrings[0].contains("TBA")) {
            building = null;
            meetingLoc = null;
        }
        else {
            building = new Building("", locationStrings[0]);
            meetingLoc = new Location(campus, building, locationStrings[1]);
        }

        
        meetings.add(new Meeting(startTime, endTime, days, meetingLoc));

        Node nextRow = meetingRow.nextSibling();
        if (nextRow != null && isAdditionalMeetingRow(nextRow))
            populateMeetings(meetings, nextRow);


    }

    public static int extractLowerCredits(String credString) {

        int firstDecimalIndex = credString.indexOf(".");
        if (firstDecimalIndex != -1) {
            credString = credString.substring(0, firstDecimalIndex);
        }

        return Integer.parseInt(credString);
    }

    public static LocalDate parseDateWithArbitraryYear(String dateString) {
        String[] monthDay = dateString.split("/");
        return LocalDate.of(2023, Integer.parseInt(monthDay[0]), Integer.parseInt(monthDay[1]));
    }

    record InstructorsParseResult(Instructor primary, Instructor[] all) {}
    public static InstructorsParseResult parseInstructors(String[] instructorStrings) {
    
        Instructor primary = null;
        Instructor[] allIns = new Instructor[instructorStrings.length];
        for (int i = 0; i < allIns.length; i++) {
            Instructor currIns;
            int primaryIndicator = instructorStrings[i].indexOf("(P)");
            if (primaryIndicator >= 0) {
                currIns = new Instructor(
                    instructorStrings[i].substring(0, primaryIndicator).trim()
                );
                primary = currIns;
            } else {
                currIns = new Instructor(
                    instructorStrings[i].trim()
                );
            }
            allIns[i] = currIns;
        }
        
        return new InstructorsParseResult(primary, allIns);
    }

    record AttributesParseResult(FundingAttribute funding, CourseAttribute[] all) {}
    public static AttributesParseResult parseAttributes(String[] attributeStrings) {
    
        FundingAttribute funding = null;
        CourseAttribute[] allAtrib = new CourseAttribute[attributeStrings.length];
        for (int i = 0; i < allAtrib.length; i++) {
            CourseAttribute currAtrib;
            int fundingIndicator = attributeStrings[i].indexOf("Funding:");
            if (fundingIndicator >= 0) {
                funding = new FundingAttribute(attributeStrings[i].trim());
                currAtrib = funding;
            } else {
                currAtrib = new CourseAttribute(attributeStrings[i].trim());
            }
            allAtrib[i] = currAtrib;
        }
        
        return new AttributesParseResult(funding, allAtrib);
    }

    public static Section getSection(Node rowNode, String department) {

        String crn = extractString(rowNode.childNode(1));
        Subject subject = new Subject(extractString(rowNode.childNode(2)), "");
        String courseNumber = extractString(rowNode.childNode(3));
        String sectionNumber = extractString(rowNode.childNode(4));
        int credits = extractLowerCredits(extractString(rowNode.childNode(5)));
        String courseTitle = extractString(rowNode.childNode(6));
        InstructionalMethod method = InstructionalMethod.fromString(extractString(rowNode.childNode(7)));
        boolean permitRequired = extractString(rowNode.childNode((8))) != "";

        String[] dateStrings = extractString(rowNode.childNode(9)).split("-");
        LocalDate startDate = parseDateWithArbitraryYear(dateStrings[0]);
        LocalDate endDate = parseDateWithArbitraryYear(dateStrings[1]);

        List<Meeting> meetings = new ArrayList<>();
        populateMeetings(meetings, rowNode);

        int spanOffset = (meetings.get(0).days().isEmpty()) && !extractString(rowNode.childNode(11)).contains("N/A") ? 1 : 0;

        int seatCap = Integer.parseInt(extractString(rowNode.childNode(12 + spanOffset)));
        int seatAvail = Integer.parseInt(extractString(rowNode.childNode(13 + spanOffset)));
        int waitCap = Integer.parseInt(extractString(rowNode.childNode(14 + spanOffset)));
        int waitAvail = Integer.parseInt(extractString(rowNode.childNode(15 + spanOffset)));

        InstructorsParseResult instructorsResults = parseInstructors(extractString(rowNode.childNode(16 + spanOffset)).split(","));

        AttributesParseResult attributesResults = parseAttributes(extractString(rowNode.childNode(19 + spanOffset)).split("and"));

        Course course = new Course(courseTitle, courseNumber, subject, credits, department, permitRequired, attributesResults.funding(), attributesResults.all());


        return new Section(course, crn, sectionNumber, startDate, endDate, waitAvail, waitCap, seatAvail, seatCap, method, instructorsResults.primary(), instructorsResults.all(), meetings);






    }



    public static Document createHTMLDocFromFile(String filePath) throws IOException{

        File input = new File(filePath);
        
        return Jsoup.parse(input, "UTF-8", "");
    }

    public static Node getCleanTableBody(Document fromDoc) {

        Element parentTable = fromDoc.select("table[summary='This layout table is used to present the sections found']").first();

        parentTable.filter(new EmptyTextFilter()); // Clears out any empty text nodes that have been picked up.

        return parentTable.childNode(1); // Gets the table body node from the "table".
    }

    public static List<Section> parseSectionsFromDocument(Document sectionDoc) {


        List<Section> sections = new ArrayList<>();

        Node tableBody = getCleanTableBody(sectionDoc);
        

        String activeDepartment = "";

        for (int i = 0; i < tableBody.childNodeSize(); i++) {
            Node rowNode = tableBody.childNode(i);
            RowType rowType = RowType.fromNode(rowNode);

            switch (rowType) {
                case TITLE:
                    activeDepartment = getDepartment(rowNode);
                    break;
                case CONTENT:
                    Section sec = getSection(rowNode, activeDepartment);
                    sections.add(sec);
                    i += (sec.meets().size() - 1); // Skip rows that have been processed for meetings
                    break;
                case HEADER: // Header rows are all identical, no information needs processing
                    break;
                default:
                    break;
            }

        }

        return sections;
    }

    public static void main(String[] args) throws Exception {


        Document doc = createHTMLDocFromFile("Look Up Classes.htm");

        List<Section> sections = parseSectionsFromDocument(doc);

        System.out.println(sections.size());

    }
}
