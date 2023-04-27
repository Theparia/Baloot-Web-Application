import axios from "./BaseRequest.js";

export async function getUser(username){
    return axios.get("/users/" + username);
}