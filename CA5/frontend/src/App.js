import React, {useEffect, useState} from 'react';
import "./App.css";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./pages/auth/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import User from "./pages/user/User.js";
import Home from "./pages/home/Home.js";
import Signup from "./pages/auth/Signup.js";


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" exact element={<Home/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/users/:username" element={<User/>}/>
            </Routes>
        </Router>
    );
}

export default App;
