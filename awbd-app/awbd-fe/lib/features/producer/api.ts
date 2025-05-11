// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";
import { Producer } from "./slice"; // Adjust the path

export const getProducers = async () => {
	const { data } = await apiClient.get("/producers");
	return data
};

export const addProducerApi = async (producer: Producer) => {
	try {
		await apiClient.post("/producers", producer);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteProducerApi = async (producerId: number) => {
	try {
		await apiClient.delete(`/producers/${producerId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getProducersApi = async (params: any) => {
	try {
		const response = await apiClient.get("/producers", {
			params,
		});
		return response.data;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putProducersApi = async (producerId: number, producer: any) => {
	try {
		const response = await apiClient.put(
			`/producers/${producerId}`, producer);
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
