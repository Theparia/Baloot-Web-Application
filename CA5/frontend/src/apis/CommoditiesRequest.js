import axios from "./BaseRequest.js";

export const getCommodities = () => {
    console.log('Getting Commodities');
    return axios.get('/commodities');
}

export function getCommodity(id){
    return axios.get('/commodities/' + id);
}

// export const sortCommodities = data => {
//     console.log('Sorting Commodities ', data);
//     return axios.get('/commodities/sort', {params:data});
// }

export const searchCommodities = data => {
    console.log('Getting Searched Commodities ', data);
    return axios.get('/commodities/search', {params: data});
}