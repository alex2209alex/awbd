import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
import {
	addProductApi,
	deleteProductApi,
	getProductByIdApi,
	getProductsApi,
	putProductsApi,
} from "./api";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Product {
	id: number;
	name: string;
	price: number;
	description: string;
	ingredients: [
		{
			id: number;
			quantity: number;
		},
	];
}

// Define the state structure
export interface ProductSliceState {
	products: Product[];
	status: "idle" | "loading" | "failed";
}

const initialState: ProductSliceState = {
	products: [],
	status: "idle",
};

// Create the Redux slice
export const productSlice = createAppSlice({
	name: "products",
	initialState,
	reducers: (create) => ({
		getProductsAsync: create.asyncThunk(async (params: any) => {
			return await getProductsApi(params);
		}),
		getProductByIdAsync: create.asyncThunk(async (id: number) => {
			return await getProductByIdApi(id);
		}),
		addProduct: create.reducer((state, action: PayloadAction<Product>) => {
			state.products.push(action.payload);
		}),
		removeProduct: create.reducer((state, action: PayloadAction<number>) => {
			state.products = state.products.filter(
				(product) => product.id !== action.payload,
			);
		}),
		updateProduct: create.reducer((state, action: PayloadAction<Product>) => {
			state.products = state.products.map((product) =>
				product.id === action.payload.id ? action.payload : product,
			);
		}),
		addProductAsync: create.asyncThunk(async (product: Product) => {
			return await addProductApi(product);
		}),
		removeProductAsync: create.asyncThunk(async (productId: number) => {
			return await deleteProductApi(productId);
		}),
		updateProductAsync: create.asyncThunk(async (product: any) => {
			if (product.producer?.id) {
				product.producer = product.producer.id;
			}
			product.producerId = product.producer;

			return await putProductsApi(product.id, product);
		}),
	}),
	selectors: {
		selectProducts: (state) => state.products,
		selectStatus: (state) => state.status,
	},
});

// Export actions
export const {
	getProductByIdAsync,
	addProduct,
	removeProduct,
	updateProduct,
	getProductsAsync,
	addProductAsync,
	removeProductAsync,
	updateProductAsync,
} = productSlice.actions;

// Export selectors
export const { selectProducts, selectStatus } = productSlice.selectors;
