import React, { useState } from 'react';
import './Cart.css';

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

export default function App() {
    const [courses, setCourses] = useState([
        { crn: "12345", number: "CS101" },
        { crn: "67890", number: "MATH201" },
        { crn: "32146", number: "COP3540" },
    ]);

    const [courses2, setCourses2] = useState([
        { crn: "98765", number: "ENGL101" },
        { crn: "56789", number: "BIO202" },
        { crn: "65432", number: "HIST150" },
    ]);

    const clearCart = (cartIndex) => {
        if (cartIndex === 1) {
            setCourses([]);
        } else if (cartIndex === 2) {
            setCourses2([]);
        }
    };

    const checkout = () => {
        console.log("Proceeding to checkout...");
    };

    return (
        <div className="App">
            <div className="cart-container">
                <Cart
                    title="Locked CRNS"
                    onClear={() => clearCart(1)}
                >
                    {courses.map(course => (
                        <Course
                            key={course.crn}
                            crn={course.crn}
                            number={course.number}
                        />
                    ))}
                </Cart>
                <Cart
                    title="Unlocked CRNS"
                    onClear={() => clearCart(2)}
                >
                    {courses2.map(course => (
                        <Course
                            key={course.crn}
                            crn={course.crn}
                            number={course.number}
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
