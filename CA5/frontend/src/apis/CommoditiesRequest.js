import axios from "./BaseRequest.js";

export function getCommodities(){
    console.log('Getting Commodities');
    return axios.get('/commodities');
}

export function getCommodity(id){
    return axios.get('/commodities/' + id);
}

export function filterCommodities(data) {
    console.log('Filtering Commodities ', data);
    return axios.get('/commodities/filter', {params: data});
}

export function sortCommodities(data) {
    console.log('Sorting Commodities ', data);
    return axios.get('/commodities/sort', {params: data});
}

export function searchCommodities(data) {
    console.log('Getting Searched Commodities ', data);
    return axios.get('/commodities/search', {params: data});
}

export function getAvailableCommodities() {
    console.log('Getting Available Commodities ');
    return axios.get('/commodities/available');
}