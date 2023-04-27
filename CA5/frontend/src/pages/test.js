import {getLoggedInUser} from "../apis/AuthRequest.js";

const eee = {
    "username" :  "amir",
    "password" :  "1234"
}

getLoggedInUser(eee)
    .then(response => console.log(response.data.username))
    .catch(error => console.error(error));


