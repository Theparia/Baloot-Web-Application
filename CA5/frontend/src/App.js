import React, {useEffect, useState} from 'react';
import "./App.css";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./pages/auth/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import User from "./pages/user/User.js";
import Home from "./pages/home/Home.js";
import Signup from "./pages/auth/Signup.js";
import Product from "./pages/Product/Product.js";
import Provider from "./pages/provider/Provider.js";
import {fetchBuyList} from "./components/utils.js";


function App() {
    const [buyList, setBuyList] = useState([]);

    useEffect(() => {
        // setBuyList(fetchBuyList());
        fetchBuyList().then(response => {
            setBuyList(response);
            console.log("IN USE EFFECT " + response);
        }).catch(error=>console.log("FFFF"))
    }, []);


    return (
        <Router>
            <Routes>
                <Route path="/" exact element={<Home buyList={buyList} setBuyList={setBuyList}/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/users/:username" element={<User/>}/>
                <Route path="/commodities/:commodityId" element={<Product/>}/>
                <Route path="/providers/:providerId" element={<Provider/>}/>
            </Routes>
        </Router>
    );
}

export default App;
