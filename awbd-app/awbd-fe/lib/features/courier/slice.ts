import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addCourierApi,
	deleteCourierApi,
	getCourierByIdApi,
	getCouriersApi,
	putCouriersApi,
} from "./api";

// Define the shape of a Courier
export interface Courier {
	id: number;
	email: string;
	password: string;
	name: string;
	phoneNumber: string;
	salary: number;
}

export interface CourierPage {
	items: Courier[];
	pagination: any;
}

// Define the state structure
export interface CourierSliceState {
	couriers: CourierPage;
	status: "idle" | "loading" | "failed";
}

const initialState: CourierSliceState = {
	// ingredients: [
	// 	{
	// 		id: 1,
	// 		name: "Tomato",
	// 		price: 2.5,
	// 		calories: 20,
	// 		producer: {
	// 			id: 1,
	// 			name: "Fresh Farms",
	// 			address: "123 Green St, New York",
	// 		},
	// 	},
	// 	{
	// 		id: 2,
	// 		name: "Lettuce",
	// 		price: 1.2,
	// 		calories: 5,
	// 		producer: {
	// 			id: 2,
	// 			name: "Organic Suppliers",
	// 			address: "456 Healthy Ave, California",
	// 		},
	// 	},
	// ],
	couriers: {
		items: [],
		pagination: {},
	},
	status: "idle",
};

// Create the Redux slice
export const courierSlice = createAppSlice({
	name: "couriers",
	initialState,
	reducers: (create) => ({
		getCouriersAsync: create.asyncThunk(async (params: any) => {
			return await getCouriersApi(params);
		}),
		getCourierByIdAsync: create.asyncThunk(async (id: number) => {
			return await getCourierByIdApi(id);
		}),
		addCourier: create.reducer((state, action: PayloadAction<Courier>) => {
			state.couriers.items.push(action.payload);
		}),
		removeCourier: create.reducer((state, action: PayloadAction<number>) => {
			state.couriers.items = state.couriers.items.filter(
				(courier) => courier.id !== action.payload,
			);
		}),
		updateCourier: create.reducer((state, action: PayloadAction<Courier>) => {
			state.couriers.items = state.couriers.items.map((courier) =>
				courier.id === action.payload.id ? action.payload : courier,
			);
		}),
		addCourierAsync: create.asyncThunk(async (courier: Courier) => {
			return await addCourierApi(courier);
		}),
		removeCourierAsync: create.asyncThunk(async (courierId: number) => {
			return await deleteCourierApi(courierId);
		}),
		updateCourierAsync: create.asyncThunk(async (courier: any) => {
			console.log(">>>>COURIER in update async: ", courier);

			return await putCouriersApi(courier.id, courier);
		}),
	}),
	selectors: {
		selectCouriers: (state) => state.couriers,
		selectStatus: (state) => state.status,
	},
});

// Export actions
export const {
	getCouriersAsync,
	getCourierByIdAsync,
	addCourier,
	removeCourier,
	updateCourier,
	addCourierAsync,
	removeCourierAsync,
	updateCourierAsync,
} = courierSlice.actions;

// Export selectors
export const { selectCouriers, selectStatus } = courierSlice.selectors;
