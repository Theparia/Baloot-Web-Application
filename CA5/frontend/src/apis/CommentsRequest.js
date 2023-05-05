import axios from "./BaseRequest.js";

export function getComments(commodityId){
    return axios.get("/comments/" + commodityId + "/");
}
export function getCommentVotes(commentId){
    return axios.get("/comments/" + commentId + "/vote/");
}

export function likeComment(commentId, data){
    return axios.post("/comments/" + commentId + "/like/", data);
}

export function dislikeComment(commentId, data){
    return axios.post("/comments/" + commentId + "/dislike/", data);
}

export function addComment(data){
    return axios.post("/comments/add/", data)
}