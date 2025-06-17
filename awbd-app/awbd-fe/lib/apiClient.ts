import axios from "axios";
import { toast } from 'sonner';

const apiClient = axios.create({
    baseURL: "http://localhost:8092/awbd",
});

// Add token to headers automatically
apiClient.interceptors.request.use((config) => {
    const token = typeof window !== "undefined" ? localStorage.getItem("token_awbd") : null;
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    // Handle request errors globally
    console.error("Request error:", error);
    return Promise.reject(error);
});

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        // Log or handle any error
        console.error("API error:", error);

        // Optionally, you can handle based on status
        if (error.response) {
            const { status } = error.response;

            if (status === 401) {
                // For example: redirect to login or show notification
                console.warn("Unauthorized! Redirecting to login...");
                // window.location.href = "/login"; // optional
            } else if (status >= 500) {
                console.warn("Server error occurred.");
            }
        } else if (error.request) {
            console.warn("No response received from server.");
        } else {
            console.warn("Error setting up the request:", error.message);
        }

        console.log(">>>error:", error)

        let config: any = {
            duration: 0,
            message: error?.response?.data?.detail || "Error occured",
            // onClose: () => {
            //     acknowledged.set(true, { id: errorId });
            // }
        };
        // config.description = `Error ID: "${errorId}"`;
        // if (error.description) config.description = i18n.t(`errors.${error.description}`);
        toast.error(config.message, {
            // description: config.description,
            duration: config.duration,
            // onDismiss: config.onClose,
        });

        return Promise.reject(error); // still allow local catch blocks to run
    }
);

export default apiClient;