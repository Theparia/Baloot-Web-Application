import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/login/Login.js";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from "react-toastify";


function App() {
  return (
      <Router>
          <Routes>
              <Route path="/login" element={<Login/>} />
          </Routes>
      </Router>
  );
}

export default App;
