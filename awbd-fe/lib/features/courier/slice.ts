import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";

// Define the shape of a Courier
export interface Courier {
  id: number;
  name: string;
  phoneNumber: string;
  salary: number;
}

// Define the state structure
export interface CourierSliceState {
  couriers: Courier[];
  status: "idle" | "loading" | "failed";
}

const initialState: CourierSliceState = {
  couriers: [
    { id: 1, name: "John Doe", phoneNumber: "+1234567890", salary: 3500 },
    { id: 2, name: "Jane Smith", phoneNumber: "+0987654321", salary: 4000 },
  ],
  status: "idle",
};

// Create the Redux slice
export const courierSlice = createAppSlice({
  name: "couriers",
  initialState,
  reducers: (create) => ({
    addCourier: create.reducer((state, action: PayloadAction<Courier>) => {
      state.couriers.push(action.payload);
    }),
    removeCourier: create.reducer((state, action: PayloadAction<number>) => {
      state.couriers = state.couriers.filter(
        (courier) => courier.id !== action.payload
      );
    }),
    updateCourier: create.reducer((state, action: PayloadAction<Courier>) => {
      state.couriers = state.couriers.map((courier) =>
        courier.id === action.payload.id ? action.payload : courier
      );
    }),
  }),
  selectors: {
    selectCouriers: (state) => state.couriers,
    selectStatus: (state) => state.status,
  },
});

// Export actions
export const { addCourier, removeCourier, updateCourier } =
  courierSlice.actions;

// Export selectors
export const { selectCouriers, selectStatus } = courierSlice.selectors;
