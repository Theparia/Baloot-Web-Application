import Header from "../../components/Header/Header.js";
import Footer from "../../components/Footer/Footer.js";
import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getProvider} from "../../apis/Provider.js";
import "./Provider.css"

const ProviderInfo = () => {
    const {providerId} = useParams();
    const [provider, setProvider] = useState({});

    useEffect(() => {
        getProvider(providerId).then((response) => {
           setProvider(response.data);
        }).catch((error) => alert("Provider Not Found"));
    }, []);

    return (
        <div className="provider-info">
            <img src={provider.image}/>
            <div id="provider-year"> since {provider.registryDate?.split('-')[0]} </div>
            <div id="provider-name" className="product-name"> {provider.name}</div>
        </div>
    )
}

const Provider = () => {
        if (sessionStorage.getItem('username') === null) {
        window.location.replace("/login")
        return;
    }

    return (
        <>
            <Header searchBar={false} username={sessionStorage.getItem('username')}/>
            <ProviderInfo/>
            <Footer/>
        </>
    )
}

export default Provider;