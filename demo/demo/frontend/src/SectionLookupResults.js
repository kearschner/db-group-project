import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function SectionLookupResults() {
    const location = useLocation();
    const secFormData = location.state;

    const [sectionData, setSectionData] = useState([]);
    useEffect(() => {
        fetch('/api/section-lookup', {
            method: 'POST',
            body: JSON.stringify(secFormData),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                setSectionData(data);
                console.log(data);
            })
            .catch(error => console.error(error))
    }, [secFormData])

    // Event handlers for button clicks
    const handleAddSectionOnly = (section) => {
        // Implement functionality for "Add section only" button
        console.log('Add section only button clicked', section);
    };

    const handleAddCourse = (section) => {
        // Implement functionality for "Add course" button
        console.log('Add course button clicked', section);
    };

    const renderTableHeader = () => (
        <tr>
            <th>CRN</th>
            <th>Course</th>
            <th>Title</th>
            <th>Instructor</th>
            <th>Day</th>
            <th>Time</th>
            <th>Location</th>
            <th>Seats Available</th>
            <th>Actions</th>
        </tr>
    );

    const renderTableRows = () =>
        sectionData.map((section, index) => (
            <tr key={index}>
                <td>{section.crn}</td>
                <td>{section.course}</td>
                <td>{section.title}</td>
                <td>{section.instructor}</td>
                <td>{section.day}</td>
                <td>{section.time}</td>
                <td>{section.location}</td>
                <td>{section.seatsAvailable}</td>
                <td>
                    <button onClick={() => handleAddSectionOnly(section)}>Add section only</button>
                    <button onClick={() => handleAddCourse(section)}>Add course</button>
                </td>
            </tr>
        ));

    return (
        <div>
            <p>Lookup results:</p>
            <table>
                <thead>{renderTableHeader()}</thead>
                <tbody>{renderTableRows()}</tbody>
            </table>
        </div>
    );
}

export default SectionLookupResults;
