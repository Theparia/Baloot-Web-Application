import React, { useEffect, useState } from 'react';
import Header from "../../components/Header/Header.js";
import {getCommodities, searchCommodities} from "../../apis/CommoditiesRequest.js";

const Home = ({}) => {
    const [CommoditiesList, setCommoditiesList] = useState([]);
    const [searchMethod, setSearchMethod] = useState(null);
    const [searchedText, setSearchedText] = useState("");
    // const [loadingState, setLoadingState] = useState(true);

    useEffect(() => {
        getCommodities().then((response) => {
            let result = [];
            for(let i in response.data) {
                console.log(response.data[i].name)
                result.push(response.data[i]);
            }
            setCommoditiesList(result)
            // setLoadingState(false)
        }).catch(console.error);
    }, []);


    function search(method, text){
        setSearchMethod(method)
        setSearchedText(text)
        const req = { "searchMethod" :  method , "searchedTxt" : text};
        searchCommodities(req).then(response => {
            let result = [];
            for(let i in response.data) {
                console.log(response.data[i].name)
                result.push(response.data[i]);
            }
            setCommoditiesList(result)
        }).catch(error => console.log("ERRORRRR: " + error));
    }


    return(
        <div>
            <Header searchBar={true} searchFunc={search} username={sessionStorage.getItem('username')} />
            <p></p>
        </div>
    )
}

export default Home;