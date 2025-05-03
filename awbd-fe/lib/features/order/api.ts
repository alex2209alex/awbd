// A mock module to handle CRUD operations for online orders
import axios from "axios";

// Define the OnlineOrder type
export interface OnlineOrder {
	id?: number;
	customerName: string;
	address: string;
	items: Array<{
		productId: number;
		quantity: number;
	}>;
	totalPrice: number;
	status: "pending" | "confirmed" | "preparing" | "delivered" | "cancelled";
}

// Fetch all orders (GET)
export const getOnlineOrders = async (): Promise<OnlineOrder[]> => {
	const response = await fetch("http://localhost:8080/orders", {
		method: "GET",
		headers: { "Content-Type": "application/json" },
	});
	const result: { data: OnlineOrder[] } = await response.json();
	return result.data;
};

// Add a new order (POST)
export const addOnlineOrderApi = async (order: OnlineOrder): Promise<void> => {
	try {
		await axios.post("http://localhost:8080/orders", order, {
			headers: { "Content-Type": "application/json" },
		});
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Delete an order by ID (DELETE)
export const deleteOnlineOrderApi = async (orderId: number): Promise<void> => {
	try {
		await axios.delete(`http://localhost:8080/orders/${orderId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Fetch orders with query params (GET with params)
export const getOnlineOrdersApi = async (
	params: any,
): Promise<OnlineOrder[]> => {
	try {
		const response = await axios.get("http://localhost:8080/orders", {
			params,
		});
		return response.data as OnlineOrder[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Update an existing order (PUT)
export const putOnlineOrderApi = async (
	orderId: number,
	order: Partial<OnlineOrder>,
): Promise<OnlineOrder> => {
	try {
		const response = await axios.put(
			`http://localhost:8080/orders/${orderId}`,
			order,
			{ headers: { "Content-Type": "application/json" } },
		);
		return response.data as OnlineOrder;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
