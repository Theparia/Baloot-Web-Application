import axios from "./BaseRequest.js";

export function getComments(commodityId){
    return axios.get("/comments/" + commodityId + "/", {headers : {Authorization : localStorage.getItem("userJWT")}});
}
export function getCommentVotes(commentId){
    return axios.get("/comments/" + commentId + "/vote/", {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function likeComment(commentId, data){
    return axios.post("/comments/like/", data, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function dislikeComment(commentId, data){
    return axios.post("/comments/dislike/", data, {headers : {Authorization : localStorage.getItem("userJWT")}});
}

export function addComment(data){
    return axios.post("/comments/add/", data, {headers : {Authorization : localStorage.getItem("userJWT")}})
}