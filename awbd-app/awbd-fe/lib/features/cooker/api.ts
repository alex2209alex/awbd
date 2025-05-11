// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";

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
	const { data } = await apiClient.get("/cooks");
	return data;
};

export const addCookerApi = async (cooker: Cooker) => {
	try {
		await apiClient.post("/cooks", cooker);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteCookerApi = async (cookerId: number) => {
	try {
		await apiClient.delete(`/cooks/${cookerId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getCookersApi = async (params: any) => {
	try {
		const response = await apiClient.get("/cooks", {
			params,
		});
		return response.data as Cooker[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getCookerByIdApi = async (id: number): Promise<Cooker> => {
	try {
		const response = await apiClient.get(`/coks/${id}`)
		return response.data as Cooker;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};


export const putCookersApi = async (cookerId: number, cooker: any) => {
	try {
		const response = await apiClient.put(
			`/cooks/${cookerId}`,
			cooker,
		);
		return response.data as Cooker;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
