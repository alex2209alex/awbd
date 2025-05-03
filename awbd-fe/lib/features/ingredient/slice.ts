import { createAppSlice } from "@/lib/createAppSlice";
import type { AppThunk } from "@/lib/store";
import type { PayloadAction } from "@reduxjs/toolkit";
import { Producer } from "../producer/slice";
import {
	addIngredientApi,
	deleteIngredientApi,
	getIngredientsApi,
	putIngredientsApi,
} from "./api";
// import { fetchCount } from "./counterAPI";

// Define the shape of a Producer
export interface Ingredient {
	id: number;
	name: string;
	price: number;
	calories: number;
	producer?: any;
	producerId?: number;
}

// Define the state structure
export interface IngredientPage {
	items: Ingredient[];
	pagination: any;
}

// Define the state structure
export interface IngredientSliceState {
	ingredients: IngredientPage;
	status: "idle" | "loading" | "failed";
}

const initialState: IngredientSliceState = {
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
	ingredients: {
		items: [],
		pagination: {},
	},
	status: "idle",
};

// Create the Redux slice
export const ingredientSlice = createAppSlice({
	name: "ingredients",
	initialState,
	reducers: (create) => ({
		getIngredientsAsync: create.asyncThunk(async (params: any) => {
			return await getIngredientsApi(params);
		}),

		addIngredient: create.reducer(
			(state, action: PayloadAction<Ingredient>) => {
				if (action.payload.producer?.id)
					action.payload.producer = action.payload.producer.id;
				state.ingredients.items.push(action.payload);
			},
		),
		removeIngredient: create.reducer((state, action: PayloadAction<number>) => {
			state.ingredients.items = state.ingredients.items.filter(
				(ingredient) => ingredient.id !== action.payload,
			);
		}),
		updateIngredient: create.reducer(
			(state, action: PayloadAction<Ingredient>) => {
				if (action.payload.producer?.id)
					action.payload.producer = action.payload.producer.id;
				state.ingredients.items = state.ingredients.items.map(
					(ingredient: Ingredient) => {
						if (ingredient.id === action.payload.id) return action.payload;
						return ingredient;
					},
				);
			},
		),
		addIngredientAsync: create.asyncThunk(async (ingredient: Ingredient) => {
			// console.log(">>>>INGREDIENT in add async: ", ingredient)
			if (ingredient?.producer?.id)
				ingredient.producer = ingredient?.producer?.id;

			console.log(">>>AFTER IF ingredient in add async: ", ingredient);
			const newIngredient = { ...ingredient, producerId: ingredient.producer };
			delete newIngredient.producer;
			console.log(">>>ingredient in add async: ", newIngredient);

			return await addIngredientApi(newIngredient);
		}),
		removeIngredientAsync: create.asyncThunk(async (ingredientId: number) => {
			return await deleteIngredientApi(ingredientId);
		}),
		updateIngredientAsync: create.asyncThunk(async (ingredient: any) => {
			if (ingredient.producer?.id) ingredient.producer = ingredient.producer.id;
			ingredient.producerId = ingredient.producer;
			return await putIngredientsApi(ingredient.id, ingredient);
		}),
	}),
	selectors: {
		selectIngredients: (state) => state.ingredients,
		selectStatus: (state) => state.status,
	},
});

export const {
	getIngredientsAsync,
	addIngredient,
	removeIngredient,
	updateIngredient,
	addIngredientAsync,
	removeIngredientAsync,
	updateIngredientAsync,
} = ingredientSlice.actions;
export const { selectIngredients, selectStatus } = ingredientSlice.selectors;
