import React, { useEffect, useState } from 'react';
import Header from "../../components/Header/Header.js";
import './Home.css'
import {
    filterCommodities,
    getAvailableCommodities,
    getCommodities,
    searchCommodities,
    sortCommodities
} from "../../apis/CommoditiesRequest.js";


const Home = () => {
    const [commoditiesList, setCommoditiesList] = useState([]);
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");
    const [sortMethod, setSortMethod] = useState("");
    const [commoditiesAvailable, setCommoditiesAvailable] = useState(false);

    useEffect(() => {
        getCommodities().then((response) => {
            let result = [];
            for(let i in response.data) {
                // console.log(response.data[i].name)
                result.push(response.data[i]);
            }
            setCommoditiesList(result);
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
                    <input type="checkbox" />
                    <span className="slider round" onClick={() => setCommoditiesAvailable(prevCommoditiesAvailable => !prevCommoditiesAvailable)}></span>
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

    const CommodityCart = ({commodity}) => {
        return (
            <div className="product-cart-home">
                <a>
                    <div className="product-title-home">
                        {commodity.name}
                        {/*Huawei nova 9*/}
                    </div>
                </a>
                <div className="product-inStock-home">
                    <span> {commodity.inStock} </span> left in stock
                </div>
                <a href="">
                    <img className="w-100" src={commodity.image} alt="ProductImage"/>
                </a>
                <div className="product-price-home">
                    <div>
                        <span>{commodity.price}</span>$
                    </div>
                    <div>
                        <button className="add-to-cart-button-home" type="button">add to cart</button>
                    </div>
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
                            <CommodityCart key={index} commodity={item}/>
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