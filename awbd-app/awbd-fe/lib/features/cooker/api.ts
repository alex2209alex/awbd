// A mock function to mimic making an async request for data
import axios from "axios";

// Define the Cooker type
export interface Cooker {
	id?: number;
	email: string;
	password: string;
	name: string;
	salary: number;
	products: [
		{
			id: number;
		},
	];
}

export const getCookers = async () => {
	const response = await fetch("http://localhost:8080/cooks", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: Cooker[] } = await response.json();

	return result;
};

export const addCookerApi = async (cooker: Cooker) => {
	try {
		await axios.post("http://localhost:8080/cooks", cooker);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteCookerApi = async (cookerId: number) => {
	try {
		await axios.delete(`http://localhost:8080/cooks/${cookerId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getCookersApi = async (params: any) => {
	try {
		const response = await axios.get("http://localhost:8080/cooks", {
			params,
		});
		return response.data as Cooker[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putCookersApi = async (cookerId: number, cooker: any) => {
	try {
		const response = await axios.put(
			`http://localhost:8080/cooks/${cookerId}`,
			cooker,
		);
		return response.data as Cooker;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
