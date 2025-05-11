// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";
import { Ingredient } from "./slice"; // Adjust the path

export const getIngredients = async () => {
	const { data } = await apiClient.get("/ingredients");
	return data;
};

export const addIngredientApi = async (ingredient: Ingredient) => {
	try {
		await apiClient.post("/ingredients", ingredient);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const deleteIngredientApi = async (ingredientId: number) => {
	try {
		await apiClient.delete(`/ingredients/${ingredientId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getIngredientsApi = async (params: any) => {
	try {
		const response = await apiClient.get("/ingredients", {
			params,
		});
		return response.data as Ingredient[]; // Type the response data
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getIngredientByIdApi = async (id: number): Promise<Ingredient> => {
	try {
		const response = await apiClient.get(`/ingredients/${id}`)
		return response.data as Ingredient;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};


export const putIngredientsApi = async (
	ingredientId: number,
	ingredient: any,
) => {
	try {
		const response = await apiClient.put(
			`/ingredients/${ingredientId}`,
			ingredient,
		);
		return response.data as Ingredient; // Type the response data
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
