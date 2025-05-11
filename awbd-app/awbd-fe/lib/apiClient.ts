import axios from "axios";

const apiClient = axios.create({
    baseURL: "http://backend:8080",
});

// Add token to headers automatically
apiClient.interceptors.request.use((config) => {
    const token = typeof window !== "undefined" ? localStorage.getItem("token_awbd") : null;
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default apiClient;