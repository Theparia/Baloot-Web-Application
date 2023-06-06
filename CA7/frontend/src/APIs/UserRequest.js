import axios from "./BaseRequest.js";

export function getUser(username){
    return axios.get("/users/" + username, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function getBuyList(username){
    return axios.get("/users/" + username + "/buyList", {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function getPurchasedList(username){
    return axios.get("/users/" + username + "/purchasedList", {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function addCredit(username, amount){
    return axios.post("/users/" + username + "/credit", amount, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function addToBuyList(username, commodityId){
    return axios.post("/users/" + username + "/buyList/add", commodityId), {headers : {Authorization : localStorage.getItem("userJWT")}};
}

export function removeFromBuyList(username, commodityId){
    return axios.post("/users/" + username + "/buyList/remove", commodityId, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function finalizePayment(username){
    return axios.get("/users/" + username + "/payment", {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function applyDiscountCode(username, code){
    return axios.post("/users/" + username + "/discount", code, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function deleteDiscountCode(username) {
    return axios.delete("/users/" + username + "/discount", {headers : {Authorization : localStorage.getItem("userJWT")}});
}