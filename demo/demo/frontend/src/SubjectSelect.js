import React from 'react';
import { useState, useEffect } from 'react';

const SubjectSelect = () => {


    const [subjects, setSubjects] = useState([]);

    useEffect(() => {
        async function fetchSubjects() {
            const response = await fetch('/api/subjects');
            const data = await response.json();
            setSubjects(data);
        }
        fetchSubjects();
    }, []);

    const handleSubjectChange = (event) => {
        console.log('Selected subject:', event.target.value);
    };

    const handleCourseNumberChange = (event) => {
        console.log('Course number:', event.target.value);
    };

    const handleTitleChange = (event) => {
        console.log('Title:', event.target.value);
    };

    return (
        <div>
            <div className="form-group">
                <label htmlFor="subject">Subject:</label>
                <select
                    id="subject"
                    name="subject"
                    onChange={handleSubjectChange}
                    size="4"
                    style={{ overflowY: 'scroll', width: '200px', height: '100px' }}
                >
                    {subjects.map((subject, index) => (
                        <option key={index} value={subject}>
                            {subject}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
};

export default SubjectSelect;
