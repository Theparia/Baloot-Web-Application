import {addToBuyList, finalizePayment} from "./UserRequest.js";

// addToBuyList("amir", {"id": "1"})
//     .then(response => console.log(response.data))
//     .catch(error => console.error(error));


// addToBuyList("amir", {"id": "4"})
//     .then(response => console.log(response.data))
//     .catch(error => console.error(error));

addToBuyList("amir", {"id": "6"})
    .then(response => console.log(response.data))
    .catch(error => console.error(error));

// finalizePayment("amir").then((response) => {
//     console.log(response.data);
// })