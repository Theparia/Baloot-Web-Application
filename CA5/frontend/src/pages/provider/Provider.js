import Header from "../../components/Header/Header.js";
import Footer from "../../components/Footer/Footer.js";
import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getProvider, getProviderCommodities} from "../../apis/ProviderRequest.js";
import "./Provider.css"
import {getCommodity, getSuggestedCommodities} from "../../apis/CommoditiesRequest.js";
import {addToBuyList, getBuyList, removeFromBuyList} from "../../apis/UserRequest.js";

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


const ProvidersProducts = () => {
    const {providerId} = useParams();
    const [providerCommoditiesList, setProviderCommoditiesList] = useState([]);
    const [buyList, setBuyList] = useState([]);

    useEffect(() => {
        fetchBuyList().then();
    }, []);

    useEffect(()=>{
        getProviderCommodities(providerId).then((response) => {
            let result = [];
            for (let i in response.data) {
                result.push(response.data[i]);
            }
            setProviderCommoditiesList(result);
        }).catch(console.error);
    },[]);

    const fetchBuyList = async () => {
        const buyListResponse = await getBuyList(sessionStorage.getItem('username'));
        const commodities = await extractCommodities(buyListResponse.data);
        setBuyList(commodities);
    }

    const extractCommodities = async (data) => {
        const commodities = [];
        for (const [commodityId, quantity] of Object.entries(data)) {
            const CommodityResponse = await getCommodity(commodityId);
            const commodity = {...CommodityResponse.data, quantity: quantity};
            commodities.push(commodity);
        }
        return commodities;
    }

    const ProviderCommoditiesTable = () => {
        return (
            <div id="product-container-provider" className="product-container">
                {providerCommoditiesList.length > 0 ?
                    providerCommoditiesList.map((item, index) => (
                        <ProviderProductCard key={index} commodity={item}/>
                    )) : <></>
                }
            </div>
        )
    }

    const ProviderProductCard = ({commodity}) => {
        const AddCommodityToUsersBuyListButton = ({commodity_in}) => {
            const isCommodityInBuyList = buyList.some((item) => item.id === commodity_in.id);
            const buyListItem = buyList.filter(item => item.id === commodity.id).find(item => item);
            if(commodity.inStock <= 0){
                return (
                    <button className="add-to-cart-button-disabled" type="button" disabled={true}>add to cart</button>
                )
            }
            if (isCommodityInBuyList) {
                return (
                    <div className="add-remove-button-home">
                        <div className="add-remove-buttons" onClick={(e) => handleRemoveFromBuyList(e)}>-</div>
                        <div> {buyListItem.quantity}</div>
                        <div className="add-remove-buttons" onClick={(e) => handleAddToBuyList(e)}>+</div>
                    </div>
                )
            } else {
                return (
                    <button className="add-to-cart-button-home" type="button" onClick={(e) => handleAddToBuyList(e)}>add to cart</button>
                )
            }
        }

        const handleAddToBuyList = (e) => {
            e.preventDefault();
            addToBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async (response) => {
                console.log("ADD TO BUY LIST");
                await fetchBuyList();
            }).catch((error) => alert(error.response.data))
        }

        const handleRemoveFromBuyList = (e) => {
            e.preventDefault();
            removeFromBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async(response) => {
                console.log("REMOVE FROM BUY LIST");
                await fetchBuyList();
            }).catch((error) => alert(error.response.data))
        }

        return (
            <div className="product-card" id="suggested-product-card">
                <a href={"/commodities/" + commodity.id}>
                    <div className="product-title">
                        {commodity.name}
                    </div>
                </a>
                <div className="product-inStock">
                    <span> {commodity.inStock} </span> left in stock
                </div>
                <a href={"/commodities/" + commodity.id}>
                    <img className="w-100" src={commodity.image} alt="ProductImage"/>
                </a>
                <div className="product-price">
                    <div>
                        <span>{commodity.price}</span>$
                    </div>
                    <AddCommodityToUsersBuyListButton commodity_in={commodity}/>
                </div>
            </div>
        )
    }

    return (
        <div>
            <div id="provider-product-topic" className="suggested-product-topic">
                All provided commodities
            </div>
            <ProviderCommoditiesTable/>
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
            <Header username={sessionStorage.getItem('username')}/>
            <ProviderInfo/>
            <ProvidersProducts/>
            <Footer/>
        </>
    )
}

export default Provider;