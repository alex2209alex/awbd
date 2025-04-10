import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Producer {
  id: number;
  name: string;
  address: string;
}

// Define the state structure
export interface ProducerSliceState {
  producers: Producer[];
  status: "idle" | "loading" | "failed";
}

const initialState: ProducerSliceState = {
  producers: [
    { id: 1, name: "Fresh Farms", address: "123 Green St, New York" },
    {
      id: 2,
      name: "Organic Suppliers",
      address: "456 Healthy Ave, California",
    },
  ],
  status: "idle",
};

// Create the Redux slice
export const producerSlice = createAppSlice({
  name: "producers",
  initialState,
  reducers: (create) => ({
    addProducer: create.reducer((state, action: PayloadAction<Producer>) => {
      state.producers.push(action.payload);
    }),
    removeProducer: create.reducer((state, action: PayloadAction<number>) => {
        if(action.payload)
      state.producers = state.producers.filter(
        (producer) => producer.id !== action.payload
      );
    }),
    updateProducer: create.reducer((state, action: PayloadAction<Producer>) => {
      state.producers = state.producers.map((producer: Producer) => {
        if (producer.id === action.payload.id) return action.payload;
        return producer;
      });
    }),
  }),
  selectors: {
    selectProducers: (state) => state.producers,
    selectStatus: (state) => state.status,
  },
});

// Export actions
export const { addProducer, removeProducer, updateProducer } =
  producerSlice.actions;

// Export selectors
export const { selectProducers, selectStatus } = producerSlice.selectors;
