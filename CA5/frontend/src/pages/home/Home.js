import React, { useEffect, useState } from 'react';
import Header from "../../components/Header/Header.js";
import './Home.css'
import {
    getCommoditiesSize,
    getCommodities,
} from "../../apis/CommoditiesRequest.js";
import Footer from "../../components/Footer/Footer.js";
import Card from "../../components/Card/Card.js";
import {fetchBuyList} from "../../components/utils.js";


const Home = ({buyList, setBuyList}) => {

    // const { buyList = [] } = props;

    console.log("------------> " + buyList);

    const [commoditiesList, setCommoditiesList] = useState([]);
    const [commoditiesSize, setCommoditiesSize] = useState(0);
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");
    const [sortMethod, setSortMethod] = useState("");
    const [commoditiesAvailable, setCommoditiesAvailable] = useState(false);
    const [pageNumber, setPageNumber] = useState(0);
    const [pageSize, setPageSize] = useState(12);
    const [totalPages, setTotalPages] = useState(Math.ceil(commoditiesSize / pageSize));



    useEffect(() => {
        fetchBuyList().then(response => {
            setBuyList(response.data);
        });
        setTotalPages(Math.ceil(commoditiesSize / pageSize));
    }, []);

    useEffect(() => {
        const req = {"sortMethod":  sortMethod, "searchMethod": searchMethod, "searchedText": searchedText, "commoditiesAvailable": commoditiesAvailable, "pageNumber": pageNumber, "pageSize": pageSize};
        getCommodities(req).then((response) => {
            let result = [];
            for (let i in response.data) {
                result.push(response.data[i]);
            }
            setCommoditiesList(result);
        }).catch(console.error);

        getCommoditiesSize(req).then((response) => {
            setCommoditiesSize(response.data);
            setTotalPages(Math.ceil(response.data / pageSize));
            if(pageNumber+1 > Math.ceil(response.data / pageSize)){
                setPageNumber(0);
            }
        }).catch(console.error);
    }, [sortMethod, commoditiesAvailable, totalPages, pageNumber, searchMethod, searchedText]);


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
    }

    const Pagination = () => {
        const handlePageClick = (e, pageNumber) => {
            e.preventDefault();
            setPageNumber(pageNumber);
        }
        return (
            <div id={"pagination-container"}>
                <div className="pagination">
                    <a href="" className="prev" onClick={(e) => {pageNumber < 1 ? handlePageClick(e, 0) : handlePageClick(e, pageNumber - 1)}}>
                        &#8249;
                    </a>
                    {[...Array(totalPages).keys()].map((currentPageNumber) => (
                        <a key={currentPageNumber} href="" className={pageNumber === currentPageNumber ? "active" : ""} onClick={(e) => handlePageClick(e, currentPageNumber)}>
                            {currentPageNumber + 1}
                        </a>
                    ))}
                    <a href="" className="next" onClick={(e) => {pageNumber > totalPages-2 ? handlePageClick(e, pageNumber) : handlePageClick(e, pageNumber + 1)}}>
                        &#8250;
                    </a>
                </div>
            </div>

        )
    }

    const CommoditiesTable = ({buyList, setBuyList}) => {
        return (
            <div className="main-container-home">
                <div className="product-container">
                    {commoditiesList.length > 0 ?
                        commoditiesList.map((item, index) => (
                            <Card key={index} commodity={item} buyList={buyList} setBuyList={setBuyList}/>
                        )) : <></>
                    }
                </div>
            </div>
        )
    }

    if (sessionStorage.getItem('username') === null) {
        window.location.replace("/login")
        return;
    }


    return(
        <>
            <Header itemCount={!buyList ? 0 : buyList.length} searchFunc={search} username={sessionStorage.getItem('username')}/>
            <main id="home-main">
                <FilterCommodities/>
                <CommoditiesTable buyList={!buyList ? [] : buyList} setBuyList={setBuyList}/>
                <Pagination/>
            </main>
            <Footer/>
        </>

    )
}

export default Home;