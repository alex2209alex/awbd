// A mock function to mimic making an async request for data
import axios from "axios";
import { Ingredient } from "./slice"; // Adjust the path

export const getProducts = async () => {
  const response = await fetch("http://localhost:8080/products", {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  const result: { data: Product[] } = await response.json(); // Expect an array of Products

  return result;
};

export const addProductApi = async (product: Product) => {
  try {
    await axios.post("http://localhost:8080/products", product);
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const deleteProductApi = async (productId: number) => {
  try {
    await axios.delete(`http://localhost:8080/products/${productId}`);
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const getProductsApi = async (params: any) => {
  try {
    const response = await axios.get("http://localhost:8080/products", {
      params,
    });
    return response.data as Product[]; // Type the response data
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const putProductsApi = async (
  productId: number,
  product: any,
) => {
  try {
    const response = await axios.put(
      `http://localhost:8080/products/${productId}`,
      product,
    );
    return response.data as Product; // Type the response data
  } catch (error: any) {
    throw error.response?.data || error;
  }
};
