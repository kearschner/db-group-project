import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {getCookie, setCookie, appendToCookie} from './CookieHelper'

function SectionLookupResults() {
  const location = useLocation();
  const secFormData = location.state;

  const [sectionData, setSectionData] = useState([]);

  useEffect(() => {
    fetch('/api/section-lookup', {
      method: 'POST',
      body: JSON.stringify(secFormData),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setSectionData(data);
      })
      .catch((error) => console.error(error));
  }, [secFormData]);

  const handleAddSectionOnly = (row) => {
      // Implement functionality for "Add section only" button
      let currentLocked = getCookie("locked");
      if (currentLocked.includes(row.crn))
          return;

      appendToCookie("locked", [row.crn, row.subject, row.course_number].join(' '));
  };

  const handleAddCourse = (row) => {
      // Implement functionality for "Add course" button
      let currentUnlocked = getCookie("unlocked");
      if (currentUnlocked.includes(row.subject + ' ' + row.course_number))
          return;
      appendToCookie("unlocked", [row.crn, row.subject, row.course_number].join(' '));
  };

  const rows = [];

  sectionData.forEach((row, index) => {
    const isDuplicate =
      index > 0 && sectionData[index - 1].crn === row.crn;

    rows.push(
      <tr key={index}>
        <td>{isDuplicate ? '' : row.crn}</td>
        <td>{isDuplicate ? '' : row.subject}</td>
        <td>{isDuplicate ? '' : row.course_number}</td>
        <td>{isDuplicate ? '' : row.section_number}</td>
        <td>{isDuplicate ? '' : row.credits}</td>
        <td>{isDuplicate ? '' : row.title}</td>
        <td>{isDuplicate ? '' : row.instructional_method}</td>
        <td>{row.days}</td>
        <td>{row.times}</td>
        <td>{isDuplicate ? '' : row.instructor}</td>
        <td>{isDuplicate ? '' : row.campus}</td>
        <td>{isDuplicate ? '' : row.location}</td>
        <td>{isDuplicate ? '' : row.attribute_type}</td>
        <td>{isDuplicate ? '' :
            <div>
                <button onClick={() => handleAddSectionOnly(row)}>
                    Add section only
                </button>
                <button onClick={() => handleAddCourse(row)}>
                    Add course
                </button>
            </div>
        }
        </td>
      </tr>
    );
  });

  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>CRN</th>
            <th>Subj</th>
            <th>Crse</th>
            <th>Sec</th>
            <th>Cred</th>
            <th>Title</th>
            <th>Instructional Method</th>
            <th>Days</th>
            <th>Time</th>
            <th>Instructor</th>
            <th>Campus</th>
            <th>Location</th>
            <th>Attribute Type</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>{rows}</tbody>
      </table>
    </div>
  );
}

export default SectionLookupResults;

