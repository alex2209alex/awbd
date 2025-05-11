// api.ts
import apiClient from "@/lib/apiClient";
import type { UserSliceState } from "./slice";

// export async function signupAPI(
// 	userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
// ) {
// 	const response = await fetch("/api/signup", {
// 		method: "POST",
// 		headers: {
// 			"Content-Type": "application/json",
// 		},
// 		body: JSON.stringify(userData),
// 	});

// 	if (!response.ok) {
// 		const errorData = await response.json();
// 		throw new Error(errorData.message || "Signup failed");
// 	}

// 	return response.json();
// }

export async function loginAPI(credentials: {
	email: string;
	password: string;
}) {
	const { data } = await apiClient.post("/users/login", credentials);

	if (!data) {
		const errorData = data;
		throw new Error(errorData.message || "Login failed");
	}

	return data;
}

// export async function createUserAPI(
// 	userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
// ) {
// 	const response = await fetch("/api/createUser", {
// 		method: "POST",
// 		headers: {
// 			"Content-Type": "application/json",
// 		},
// 		body: JSON.stringify(userData),
// 	});

// 	if (!response.ok) {
// 		const errorData = await response.json();
// 		throw new Error(errorData.message || "Create User failed");
// 	}

// 	return response.json();
// }
