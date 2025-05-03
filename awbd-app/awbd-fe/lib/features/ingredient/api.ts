// A mock function to mimic making an async request for data
import axios from "axios";
import { Ingredient } from "./slice"; // Adjust the path

export const getIngredients = async () => {
	const response = await fetch("http://localhost:8080/ingredients", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: Ingredient[] } = await response.json(); // Expect an array of Ingredients

	return result;
};

export const addIngredientApi = async (ingredient: Ingredient) => {
	try {
		await axios.post("http://localhost:8080/ingredients", ingredient);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteIngredientApi = async (ingredientId: number) => {
	try {
		await axios.delete(`http://localhost:8080/ingredients/${ingredientId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getIngredientsApi = async (params: any) => {
	try {
		const response = await axios.get("http://localhost:8080/ingredients", {
			params,
		});
		return response.data as Ingredient[]; // Type the response data
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const putIngredientsApi = async (
	ingredientId: number,
	ingredient: any,
) => {
	try {
		const response = await axios.put(
			`http://localhost:8080/ingredients/${ingredientId}`,
			ingredient,
		);
		return response.data as Ingredient; // Type the response data
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
