// A mock function to mimic making an async request for data
import axios from "axios";
import { Producer } from "./slice"; // Adjust the path

export const getProducers = async () => {
	const response = await fetch("http://localhost:8080/producers", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: number } = await response.json();

	return result;
};

export const addProducerApi = async (producer: Producer) => {
	try {
		await axios.post("http://localhost:8080/producers", producer);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteProducerApi = async (producerId: number) => {
	try {
		await axios.delete(`http://localhost:8080/producers/${producerId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getProducersApi = async (params: any) => {
	try {
		const response = await axios.get("http://localhost:8080/producers", {
			params,
		});
		return response.data;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
