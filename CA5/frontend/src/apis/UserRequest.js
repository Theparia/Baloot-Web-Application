import axios from "./BaseRequest.js";

export async function getUser(username){
    return axios.get("/users/" + username);
}

export async function getBuyList(username){
    return axios.get("/users/" + username + "/buyList");
}

export async function getPurchasedList(username){
    return axios.get("/users/" + username + "/purchasedList");
}