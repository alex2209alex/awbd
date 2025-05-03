import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addClientApi,
	deleteClientApi,
	getClientsApi,
	putClientApi,
} from "./api";

// Define the Client type
export interface Client {
	id?: number;
	email: string;
	password: string;
	name: string;
	phoneNumber: string;
	// Add other client properties as needed
}

// Define pagination/page shape for Clients
export interface ClientPage {
	items: Client[];
	pagination: any;
}

// Define the state structure for the client slice
export interface ClientSliceState {
	clients: ClientPage;
	status: "idle" | "loading" | "failed";
}

const initialState: ClientSliceState = {
	clients: {
		items: [],
		pagination: {},
	},
	status: "idle",
};

// Create the Redux slice for Clients
export const clientSlice = createAppSlice({
	name: "clients",
	initialState,
	reducers: (create) => ({
		// Async thunk to fetch clients with optional params
		getClientsAsync: create.asyncThunk(async (params: any) => {
			return await getClientsApi(params);
		}),
		// Synchronous reducers to manipulate local state
		addClient: create.reducer((state, action: PayloadAction<Client>) => {
			state.clients.items.push(action.payload);
		}),
		removeClient: create.reducer((state, action: PayloadAction<number>) => {
			state.clients.items = state.clients.items.filter(
				(client) => client.id !== action.payload,
			);
		}),
		updateClient: create.reducer((state, action: PayloadAction<Client>) => {
			state.clients.items = state.clients.items.map((client) =>
				client.id === action.payload.id ? action.payload : client,
			);
		}),
		// Async thunks for API calls
		addClientAsync: create.asyncThunk(async (client: Client) => {
			return await addClientApi(client);
		}),
		removeClientAsync: create.asyncThunk(async (clientId: number) => {
			return await deleteClientApi(clientId);
		}),
		updateClientAsync: create.asyncThunk(async (client: any) => {
			return await putClientApi(client.id, client);
		}),
	}),
	selectors: {
		selectClients: (state) => state.clients,
		selectClientStatus: (state) => state.status,
	},
});

// Export actions
export const {
	getClientsAsync,
	addClient,
	removeClient,
	updateClient,
	addClientAsync,
	removeClientAsync,
	updateClientAsync,
} = clientSlice.actions;

// Export selectors
export const { selectClients, selectClientStatus } = clientSlice.selectors;
