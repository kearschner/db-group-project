import React, { useState } from 'react';
import './Cart.css';
import {getCookie, setCookie, appendToCookie, clearCookie} from './CookieHelper'
import {useNavigate} from "react-router-dom";

function Cart({ title, children, onClear }) {
    return (
        <div className="cart">
            <h2>{title}</h2>
            {children}
            <button className="cart-button" onClick={onClear}>Clear Cart</button>
        </div>
    );
}

function Course({ crn, number }) {
    return (
        <div className="course">
            <div><strong>CRN:</strong> {crn}</div>
            <div><strong>Course Number:</strong> {number}</div>
        </div>
    );
}

function extractCourses(fromCookie) {
    let cookieString = getCookie(fromCookie);
    let values = cookieString.split(",");
    let crns = [];
    if (cookieString === '') return crns;
    for (const value of values) {
        let crn = value.split(' ')[0];
        let cour = value.split(' ')[2]
        if (crn === "" || cour === "") continue;
        crns.push(crn + " " + cour)
    }
    return crns;
}

export default function App() {

    let courses = extractCourses("unlocked");
    let courses2 = extractCourses("locked");

    const clearCart = (cartIndex) => {
        if (cartIndex === 1) {
            courses = [];
            clearCookie("unlocked");
        } else if (cartIndex === 2) {
            courses2 = [];
            clearCookie("locked");
        }
    };

    const navigate = useNavigate();
    const checkout = () => {
        navigate("/schedule-lookup")
    };

    return (
        <div className="App">
            <div className="cart-container">
                <Cart
                    title="Locked CRNS"
                    onClear={() => clearCart(1)}
                >
                    {courses.map((val,index) => (
                        <Course
                            key={val.split(" ")[0]}
                            crn={val.split(" ")[0]}
                            number={val.split(" ")[1]}
                        />
                    ))}
                </Cart>
                <Cart
                    title="Unlocked CRNS"
                    onClear={() => clearCart(2)}
                >
                    {courses2.map((val,index) => (
                        <Course
                            key={val.split(" ")[0]}
                            crn={val.split(" ")[0]}
                            number={val.split(" ")[1]}
                        />
                    ))}
                </Cart>
            </div>
            <div className="checkout-button-container">
                <button className="checkout-button" onClick={checkout}>Checkout</button>
            </div>
        </div>
    );
}
