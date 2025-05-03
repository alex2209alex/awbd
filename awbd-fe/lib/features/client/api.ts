import axios from "axios";

export interface Client {
	id?: number;
	email: string;
	password: string;
	name: string;
	phoneNumber: string;
	// Add other client properties as needed
}

// Fetch all clients (GET)
export const getClients = async (): Promise<Client[]> => {
	const response = await fetch("http://localhost:8080/clients", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: Client[] } = await response.json();
	return result.data;
};

// Add a new client (POST)
export const addClientApi = async (client: Client): Promise<void> => {
	try {
		await axios.post("http://localhost:8080/clients", client, {
			headers: { "Content-Type": "application/json" },
		});
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Delete a client by ID (DELETE)
export const deleteClientApi = async (clientId: number): Promise<void> => {
	try {
		await axios.delete(`http://localhost:8080/clients/${clientId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Fetch clients with query params (GET with params)
export const getClientsApi = async (params: any): Promise<Client[]> => {
	try {
		const response = await axios.get("http://localhost:8080/clients", {
			params,
		});
		return response.data as Client[];
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
		const response = await axios.put(
			`http://localhost:8080/clients/${clientId}`,
			client,
			{ headers: { "Content-Type": "application/json" } },
		);
		return response.data as Client;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
