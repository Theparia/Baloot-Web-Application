import React, { useState } from 'react';
import './Header.css';
import user from "../../pages/user/User.js";

const BalootLogo = () => {
    return (
        <div className="baloot">
            <div className="baloot-icon">
                <a href="/">
                    <img src="/images/logo.png" alt="logo" />
                </a>
            </div>
            <div className="baloot-text">
                Baloot
            </div>
        </div>
    )
}

const Username = ({username}) => {
    return (
        <div className="username font">
            #{username}
        </div>
    )
}
const Header = ({searchBar, username}) => {

    return(
        <header>
            <nav className="navbar">
                <BalootLogo />
                <Username username={username}/>
            </nav>
        </header>
    )
}

export default Header;