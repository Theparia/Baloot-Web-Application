import React, { useState } from 'react';
import {getLoggedInUser} from "../../apis/loginRequest.js";
import "./Login.css"

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    // const [user, setUser] = useState({});


    function handleLogin() {
        console.log("handle loginnnn");
        const user = { "username" :  username, "password" :  password};
        console.log(user);
        getLoggedInUser(user)
            .then(response => {
            // if(response.status === 200) {
                console.log(response.data.username);
                console.log(response.data.password);
                // window.location.replace("/")
            // }
            // else {
            //     alert("نام کاربری یا رمز عبور اشتباه است. دوباره تلاش کنید!")
            // }

        })
    }
    //
    // const eee = {
    //     "username" :  "amir",
    //     "password" :  "1234"
    // }
    // //
    // getLoggedInUser(eee)
    //     .then(response => {
    //         console.log(response.data.username);
    //         setUser(response.data);
    //     })
    //     .catch(error => console.error(error));


    // return (
    //     <div>
    //         usernameeee: {user.username}
    //     </div>
    // );

    return (
        <div className="form-container">
        <h2>LOGIN</h2>
        <form action="" method="post" id="login-form">
            <label>
                <input className="form-input" name="username" type="text" placeholder="Username"
                       onChange={(event) => setUsername(event.target.value)} required/>
            </label>
            <label>
                <input className="form-input" name="password" type="password" placeholder="Password"
                       onChange={(event) => setPassword(event.target.value)} required/>
            </label>
            <button type="submit" onClick={(e) => handleLogin(e)}>Register</button>
        </form>
    </div>
    );
}

export default Login;
