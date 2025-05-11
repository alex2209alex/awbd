// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";
import { Producer } from "./slice"; // Adjust the path

export const getProducers = async () => {
<<<<<<< Updated upstream
	const response = await fetch("http://backend:8080/producers", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: number } = await response.json();

	return result;
=======
	const { data } = await apiClient.get("/producers");
	return data
>>>>>>> Stashed changes
};

export const addProducerApi = async (producer: Producer) => {
	try {
<<<<<<< Updated upstream
		await axios.post("http://backend:8080/producers", producer);
=======
		await apiClient.post("/producers", producer);
>>>>>>> Stashed changes
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteProducerApi = async (producerId: number) => {
	try {
<<<<<<< Updated upstream
		await axios.delete(`http://backend:8080/producers/${producerId}`);
=======
		await apiClient.delete(`/producers/${producerId}`);
>>>>>>> Stashed changes
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getProducersApi = async (params: any) => {
	try {
<<<<<<< Updated upstream
		const response = await axios.get("http://backend:8080/producers", {
=======
		const response = await apiClient.get("/producers", {
>>>>>>> Stashed changes
			params,
		});
		return response.data;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putProducersApi = async (producerId: number, producer: any) => {
	try {
<<<<<<< Updated upstream
		const response = await axios.put(
			`http://backend:8080/producers/${producerId}`, producer);
=======
		const response = await apiClient.put(
			`/producers/${producerId}`, producer);
>>>>>>> Stashed changes
		return response.data;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getProducerByIdApi = async (id: number): Promise<Producer> => {
	try {
		const response = await apiClient.get(`/producers/${id}`)
		return response.data as Producer;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
