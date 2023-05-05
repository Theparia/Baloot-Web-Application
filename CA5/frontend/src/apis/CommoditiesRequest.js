import axios from "./BaseRequest.js";

export function getCommoditiesSize(data){
    console.log('Getting Commodities, data: ', data);
    return axios.get('/commodities/size/', {params: data});
}

export function getCommodity(id){
    return axios.get('/commodities/' + id);
}

export function getCommodities(data) {
    console.log('Filtering Commodities ', data);
    return axios.get('/commodities/', {params: data});
}

export function getComments(commodityId){
    return axios.get("/commodities/" + commodityId + "/comments/");
}