import axios from "./BaseRequest.js";

export function getProvider(id){
    return axios.get('/providers/' + id, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function getProviderCommodities(id){
    return axios.get('/providers/' + id + '/commodities/', {headers : {Authorization : localStorage.getItem("userJWT")}});
}