import axios from 'axios'

// export default axios.create({
//     baseURL: 'http://localhost:8080/'
// });

var axiosInstance = axios.create({
    baseURL: "http://localhost:8080/",
    responseType: "json",
    headers: {
        "Content-Type": "application/json;charset=UTF-8",
        Accept: "application/json"
    }
});

axiosInstance.interceptors.response.use( response => response );

export default axiosInstance;
