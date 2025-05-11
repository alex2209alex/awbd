import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addProducerApi,
	deleteProducerApi,
	getProducerByIdApi,
	getProducersApi,
	putProducersApi,
} from "./api";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Producer {
	id: number;
	name: string;
	address: string;
}

export interface ProducerPage {
	items: Producer[];
	pagination: any;
}

// Define the state structure
export interface ProducerSliceState {
	producers: ProducerPage;
	status: "idle" | "loading" | "failed";
}

const initialState: ProducerSliceState = {
	// producers: [
	//   { id: 1, name: "Fresh Farms", address: "123 Green St, New York" },
	//   {
	//     id: 2,
	//     name: "Organic Suppliers",
	//     address: "456 Healthy Ave, California",
	//   },
	// ],
	producers: {
		items: [],
		pagination: {},
	},
	status: "idle",
};

// Create the Redux slice
export const producerSlice = createAppSlice({
	name: "producers",
	initialState,
	reducers: (create) => ({
		getProducersAsync: create.asyncThunk(async (params: any) => {
			return await getProducersApi(params);
		}),
		getProducerByIdAsync: create.asyncThunk(async (id: number) => {
			return await getProducerByIdApi(id);
		}),
		addProducer: create.reducer((state, action: PayloadAction<Producer>) => {
			state.producers.items.push(action.payload);
		}),
		removeProducer: create.reducer((state, action: PayloadAction<number>) => {
			if (action.payload)
				state.producers.items = state.producers.items.filter(
					(producer) => producer.id !== action.payload,
				);
		}),
		updateProducer: create.reducer((state, action: PayloadAction<Producer>) => {
			console.log(">>>action payloadi n upadate: ", action.payload)
			state.producers.items = state.producers.items.map(
				(producer: Producer) => {
					if (producer.id === action.payload.id) return action.payload;
					return producer;
				},
			);
		}),
		addProducerAsync: create.asyncThunk(async (producer: Producer) => {
			return await addProducerApi(producer);
		}),
		removeProducerAsync: create.asyncThunk(async (producerId: number) => {
			return await deleteProducerApi(producerId);
		}),
		updateProducerAsync: create.asyncThunk(async (producer: any) => {
			console.log(">>>producer in slice: ", producer);
			return await putProducersApi(producer.id, producer);
		}),
	}),
	selectors: {
		selectProducers: (state) => state.producers,
		selectStatus: (state) => state.status,
	},
});

// Export actions
export const {
	getProducerByIdAsync,
	addProducer,
	removeProducer,
	updateProducer,
	addProducerAsync,
	removeProducerAsync,
	getProducersAsync,
	updateProducerAsync,
} = producerSlice.actions;

// Export selectors
export const { selectProducers, selectStatus } = producerSlice.selectors;

// Async thunk for adding a producer
