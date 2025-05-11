import apiClient from "@/lib/apiClient";

export interface Client {
	id?: number;
	email: string;
	password?: string;
	name: string;
	phoneNumber: string;
	// Add other client properties as needed
}

// Fetch all clients (GET)
export const getClients = async (): Promise<Client[]> => {
	const { data } = await apiClient.get("/clients");
	return data;
};

// Add a new client (POST)
export const addClientApi = async (client: Client): Promise<void> => {
	try {
		await apiClient.post("/clients", client, {
			headers: { "Content-Type": "application/json" },
		});
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Delete a client by ID (DELETE)
export const deleteClientApi = async (clientId: number): Promise<void> => {
	try {
		await apiClient.delete(`/clients/${clientId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Fetch clients with query params (GET with params)
export const getClientsApi = async (params: any): Promise<Client[]> => {
	try {
		const response = await apiClient.get("/clients", {
			params,
		});
		return response.data as Client[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getClientByIdApi = async (id: number): Promise<Client> => {
	try {
		const response = await apiClient.get(`/clients/${id}`)
		return response.data as Client;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Update an existing client (PUT)
export const putClientApi = async (
	clientId: number,
	client: Partial<Client>,
): Promise<Client> => {
	try {
		const response = await apiClient.put(
			`/clients/${clientId}`,
			client,
			{ headers: { "Content-Type": "application/json" } },
		);
		return response.data as Client;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
