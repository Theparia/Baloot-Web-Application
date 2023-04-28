import React, { useState } from 'react';
import './Header.css';

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



const CommoditiesSearchFrom = ({searchFunc}) => {
    const [searchMethod, setSearchMethod] = useState("name");
    const [searchedText, setSearchedText] = useState("");

    function performSearch(e) {
        e.preventDefault();
        searchFunc(searchMethod, searchedText);
        setSearchedText("");
    }

    return (
        <form className="customized-search-form m-3">
            <input className="customized-search-box mr-auto" type="text" value={searchedText} name="search_query" placeholder="search your product ..." onChange={(event) => {setSearchedText(event.target.value)}} />
            <select className="select-option-box mr-auto" onChange={(event) => {setSearchMethod(event.target.value)}} name="select_option">
                <option value="name">name</option>
                <option value="category">category</option>
            </select>
            <input className="glass-pic mr-auto" type="image" src="/images/searchGlass.png" alt="Submit" value="submit" onClick={(e) => performSearch(e)} />
        </form>
    )
}


const Header = (props) => {

    const username = sessionStorage.getItem('username');

    return(
        <header>
            <nav className="navbar">
                <BalootLogo />
                {username!=null && <Username username={username}/>}
                {props.searchBar && <CommoditiesSearchFrom searchFunc={props.searchFunc}/>}
            </nav>
        </header>
    )
}

export default Header;