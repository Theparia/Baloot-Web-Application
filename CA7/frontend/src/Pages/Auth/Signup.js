import React, {useState} from 'react';
import {signup} from "../../APIs/AuthRequest.js";
import "./Signup.css"
import Header from "../../Components/Header/Header.js";

const Signup = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [address, setAddress] = useState("");


    function handleSignup(e) {
        e.preventDefault();
        if(username && password && email && birthDate && address){
            if (!/^\w+$/.test(username)){
                alert("Invalid Username")
                return;
            }
            if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)){
                alert("Invalid Email")
                return;
            }
        }
        else{
            alert("Please fill the form!")
            return;
        }
        const user = {
            "username": username,
            "password": password,
            "email": email,
            "birthDate": birthDate,
            "address": address
        };
        signup(user)
            .then(response => {
                localStorage.setItem("userJWT" , response.data.jwtToken);
                localStorage.setItem("username" , username);
                window.location.replace("/");
            }).catch((error) => alert(error.response.data.errorMessage))
    }

    if (localStorage.getItem('username') !== null) {
        window.location.replace("/")
        return;
    }

    return (
        <>
            <Header username={null}/>
            <div className="form-container">
                <h2>Create Account</h2>
                <form action="signup.html" method="post" id="signup-form">
                    <label>
                        <input className="form-input" name="username" type="text" placeholder="Username"
                               onChange={(event) => setUsername(event.target.value)} required/>
                    </label>
                    <label>
                        <input className="form-input" name="password" type="password" placeholder="Password"
                               onChange={(event) => setPassword(event.target.value)} required/>
                    </label>
                    <label>
                        <input className="form-input" name="email" type="email" placeholder="Email"
                               onChange={(event) => setEmail(event.target.value)} required/>
                    </label>
                    <label>
                        <input className="form-input" name="birthDate" type="date" placeholder="Birth Date"
                               onChange={(event) => setBirthDate(event.target.value)} required/>
                    </label>
                    <label>
                        <input className="form-input" name="address" type="text" placeholder="Address"
                               onChange={(event) => setAddress(event.target.value)} required/>
                    </label>
                    <button type="submit" onClick={(e) => handleSignup(e)}>Create</button>
                </form>
                <label id="rtn-str">
                    <span>or </span><a href="/">Return to Store</a>
                </label>
            </div>
        </>
    );
}

export default Signup;
