import React, {useEffect, useState} from 'react';
import "./App.css";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./pages/login/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import User from "./pages/user/User.js";
import Home from "./pages/home/Home.js";
import {getLoggedInUser} from "./apis/AuthRequest.js";


function App() {
    // const [user, setUser] = useState(null)
    // const [isUserLoggedIn, setIsUserLoggedIn] = useState(false);

    // useEffect(() => {
    //         getLoggedInUser().then(response => {
    //             if((user === null) || (response.data.username !== user.username)){
    //                 console.log("RESPONSE: " + response.data);
    //                 setUser(response.data)
    //                 if (user == null) {
    //                     console.log("NOT LOGGED IN")
    //                     setIsUserLoggedIn(false);
    //                 } else {
    //                     console.log("LOGGED IN: " + user.username)
    //                     setIsUserLoggedIn(true);
    //                 }
    //             }
    //         })
    //     }, [user]
    // )

    return (
        <Router>
            <Routes>
                <Route path="/" exact element={<Home/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/users/:username" element={<User/>}/>
            </Routes>
        </Router>
    );
}

export default App;
