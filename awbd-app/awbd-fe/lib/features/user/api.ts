// api.ts

import type { UserSliceState } from "./slice";

export async function signupAPI(
	userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
) {
	const response = await fetch("/api/signup", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(userData),
	});

	if (!response.ok) {
		const errorData = await response.json();
		throw new Error(errorData.message || "Signup failed");
	}

	return response.json();
}

export async function loginAPI(credentials: {
	email: string;
	password: string;
}) {
	const response = await fetch("/api/login", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(credentials),
	});

	if (!response.ok) {
		const errorData = await response.json();
		throw new Error(errorData.message || "Login failed");
	}

	return response.json();
}

export async function createUserAPI(
	userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
) {
	const response = await fetch("/api/createUser", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(userData),
	});

	if (!response.ok) {
		const errorData = await response.json();
		throw new Error(errorData.message || "Create User failed");
	}

	return response.json();
}
