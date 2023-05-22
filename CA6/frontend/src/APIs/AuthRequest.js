import axios from "./BaseRequest.js";

export function login(data){
    console.log('In login axios ', data);
    return axios.post("/login", data);
}

export function signup(data){
    console.log('In signup axios ', data);
    return axios.post("/signup", data);
}

export function getLoggedInUser(){
    return axios.get("/user");
}

export function logout(){
    return axios.get("/logout")
}
