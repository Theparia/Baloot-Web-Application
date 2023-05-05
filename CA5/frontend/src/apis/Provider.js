import axios from "./BaseRequest.js";

export function getProvider(id){
    return axios.get('/providers/' + id);
}