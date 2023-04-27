import React, {useEffect, useState} from 'react';
import "./App.css";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./pages/login/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import User from "./pages/user/User.js";
import {getLoggedInUser} from "./apis/AuthRequest.js";


function App() {
    const [user, setUser] = useState({})
    const [isUserLoggedIn, setIsUserLoggedIn] = useState(false);

    useEffect(() => {
            getLoggedInUser().then(response => {
                setUser(response.data)
                if (user == null) {
                    setIsUserLoggedIn(false);
                } else {
                    setIsUserLoggedIn(true);
                }
            })
        }
    )
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="/users/:username" element={<User/>}/>
            </Routes>
        </Router>
    );
}

export default App;
