import axios from "./BaseRequest.js";

export async function getCommodities(){
    console.log('Getting Commodities');
    return axios.get('/commodities');
}

export async function getCommodity(id){
    return axios.get('/commodities/' + id);
}

export async function filterCommodities(data) {
    console.log('Filtering Commodities ', data);
    return axios.get('/commodities/filter', {params: data});
}

export async function sortCommodities(data) {
    console.log('Sorting Commodities ', data);
    return axios.get('/commodities/sort', {params: data});
}

export async function searchCommodities(data) {
    console.log('Getting Searched Commodities ', data);
    return axios.get('/commodities/search', {params: data});
}

export async function getAvailableCommodities() {
    console.log('Getting Available Commodities ');
    return axios.get('/commodities/available');
}