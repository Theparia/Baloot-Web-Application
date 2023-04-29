import React, {useEffect, useState} from 'react';
import './Header.css';
import {getBuyList} from "../../apis/UserRequest.js";

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

const CartButton = ({username}) => {
    const [itemCount, setItemCount] = useState(0);
    useEffect(() => { // TODO: hook other than useEffect?
        getBuyList(username).then(response => {
                setItemCount(Object.keys(response.data).length)
            }
        )
    }, []); //TODO: Update Cart Item after adding to buylist

    return (
        <a className={`cart ${itemCount > 0 ? 'cart-on' : 'cart-off'} font`} href={"/users/" + username}>
            <div>
                <div>
                    Cart
                </div>
                <div>
                    {itemCount}
                </div>
            </div>
        </a>
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
                <option value="provider">provider</option>
            </select>
            <input className="glass-pic mr-auto" type="image" src="/images/searchGlass.png" alt="Submit" value="submit" onClick={(e) => performSearch(e)} />
        </form>
    )
}

const RegisterLoginButtons = () => {
    return (
        <div>
            <button className="register customized-navbar-button m-4" onClick={() => window.location.replace("/signup")}>Register</button>
            <button className="login customized-navbar-button m-4" onClick={() => window.location.replace("/login")}>Login</button>
        </div>

    )
}

const Header = (props) => {
    return(
        <header>
            <nav className="navbar">
                <BalootLogo />
                {props.username!=null && <Username username={props.username}/>}
                {props.searchBar && <CommoditiesSearchFrom searchFunc={props.searchFunc}/>}
                {props.username!=null && <CartButton username={props.username}/>}
                {props.username==null && <RegisterLoginButtons/>}
            </nav>
        </header>
    )
}

export default Header;