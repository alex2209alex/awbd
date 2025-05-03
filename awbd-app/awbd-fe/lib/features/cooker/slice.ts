import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addCookerApi,
	deleteCookerApi,
	getCookersApi,
	putCookersApi,
} from "./api";

// Define the shape of a Cooker
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

// Define the state structure
export interface CookerSliceState {
	cookers: Cooker[];
	status: "idle" | "loading" | "failed";
}

const initialState: CookerSliceState = {
	cookers: [],
	status: "idle",
};

// Create the Redux slice
export const cookerSlice = createAppSlice({
	name: "cookers",
	initialState,
	reducers: (create) => ({
		getCookersAsync: create.asyncThunk(async (params: any) => {
			return await getCookersApi(params);
		}),
		addCooker: create.reducer((state, action: PayloadAction<Cooker>) => {
			state.cookers.push(action.payload);
		}),
		removeCooker: create.reducer((state, action: PayloadAction<number>) => {
			state.cookers = state.cookers.filter(
				(cooker) => cooker.id !== action.payload,
			);
		}),
		updateCooker: create.reducer((state, action: PayloadAction<Cooker>) => {
			state.cookers = state.cookers.map((cooker) =>
				cooker.id === action.payload.id ? action.payload : cooker,
			);
		}),
		addCookerAsync: create.asyncThunk(async (cooker: Cooker) => {
			return await addCookerApi(cooker);
		}),
		removeCookerAsync: create.asyncThunk(async (cookerId: number) => {
			return await deleteCookerApi(cookerId);
		}),
		updateCookerAsync: create.asyncThunk(async (cooker: any) => {
			console.log(">>>>COOKER in update async: ", cooker);

			return await putCookersApi(cooker.id, cooker);
		}),
	}),
	selectors: {
		selectCookers: (state) => state.cookers,
		selectStatus: (state) => state.status,
	},
});

// Export actions
export const {
	addCooker,
	removeCooker,
	updateCooker,
	getCookersAsync,
	addCookerAsync,
	removeCookerAsync,
	updateCookerAsync,
} = cookerSlice.actions;

// Export selectors
export const { selectCookers, selectStatus } = cookerSlice.selectors;
