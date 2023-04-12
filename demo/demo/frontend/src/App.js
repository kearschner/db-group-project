
import './App.css';
import SubjectSelect from './SubjectSelect';
import { useState, useEffect } from 'react';

function App() {
    const [instructors, setInstructors] = useState([]);

    useEffect(() => {
        async function fetchInstructors() {
            const response = await fetch('/api/instructors');
            const data = await response.json();
            setInstructors(data);
        }
        fetchInstructors();
    }, []);
    return (
        <div className="App">
            <header className="App-header">
                <div className="form-container">
                    <form>
                        <SubjectSelect />
                        <div className="form-group">
                            <label htmlFor="course-number">Course Number:</label>
                            <input type="text" id="course-number" />
                        </div>
                        <div className="form-group">
                            <label htmlFor="title">Title:</label>
                            <input type="text" id="title" />
                        </div>
                        <div className="form-group">
                            <label htmlFor="schedule-type">Schedule Type:</label>
                            <select id="schedule-type">
                                <option value="">Select an option</option>
                                <option value="lecture">Lecture</option>
                                <option value="lab">Lab</option>
                                <option value="seminar">Seminar</option>
                                <option value="online">Online</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="instructional-method">Instructional Method:</label>
                            <select id="instructional-method">
                                <option value="">Select an option</option>
                                <option value="in-person">In Person</option>
                                <option value="hybrid">Hybrid</option>
                                <option value="online">Online</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="credit-range">Credit Range:</label>
                            <input type="number" id="credit-range-min" placeholder="hours" />
                            <label htmlFor="credit-range-max" className="credit-range-label">to</label>
                            <input type="number" id="credit-range-max" placeholder="hours" />
                        </div>
                        <div className="form-group">
                            <label htmlFor="department">Department:</label>
                            <select
                                id="department"
                                name="department"
                                size="4"
                                style={{ overflowY: 'scroll' }}
                            >
                                <option value="All">All</option>
                                <option value="A&S - General">A&S - General</option>
                                <option value="Academic Computing">Academic Computing</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="campus">Campus:</label>
                            <select
                                id="campus"
                                name="campus"
                                size="4"
                                style={{ overflowY: 'scroll', width: '250px', height: '100px' }}
                            >
                                <option value="All">North Campus</option>
                                <option value="Off-Campus Special Programs">Off-Campus Special Programs</option>
                                <option value="Off-Campus - Sarasota-Manatee">Off-Campus - Sarasota-Manatee</option>
                                <option value="Off-Campus - St.Petersburg">Off-Campus - St.Petersburg</option>
                                <option value="Off-Campus - Tampa">Off-Campus - Tampa</option>
                                <option value="Sarasota-Manatee">Sarasota-Manatee</option>
                                <option value="St.Petersburg">St.Petersburg</option>
                                <option value="Tampa">Tampa</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="part-of-term">Part of Term:</label>
                            <select
                                id="part-of-term"
                                name="part-of-term"
                                size="4"
                                style={{ overflowY: 'scroll', width: '250px', height: '75px' }}
                                className="select-part-of-term"
                            >
                                <option value="All">All</option>
                                <option value="Alternative Calendar Term">Alternative Calendar Term</option>
                                <option value="Extended Alternative Calendar Term">Extended Alternative Calendar Term</option>
                                <option value="Full Term">Full Term</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="instructor">Instructor:</label>
                            <select
                                id="instructor"
                                name="instructor"
                                size="4"
                                style={{ overflowY: 'scroll', width: '200px', height: '50px' }}
                                className="select-instructor"
                            >
                                {instructors.map((instructor, index) => (
                                    <option key={index} value={instructor.name}>{instructor.name}</option>
                                ))}
                            </select>
                        </div>


                        <div className="form-group">
                            <label htmlFor="attribute-type">Attribute Type:</label>
                            <select
                                id="attribute-type"
                                name="attribute-type"
                                size="4"
                                style={{ overflowY: 'scroll', width: '200px', height: '50px' }}
                                className="select-attribute-type"
                            >
                                <option value="GenEd">General Education</option>
                                <option value="Writing">Writing Intensive</option>
                                <option value="Quant">Quantitative Reasoning</option>
                                <option value="Human">Humanities</option>
                                <option value="Social">Social Sciences</option>
                                <option value="Natural">Natural Sciences</option>
                                <option value="Language">Language and Culture</option>
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="open-section-only" style={{ width: '180px', display: 'inline-block' }}>
                                Limit to Open Section ONLY:
                            </label>
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <input
                                    type="checkbox"
                                    id="open-section-only"
                                    name="open-section-only"
                                    style={{ marginRight: '5px', marginLeft: '25px' }}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label htmlFor="start-time" style={{ width: '200px', display: 'inline-block' }}>Start Time:</label>
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <div style={{ display: 'inline-flex', alignItems: 'center', marginRight: '15px' }}>
                                    <label htmlFor="start-time-hour" style={{ fontSize: '12px', marginRight: '-170px', marginLeft: '74px'  }}>Hour:</label>
                                    <select id="start-time-hour" style={{ width: '50px' }}>
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
                                    <select id="start-time-minute" style={{ width: '50px' }}>
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
                                    <select id="start-time-am-pm" style={{ width: '50px' }}>
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
                                    <select id="end-time-hour" style={{ width: '50px' }}>
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
                                    <select id="end-time-minute" style={{ width: '50px' }}>
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
                                    <select id="end-time-am-pm" style={{ width: '50px' }}>
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
                                            style={{ marginRight: '-50px' }} // Adjust the marginRight value to 0.5cm
                                        />
                                        <label htmlFor={`day-${index}`} style={{ fontSize: '12px' }}>{day}</label>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <div className="form-group">
                            <div style={{ display: 'inline-flex', alignItems: 'center' }}>
                                <button type="button" id="section-search" style={{ marginRight: '10px' }}>Section Search</button>
                                <button type="reset">Reset</button>
                            </div>
                        </div>




                    </form>
                </div>
            </header>
        </div>
    );
}

export default App;
