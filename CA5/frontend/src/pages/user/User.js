import React, { useState } from 'react';
import Header from "../../components/Header/Header.js";
import {useParams} from "react-router-dom";

const User = () => {
    const {username} = useParams();
    return(
        <div>
            <Header searchBar={false} username={username}/>
        </div>
    )
}

export default User;