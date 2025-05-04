// A mock function to mimic making an async request for data
import axios from "axios";

// Define the Courier type
export interface Courier {
	id?: number;
	email: string;
	password: string;
	name: string;
	phoneNumber: string;
	salary: number;
	// Add other courier properties as needed
}

export const getCouriers = async () => {
	const response = await fetch("http://localhost:8080/couriers", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: Courier[] } = await response.json();

	return result;
};

export const addCourierApi = async (courier: Courier) => {
	try {
		await axios.post("http://localhost:8080/couriers", courier);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteCourierApi = async (courierId: number) => {
	try {
		await axios.delete(`http://localhost:8080/couriers/${courierId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getCouriersApi = async (params: any) => {
	try {
		const response = await axios.get("http://localhost:8080/couriers", {
			params,
		});
		return response.data as Courier[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putCouriersApi = async (courierId: number, courier: any) => {
	try {
		const response = await axios.put(
			`http://localhost:8080/couriers/${courierId}`,
			courier,
		);
		return response.data as Courier;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
