// A mock module to handle CRUD operations for  orders
import apiClient from "@/lib/apiClient";

// Define the Order type
export interface Order {
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
export const getOrders = async (): Promise<Order[]> => {
	const { data } = await apiClient.get("/orders");
	return data
};

// Add a new order (POST)
export const addOrderApi = async (order: Order): Promise<void> => {
	try {
		await apiClient.post("/orders", order, {
			headers: { "Content-Type": "application/json" },
		});
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Delete an order by ID (DELETE)
export const deleteOrderApi = async (orderId: number): Promise<void> => {
	try {
		await apiClient.delete(`/orders/${orderId}`);
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Fetch orders with query params (GET with params)
export const getOrdersApi = async (
	params: any,
): Promise<Order[]> => {
	try {
		const response = await apiClient.get("/orders", {
			params,
		});
		return response.data as Order[];
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

export const getOrderByIdApi = async (id: number): Promise<Order> => {
	try {
		const response = await apiClient.get(`/orders/${id}`)
		return response.data as Order;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};

// Update an existing order (PUT)
export const putOrderApi = async (
	orderId: number,
	order: Partial<Order>,
): Promise<Order> => {
	try {
		const response = await apiClient.put(
			`/orders/${orderId}`,
			order,
			{ headers: { "Content-Type": "application/json" } },
		);
		return response.data as Order;
	} catch (error: any) {
		throw error.response?.data || error;
	}
};
