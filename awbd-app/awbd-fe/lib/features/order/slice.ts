import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addOrderApi,
	deleteOrderApi,
	getOrderByIdApi,
	getOrdersApi,
	putOrderApi,
} from "./api";

// Define the shape of an Order
export interface Order {
	id?: number;
	address: string;
	products: {
		id: number;
		quantity: number;
	}[];
}

// Define the state structure
export interface OrderSliceState {
	orders: Order[];
	status: "idle" | "loading" | "failed";
}

const initialState: OrderSliceState = {
	orders: [],
	status: "idle",
};

// Create the Redux slice
export const orderSlice = createAppSlice({
	name: "orders",
	initialState,
	reducers: (create) => ({
		getOrdersAsync: create.asyncThunk(async (params: any) => {
			return await getOrdersApi(params);
		}),
		getOrderByIdAsync: create.asyncThunk(async (id: number) => {
			return await getOrderByIdApi(id);
		}),
		addOrder: create.reducer((state, action: PayloadAction<Order>) => {
			state.orders.push(action.payload);
		}),
		removeOrder: create.reducer((state, action: PayloadAction<number>) => {
			state.orders = state.orders.filter((order) => order.id !== action.payload);
		}),
		updateOrder: create.reducer((state, action: PayloadAction<Order>) => {
			state.orders = state.orders.map((order) =>
				order.id === action.payload.id ? action.payload : order,
			);
		}),
		addOrderAsync: create.asyncThunk(async (order: Order) => {
			console.log(">>>orderin addOrderAsync: ", order)
			return await addOrderApi(order);
		}),
		removeOrderAsync: create.asyncThunk(async (orderId: number) => {
			return await deleteOrderApi(orderId);
		}),
		updateOrderAsync: create.asyncThunk(async (order: Order) => {
			console.log(">>>>ORDER in update async: ", order);
			return await putOrderApi(order.id!, order);
		}),
	}),
	selectors: {
		selectOrders: (state) => state.orders,
		selectStatus: (state) => state.status,
	},
});

// Export actions
export const {
	getOrderByIdAsync,
	addOrder,
	removeOrder,
	updateOrder,
	getOrdersAsync,
	addOrderAsync,
	removeOrderAsync,
	updateOrderAsync,
} = orderSlice.actions;

// Export selectors
export const { selectOrders, selectStatus } = orderSlice.selectors;
