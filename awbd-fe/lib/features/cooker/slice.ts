import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";

// Define the shape of a Cooker
export interface Cooker {
  id: number;
  name: string;
  salary: number;
}

// Define the state structure
export interface CookerSliceState {
  cookers: Cooker[];
  status: "idle" | "loading" | "failed";
}

const initialState: CookerSliceState = {
  cookers: [
    { id: 1, name: "John Doe", salary: 5000 },
    { id: 2, name: "Jane Smith", salary: 5500 },
  ],
  status: "idle",
};

// Create the Redux slice
export const cookerSlice = createAppSlice({
  name: "cookers",
  initialState,
  reducers: (create) => ({
    addCooker: create.reducer((state, action: PayloadAction<Cooker>) => {
      state.cookers.push(action.payload);
    }),
    removeCooker: create.reducer((state, action: PayloadAction<number>) => {
      state.cookers = state.cookers.filter(
        (cooker) => cooker.id !== action.payload
      );
    }),
    updateCooker: create.reducer((state, action: PayloadAction<Cooker>) => {
      state.cookers = state.cookers.map((cooker) =>
        cooker.id === action.payload.id ? action.payload : cooker
      );
    }),
  }),
  selectors: {
    selectCookers: (state) => state.cookers,
    selectStatus: (state) => state.status,
  },
});

// Export actions
export const { addCooker, removeCooker, updateCooker } = cookerSlice.actions;

// Export selectors
export const { selectCookers, selectStatus } = cookerSlice.selectors;
