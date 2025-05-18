import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import { loginAPI } from "./api"; // Import loginAPI
import { jwtDecode } from "jwt-decode";
import { decode } from "punycode";

export interface UserSliceState {
	token: string
	id: string | undefined;
	jti: string | undefined;
	name: string;
	email: string;
	sub: string;
	isClient: boolean;
	isCooker: boolean;
	isCourier: boolean;
	isAdmin: boolean;
	exp: number;
	status: "idle" | "loading" | "failed" | "success";
}

const initialState: UserSliceState = {
	token: "",
	id: "",
	jti: "",
	name: "",
	email: "",
	sub: "",
	isClient: false,
	isCooker: false,
	isCourier: false,
	isAdmin: false,
	exp: 0,
	status: "idle",
};

export const userSlice = createAppSlice({
	name: "user",
	initialState,
	reducers: (create) => ({
		setUser: create.reducer((state, action: PayloadAction<UserSliceState>) => {
			Object.assign(state, action.payload);
		}),
		clearUser: create.reducer((state) => {
			console.log(">>>HEREEEEEEEEEE")
			Object.assign(state, { ...initialState });
			localStorage.removeItem("token_awbd");
		}),
		setUserByToken: create.reducer((state, action: PayloadAction<any>) => {
			// Object.assign(state, { ...initialState });
			console.log(">>>action. payload: ", action.payload)
			const token = action.payload.token;
			const decoded: any = jwtDecode(token);
			if (token) state.token = token;
			if (decoded?.email) state.email = decoded.email;
			if (decoded?.jti) state.jti = decoded.jti;
			if (decoded?.name) state.name = decoded.name;
			if (decoded?.sub) {
				state.sub = decoded.sub;
				if (decoded.sub === "CLIENT") state.isClient = true;
				else if (decoded.sub === "COURIER") state.isCourier = true;
				else if (decoded.sub === "COOK") state.isCooker = true;
				else if (decoded.sub === "RESTAURANT_ADMIN") state.isAdmin = true;
			}
			if (decoded?.exp) state.exp = decoded.exp;
		}),
		// setLoggedIn: create.reducer((state, action: PayloadAction<boolean>) => {
		// 	state.loggedIn = action.payload;
		// }),
		// signup: create.asyncThunk(
		// 	async (
		// 		userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
		// 	) => {
		// 		const response = await signupAPI(userData);
		// 		return response;
		// 	},
		// 	// {
		// 	// 	pending: (state) => {
		// 	// 		state.status = "loading";
		// 	// 		state.error = null;
		// 	// 	},
		// 	// 	fulfilled: (state, action) => {
		// 	// 		state.status = "success";
		// 	// 		Object.assign(state, action.payload);
		// 	// 	},
		// 	// 	rejected: (state, action) => {
		// 	// 		state.status = "failed";
		// 	// 		state.error = action.error.message || "Signup failed";
		// 	// 	},
		// 	// },
		// ),
		// createUser: create.asyncThunk(
		// 	async (
		// 		userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
		// 	) => {
		// 		const response = await createUserAPI(userData);
		// 		return response;
		// 	},
		// ),
		login: create.asyncThunk(
			async (credentials: { email: string; password: string }) => {
				const response = await loginAPI(credentials);
				return response;
			},
			{
				pending: (state) => {
					// You can add a loading status here if needed
					// state.status = "loading";
				},
				fulfilled: (state, action) => {
					// Set the user token (and optionally other fields from API)
					const token = action.payload.token;
					state.token = token;

					// Decode the token
					const decoded: any = jwtDecode(token);
					if (decoded?.email) state.email = decoded.email;
					if (decoded?.jti) state.jti = decoded.jti;
					if (decoded?.name) state.name = decoded.name;
					if (decoded?.sub) {
						state.sub = decoded.sub;
						if (decoded.sub === "CLIENT") state.isClient = true;
						else if (decoded.sub === "COURIER") state.isCourier = true;
						else if (decoded.sub === "COOK") state.isCooker = true;
						else if (decoded.sub === "RESTAURANT_ADMIN") state.isAdmin = true;
					}
					if (decoded?.exp) state.exp = decoded.exp;
					// Example if API returns user info:
					// Object.assign(state, action.payload);
					// state.loggedIn = true;
				},
				rejected: (state, action) => {
					// Handle login failure
					// state.status = "failed";
					// state.error = action.error.message ?? "Login failed";
				},
			}
		),
	}),
	selectors: {
		selectUser: (user) => user,
		selectToken: (user) => user?.token,
	},
});

export const { setUser, clearUser, setUserByToken, login } =
	userSlice.actions;
export const { selectUser, selectToken } =
	userSlice.selectors;
