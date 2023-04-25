
import './SectionLookup.css';
import LookupSelect from './LookupSelect';
import useSimpleGet from "./useSimpleGet";

import { useState} from 'react';
import {useNavigate} from "react-router-dom";


function SectionLookup() {

    const [secFormData, setSecFormData] = useState({});

    const instructors = useSimpleGet('/api/instructors');
    const departments = useSimpleGet('/api/departments');
    const subjects = useSimpleGet('/api/subjects');
    const campuses = useSimpleGet('/api/campuses');
    const instructionalMethods = useSimpleGet('/api/instructional-methods');
    const attributes = useSimpleGet('/api/attributes');

    let navigate = useNavigate();

    function handleSectionLookupSubmit(event) {
        event.preventDefault();
        navigate('/sec-lookup-results', {state: secFormData});
    }

    function handleSecFormChange(event) {
        const {name, value} = event.target;
        setSecFormData((prevData) => ({
            ...prevData , [name]: value
        }));
    }

    function handleSecFormChangeBox(event) {
        const {name, checked} = event.target;
        setSecFormData((prevData) => ({
            ...prevData , [name]: checked
        }));
    }

    function handleSecFormChangeSelection(event) {
        const {name, options} = event.target;
        const selectedValues = [];
        for (let i = 0; i < options.length; i++) {
            if (options[i].selected) {
                selectedValues.push(options[i].value);
            }
        }
        setSecFormData((prevData) => ({
            ...prevData , [name]: selectedValues
        }));
    }

    function handleResetSecForm() {
        setSecFormData(() => ({
        }));
    }

    return (
        <div className="App">
            <header className="App-header">
                <div className="form-container">
                    <form onSubmit={handleSectionLookupSubmit}>
                        <LookupSelect contents={subjects} id="subject" label="Subject" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <div className="form-group">
                            <label htmlFor="course-number">Course Number:</label>
                            <input type="text" id="course-number" name="course-number" onChange={handleSecFormChange}/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="title">Title:</label>
                            <input type="text" id="title" name="title" onChange={handleSecFormChange}/>
                        </div>
                        <LookupSelect contents={instructionalMethods} id="instructional-method" label="Instructional Method" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <div className="form-group">
                            <label htmlFor="credit-range">Credit Range:</label>
                            <input type="number" id="credit-range-min" name="credit-range-min" placeholder="hours" onChange={handleSecFormChange}/>
                            <label htmlFor="credit-range-max" className="credit-range-label">to</label>
                            <input type="number" id="credit-range-max" name="credit-range-max" placeholder="hours" onChange={handleSecFormChange}/>
                        </div>
                        <LookupSelect contents={departments} id="department" label="Department" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <LookupSelect contents={campuses} id="campus" label="Campus" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <LookupSelect contents={instructors} id="instructor" label="Instructor" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <LookupSelect contents={attributes} id="attributes" label="Attributes" size={4} changeHandler={handleSecFormChangeSelection}/>
                        <div className="form-group">
                            <label htmlFor="start-time" style={{ width: '200px', display: 'inline-block' }}>Start Time:</label>
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <div style={{ display: 'inline-flex', alignItems: 'center', marginRight: '15px' }}>
                                    <label htmlFor="start-time-hour" style={{ fontSize: '12px', marginRight: '-170px', marginLeft: '74px'  }}>Hour:</label>
                                    <select id="start-time-hour" name="start-time-hour" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="12">00</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                        <option value="03">03</option>
                                        <option value="04">04</option>
                                        <option value="05">05</option>
                                        <option value="06">06</option>
                                        <option value="07">07</option>
                                        <option value="08">08</option>
                                        <option value="09">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="11">12</option>

                                    </select>
                                </div>
                                <div style={{ display: 'inline-flex', alignItems: 'center', marginRight: '15px' }}>
                                    <label htmlFor="start-time-minute" style={{ fontSize: '12px', marginRight: '-155px', marginLeft: '150px' }}>Minute:</label>
                                    <select id="start-time-minute" name="start-time-minute" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="00">00</option>
                                        <option value="05">05</option>
                                        <option value="10">10</option>
                                        <option value="15">15</option>
                                        <option value="20">20</option>
                                        <option value="25">25</option>
                                        <option value="30">30</option>
                                        <option value="35">35</option>
                                        <option value="40">40</option>
                                        <option value="45">45</option>
                                        <option value="50">50</option>
                                        <option value="55">55</option>

                                    </select>
                                </div>
                                <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                    <label htmlFor="start-time-am-pm" style={{ fontSize: '12px', marginRight: '-155px' , marginLeft: '150px' }}>AM/PM:</label>
                                    <select id="start-time-am-pm" name="start-time-am-pm" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="am">AM</option>
                                        <option value="pm">PM</option>

                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="end-time" style={{ width: '200px', display: 'inline-block' }}>End Time:</label>
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <div style={{ display: 'inline-flex', alignItems: 'center', marginRight: '15px' }}>
                                    <label htmlFor="end-time-hour" style={{ fontSize: '12px', marginRight: '-170px', marginLeft: '74px' }}>Hour:</label>
                                    <select id="end-time-hour" name="end-time-hour" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="12">00</option>
                                        <option value="01">01</option>
                                        <option value="02">02</option>
                                        <option value="03">03</option>
                                        <option value="04">04</option>
                                        <option value="05">05</option>
                                        <option value="06">06</option>
                                        <option value="07">07</option>
                                        <option value="08">08</option>
                                        <option value="09">09</option>
                                        <option value="10">10</option>
                                        <option value="11">11</option>
                                        <option value="11">12</option>

                                    </select>
                                </div>
                                <div style={{ display: 'inline-flex', alignItems: 'center', marginRight: '15px' }}>
                                    <label htmlFor="end-time-minute" style={{ fontSize: '12px', marginRight: '-155px', marginLeft: '150px' }}>Minute:</label>
                                    <select id="end-time-minute" name="end-time-minute" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="00">00</option>
                                        <option value="05">05</option>
                                        <option value="10">10</option>
                                        <option value="15">15</option>
                                        <option value="20">20</option>
                                        <option value="25">25</option>
                                        <option value="30">30</option>
                                        <option value="35">35</option>
                                        <option value="40">40</option>
                                        <option value="45">45</option>
                                        <option value="50">50</option>
                                        <option value="55">55</option>


                                    </select>
                                </div>
                                <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                    <label htmlFor="end-time-am-pm" style={{ fontSize: '12px', marginRight: '-155px', marginLeft: '150px' }}>AM/PM:</label>
                                    <select id="end-time-am-pm" name="end-time-am-pm" onChange={handleSecFormChange} style={{ width: '50px' }}>
                                        <option value="">--</option>
                                        <option value="am">AM</option>
                                        <option value="pm">PM</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="days" style={{ width: '200px', display: 'inline-block' }}>Days:</label>
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((day, index) => (
                                    <div key={index} style={{ marginRight: '-100px', display: 'inline-flex', alignItems: 'center' }}>
                                        <input
                                            type="checkbox"
                                            id={`day-${index}`}
                                            name={`day-${index}`}
                                            onChange={handleSecFormChangeBox}
                                            style={{ marginRight: '-50px' }} // Adjust the marginRight value to 0.5cm
                                        />
                                        <label htmlFor={`day-${index}`} style={{ fontSize: '12px' }}>{day}</label>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <div className="form-group">
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <button type="submit" id="section-search" style={{ marginRight: '10px' }}>Section Search</button>
                                <button type="reset" onClick={handleResetSecForm}>Reset</button>
                            </div>
                        </div>




                    </form>
                </div>
            </header>
        </div>
    );
}

export default SectionLookup;
