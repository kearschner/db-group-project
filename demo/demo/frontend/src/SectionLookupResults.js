import React, {useState, useEffect} from 'react';
import {useLocation} from 'react-router-dom'

function SectionLookupResults() {

    const location = useLocation();
    const secFormData = location.state;

    const [sectionData, setSectionData] = useState({});
    useEffect( () => {
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


    return (
        <div>
            <p>Lookup results: {JSON.stringify(sectionData)}</p>
        </div>
    )
}

export default SectionLookupResults;