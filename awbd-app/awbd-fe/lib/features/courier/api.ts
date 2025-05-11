// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";

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
	const { data } = await apiClient.get("/couriers");
	return data;
};

export const getCourierByIdApi = async (id: number): Promise<Courier> => {
	try {
		const response = await apiClient.get(`/couriers/${id}`)
		return response.data as Courier;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const addCourierApi = async (courier: Courier) => {
	try {
		await apiClient.post("/couriers", courier);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteCourierApi = async (courierId: number) => {
	try {
		await apiClient.delete(`/couriers/${courierId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getCouriersApi = async (params: any) => {
	try {
		const response = await apiClient.get("/couriers", {
			params,
		});
		return response.data as Courier[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putCouriersApi = async (courierId: number, courier: any) => {
	try {
		const response = await apiClient.put(
			`/couriers/${courierId}`,
			courier,
		);
		return response.data as Courier;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
