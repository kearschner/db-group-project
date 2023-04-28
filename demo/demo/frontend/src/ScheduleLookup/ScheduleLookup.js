import React, { useState, useEffect } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {getCookie, setCookie, appendToCookie, clearCookie} from '../CookieHelper';
import Schedule from "./schedule";

function ScheduleLookup() {

  function extractCrns(fromCookie) {
      let cookieString = getCookie(fromCookie);
      if (cookieString === '') return null;
      let values = cookieString.split(",");
      let crns = [];
      for (const value of values) {
          let crn = value.split(' ')[0];
          if (crn === "") continue;
          crns.push(crn);
      }
      return crns;
  }

  const locked = extractCrns("locked");
  const unlocked = extractCrns("unlocked");

  const [scheduleLookupData, setScheduleLookupData] = useState([]);

  useEffect(() => {
    fetch('/api/schedule-lookup', {
      method: 'POST',
      body: JSON.stringify({locked, unlocked}),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setScheduleLookupData(data);
      })
      .catch((error) => console.error(error));
  }, []);


  const schedules = scheduleLookupData.map( ((value, index) => (
      <Schedule index={index} components={value.components}/>
  )))

  const [currentIndex, setCurrentIndex] = useState(0);
  const handlePrevClick = () => {
      if (currentIndex > 0) {
          setCurrentIndex((currentIndex - 1));
      }
  };
  const handleNextClick = () => {
      if (currentIndex < schedules.length - 1) {
          setCurrentIndex(currentIndex + 1);
      }
  };

  const navigate = useNavigate();
  const handleReturnHome = (event) => {
      event.preventDefault();
      clearCookie("locked");
      clearCookie("unlocked");
      navigate("/schedule-lookup");
    }

  if (schedules.length === 0) {
      return (
          <div>
              <p>No schedules found with your course selections</p>
              <button onClick={handleReturnHome}>Return Home</button>
          </div>
      )
  }

  return (
      <div>
          <div>
              <table>
                  <tr>
                      <td><button onClick={handlePrevClick}>Prev</button></td>
                      <td>Viewing Schedule {currentIndex + 1} of {schedules.length}</td>
                      <td><button onClick={handleNextClick}>Next</button></td>
                  </tr>
              </table>
          </div>
          {schedules[currentIndex]}
          <button onClick={handleReturnHome}>Return Home</button>
      </div>
  );
}

export default ScheduleLookup;

