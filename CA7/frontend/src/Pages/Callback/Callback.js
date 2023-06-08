import React, { useEffect, useState } from 'react';
import {
    callback
} from "../../APIs/AuthRequest.js";

function Callback() {
    let search = window.location.search;
    let params = new URLSearchParams(search.split('?')[1]);

    useEffect(() => {
        if(localStorage.getItem("userJWT") != null){
            window.location.replace("/");
        }
        const req = {"code": params.get('code')};
        console.log("--->" + params.get('code'))
        callback(req).then((response) => {
            let userJWT = response.data.jwtToken;
            let username = response.data.username;
            localStorage.setItem("userJWT" , userJWT);
            localStorage.setItem("username" , username);
            window.location.replace("/")
        })
            .catch(console.error);
    }, []);

    return <div>Receiving Data From Github...</div>;

}

export default Callback;