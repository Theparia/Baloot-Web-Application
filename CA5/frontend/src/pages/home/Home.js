import React, { useEffect, useState } from 'react';
import Header from "../../components/Header/Header.js";
import './Home.css'
import {
    filterCommodities,
    getCommodities, getCommodity,
    searchCommodities,
} from "../../apis/CommoditiesRequest.js";
import {addToBuyList, getBuyList, removeFromBuyList} from "../../apis/UserRequest.js";


const Home = () => {
    const [commoditiesList, setCommoditiesList] = useState([]);
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");
    const [sortMethod, setSortMethod] = useState("");
    const [commoditiesAvailable, setCommoditiesAvailable] = useState(false);
    const [buyList, setBuyList] = useState([]);

    useEffect(() => {
        getCommodities().then((response) => {
            let result = [];
            for(let i in response.data) {
                result.push(response.data[i]);
            }
            setCommoditiesList(result);
            fetchBuyList();
            // setCommoditiesAvailable(false);
            // setSortMethod("")
            // setLoadingState(false)
        }).catch(console.error);
    }, []);

    useEffect(() => {
        const req = {"sortMethod":  sortMethod, "searchMethod": searchMethod, "searchedText": searchedText, "commoditiesAvailable": commoditiesAvailable};
        filterCommodities(req).then((response) => {
            let result = [];
            for (let i in response.data) {
                console.log(response.data[i].name);
                result.push(response.data[i]);
            }
            setCommoditiesList(result);
        }).catch(console.error);
    }, [sortMethod, commoditiesAvailable]);


    const FilterCommodities = () => {
        return (
            <div className="sort-section">
                <div className="available-commodities">
                    Available commodities
                </div>
                <label className="switch">
                    <input type="checkbox" className="toggle" checked={commoditiesAvailable} onChange={() => setCommoditiesAvailable(prevCommoditiesAvailable => !prevCommoditiesAvailable)}/>
                    <span className="slider round"></span>
                </label>
                <div className="sort-by">
                    <span id="sort-by-text">
                        sort by:
                    </span>
                    <div>
                        <ul className="sort-options">
                            <li id={`${sortMethod === "name" ? 'name-sort-option-on' : 'name-sort-option-off'}`} onClick={() => setSortMethod(prevSortMethod  => prevSortMethod  === "name" ? "" : "name")}><a>name</a></li>
                            <li id={`${sortMethod === "price" ? 'price-sort-option-on' : 'price-sort-option-off'}`} onClick={() => setSortMethod(prevSortMethod  => prevSortMethod  === "price" ? "" : "price")}><a>price</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        )
    }

    function search(method, text){
        setSearchMethod(method)
        setSearchedText(text)
        const req = {"searchMethod" :  method ,"searchedText" : text};
        searchCommodities(req).then(response => {
            let result = [];
            for(let i in response.data) {
                console.log(response.data[i].name)
                result.push(response.data[i]);
            }
            setCommoditiesList(result)
        }).catch(error => console.log(error));
    }

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

    const CommodityCard = ({commodity}) => {
        const AddCommodityToUsersBuyListButton = ({commodity_in}) => {
            const isCommodityInBuyList = buyList.some((item) => item.id === commodity_in.id);
            const buyListItem = buyList.filter(item => item.id === commodity.id).find(item => item);
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
            }).catch((error) => console.log("ERROR: " + error.data))
        }

        const handleRemoveFromBuyList = (e) => {
            e.preventDefault();
            removeFromBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async(response) => {
                console.log("REMOVE FROM BUY LIST");
                await fetchBuyList();
            }).catch((error) => console.log("ERROR: " + error.data))
        }

        return (
            <div className="product-card-home">
                <a href={"/commodities/" + commodity.id}>
                    <div className="product-title-home">
                        {commodity.name}
                    </div>
                </a>
                <div className="product-inStock-home">
                    <span> {commodity.inStock} </span> left in stock
                </div>
                <a href={"/commodities/" + commodity.id}>
                    <img className="w-100" src={commodity.image} alt="ProductImage"/>
                </a>
                <div className="product-price-home">
                    <div>
                        <span>{commodity.price}</span>$
                    </div>
                    <AddCommodityToUsersBuyListButton commodity_in={commodity}/>
                </div>
            </div>
        )
    }

    const CommoditiesTable = () => {
        return (
            <div className="main-container-home">
                <div className="product-container-home">
                    {commoditiesList.length > 0 ?
                        commoditiesList.map((item, index) => (
                            <CommodityCard key={index} commodity={item}/>
                        )) : <></>
                    }
                </div>
            </div>
        )
    }

    return(
        <>
            <Header searchBar={true} searchFunc={search} username={sessionStorage.getItem('username')} />
            <main id="home-main">
                <FilterCommodities/>
                <CommoditiesTable/>
            </main>
        </>
    )
}

export default Home;