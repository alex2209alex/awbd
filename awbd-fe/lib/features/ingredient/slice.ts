import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
import { Producer } from "../producer/slice";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Ingredient {
  id: number;
  name: string;
  price: number;
  calories: number;
  producer: Producer;
}

// Define the state structure
export interface IngredientSliceState {
  ingredients: Ingredient[];
  status: "idle" | "loading" | "failed";
}

const initialState: IngredientSliceState = {
  ingredients: [
    {
      id: 1,
      name: "Tomato",
      price: 2.5,
      calories: 20,
      producer: {
        id: 1,
        name: "Fresh Farms",
        address: "123 Green St, New York",
      },
    },
    {
      id: 2,
      name: "Lettuce",
      price: 1.2,
      calories: 5,
      producer: {
        id: 2,
        name: "Organic Suppliers",
        address: "456 Healthy Ave, California",
      },
    },
  ],
  status: "idle",
};

// Create the Redux slice
export const ingredientSlice = createAppSlice({
  name: "ingredients",
  initialState,
  reducers: (create) => ({
    addIngredient: create.reducer(
      (state, action: PayloadAction<Ingredient>) => {
        state.ingredients.push(action.payload);
      }
    ),
    removeIngredient: create.reducer((state, action: PayloadAction<number>) => {
      state.ingredients = state.ingredients.filter(
        (ingredient) => ingredient.id !== action.payload
      );
    }),
    updateIngredient: create.reducer(
      (state, action: PayloadAction<Ingredient>) => {
        state.ingredients = state.ingredients.map((ingredient: Ingredient) => {
          if (ingredient.id === action.payload.id) return action.payload;
          return ingredient;
        });
      }
    ),
  }),
  selectors: {
    selectIngredients: (state) => state.ingredients,
    selectStatus: (state) => state.status,
  },
});

export const { addIngredient, removeIngredient, updateIngredient } =
  ingredientSlice.actions;
export const { selectIngredients, selectStatus } = ingredientSlice.selectors;
