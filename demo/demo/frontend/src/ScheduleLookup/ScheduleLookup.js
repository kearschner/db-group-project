import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import {getCookie, setCookie, appendToCookie} from '../CookieHelper';
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

  return (
      <div>
          <div>
              <button onClick={handlePrevClick}>Prev</button>
              <p>Viewing Schedule {currentIndex + 1} of {schedules.length}</p>
              <button onClick={handleNextClick}>Next</button>
          </div>
          {schedules[currentIndex]}
      </div>
  );
}

export default ScheduleLookup;

