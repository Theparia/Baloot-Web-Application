import React, {useEffect, useState} from 'react';
import "./App.css";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./Pages/Auth/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import User from "./Pages/User/User.js";
import Home from "./Pages/Home/Home.js";
import Signup from "./Pages/Auth/Signup.js";
import Product from "./Pages/Product/Product.js";
import Provider from "./Pages/Provider/Provider.js";
import Callback from "./Pages/Callback/Callback.js";



function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" exact element={<Home/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/users/:username" element={<User/>}/>
                <Route path="/commodities/:commodityId" element={<Product/>}/>
                <Route path="/providers/:providerId" element={<Provider/>}/>
                <Route path="/callback" element={<Callback />}/>
            </Routes>
        </Router>
    );
}

export default App;
