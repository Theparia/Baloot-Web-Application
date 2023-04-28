import axios from "./BaseRequest.js";

export async function login(data){
    console.log('In login axios ', data);
    return axios.post("/login", data);
}

export async function signup(data){
    console.log('In signup axios ', data);
    return axios.post("/signup", data);
}

export async function getLoggedInUser(){
    return axios.get("/user");
}

export async function logout(){
    return axios.get("/logout")
}
