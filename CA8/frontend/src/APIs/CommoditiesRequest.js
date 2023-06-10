import axios from "./BaseRequest.js";

export function getCommoditiesSize(data){
    console.log('Getting Commodities, data: ', data);
    return axios.get('/commodities/size/', {params: data, headers : {Authorization : localStorage.getItem("userJWT")}});
    // return axios.get('/commodities/size/', {params: data});
}

export function getCommodity(id){
    return axios.get('/commodities/' + id, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function getCommodities(data) {
    console.log('Filtering Commodities ', data);
    return axios.get('/commodities/', {params: data, headers : {Authorization : localStorage.getItem("userJWT")}});
    // return axios.get('/commodities/', {params: data});
}

export function rateCommodity(commodityId, data) {
    return axios.post("/commodities/" + commodityId + "/rating", data, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function getSuggestedCommodities(commodityId) {
    console.log('Suggesting Commodities ');
    return axios.get("/commodities/" + commodityId + "/suggested/", {headers : {Authorization : localStorage.getItem("userJWT")}});
}