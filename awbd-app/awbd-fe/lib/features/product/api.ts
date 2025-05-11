// A mock function to mimic making an async request for data
import apiClient from "@/lib/apiClient";
import { Product } from "./slice"; // Adjust the path

export const getProducts = async () => {
  const { data } = await apiClient.get("/products");
  return data
};

export const addProductApi = async (product: Product) => {
  try {
    await apiClient.post("/products", product);
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const deleteProductApi = async (productId: number) => {
  try {
    await apiClient.delete(`/products/${productId}`);
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const getProductsApi = async (params: any) => {
  try {
    const response = await apiClient.get("/products", {
      params,
    });
    return response.data as Product[]; // Type the response data
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const getProductByIdApi = async (id: number): Promise<Product> => {
  try {
    const response = await apiClient.get(`/products/${id}`)
    return response.data as Product;
  } catch (error: any) {
    throw error.response?.data || error;
  }
};

export const putProductsApi = async (
  productId: number,
  product: any,
) => {
  try {
    const response = await apiClient.put(
      `/products/${productId}`,
      product,
    );
    return response.data as Product; // Type the response data
  } catch (error: any) {
    throw error.response?.data || error;
  }
};
