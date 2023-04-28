import React, { useState } from 'react';
import Header from "../../components/Header/Header.js";
import {useParams} from "react-router-dom";

const User = () => {
    const {username} = useParams();
    if(username !== sessionStorage.getItem('username')){
        alert("You don't have access")
    }
    return(
        <div>
            <Header searchBar={false} username={username}/>
        </div>
    )
}

export default User;