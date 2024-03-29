import React, {useEffect, useState} from 'react';
import "./Login.css"
import Header from "../../Components/Header/Header.js";
import {login} from "../../APIs/AuthRequest.js";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleLogin(e) {
        e.preventDefault();
        const user = {"username": username, "password": password};
        login(user)
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
                <h2 id="login-title">LOGIN</h2>
                <form action="" method="post" id="login-form">
                    <label>
                        <input className="form-input" name="username" type="text" placeholder="Username"
                               onChange={(event) => setUsername(event.target.value)} required/>
                    </label>
                    <label>
                        <input className="form-input" name="password" type="password" placeholder="Password"
                               onChange={(event) => setPassword(event.target.value)} required/>
                    </label>
                    <button type="submit" onClick={(e) => handleLogin(e)}>Login</button>
                    <div id="github">
                        <img id="github-icon" src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" alt="GitHub icon"/>
                        <a id="github-link" href="https://github.com/login/oauth/authorize?client_id=bf0229f4067042e56a4b&scope=user">
                            Login via Github
                        </a>
                    </div>
                </form>
            </div>
        </>
    );
}

export default Login;
