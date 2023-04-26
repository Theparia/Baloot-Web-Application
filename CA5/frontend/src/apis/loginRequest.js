import axios from "./baseRequest.js";

export const getLoggedInUser = data => {
    console.log('Userrrrrrr', data);
    return axios.post("/login", data);
}