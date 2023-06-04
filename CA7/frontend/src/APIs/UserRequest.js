import axios from "./BaseRequest.js";

export function getUser(username){
    return axios.get("/users/" + username);
}

export function getBuyList(username){
    return axios.get("/users/" + username + "/buyList");
}

export function getPurchasedList(username){
    return axios.get("/users/" + username + "/purchasedList");
}

export function addCredit(username, amount){
    return axios.post("/users/" + username + "/credit", amount);
}

export function addToBuyList(username, commodityId){
    return axios.post("/users/" + username + "/buyList/add", commodityId);
}

export function removeFromBuyList(username, commodityId){
    return axios.post("/users/" + username + "/buyList/remove", commodityId);
}

export function finalizePayment(username){
    return axios.get("/users/" + username + "/payment");
}

export function applyDiscountCode(username, code){
    return axios.post("/users/" + username + "/discount", code);
}

export function deleteDiscountCode(username) {
    return axios.delete("/users/" + username + "/discount");
}