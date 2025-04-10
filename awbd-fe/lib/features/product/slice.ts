import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
}

// Define the state structure
export interface ProductSliceState {
  products: Product[];
  status: "idle" | "loading" | "failed";
}

const initialState: ProductSliceState = {
  products: [
    { id: 1, name: "Apple", price: 1.5, description: "Fresh Red Apple" },
    { id: 2, name: "Orange", price: 2.0, description: "Juicy Sweet Orange" },
  ],
  status: "idle",
};

// Create the Redux slice
export const productSlice = createAppSlice({
  name: "products",
  initialState,
  reducers: (create) => ({
    addProduct: create.reducer((state, action: PayloadAction<Product>) => {
      state.products.push(action.payload);
    }),
    removeProduct: create.reducer((state, action: PayloadAction<number>) => {
      state.products = state.products.filter(
        (product) => product.id !== action.payload
      );
    }),
    updateProduct: create.reducer((state, action: PayloadAction<Product>) => {
      state.products = state.products.map((product) =>
        product.id === action.payload.id ? action.payload : product
      );
    }),
  }),
  selectors: {
    selectProducts: (state) => state.products,
    selectStatus: (state) => state.status,
  },
});

// Export actions
export const { addProduct, removeProduct, updateProduct } =
  productSlice.actions;

// Export selectors
export const { selectProducts, selectStatus } = productSlice.selectors;
