import { createAppSlice } from "@/lib/createAppSlice";
import type { PayloadAction } from "@reduxjs/toolkit";
import { signupAPI, loginAPI, createUserAPI } from "./api"; // Import loginAPI

export interface UserSliceState {
	id: string | undefined;
	name: string;
	phoneNumber: string;
	address: string;
	email: string;
	status: "idle" | "loading" | "failed" | "success";
	error: string | null;
	loggedIn: boolean;
}

const initialState: UserSliceState = {
	id: "",
	name: "",
	phoneNumber: "",
	address: "",
	email: "",
	status: "idle",
	error: null,
	loggedIn: false,
};

export const userSlice = createAppSlice({
	name: "user",
	initialState,
	reducers: (create) => ({
		setUser: create.reducer((state, action: PayloadAction<UserSliceState>) => {
			Object.assign(state, action.payload);
		}),
		clearUser: create.reducer((state) => {
			Object.assign(state, { ...initialState, loggedIn: false }); // Reset to initialState, keep loggedIn false
		}),
		setLoggedIn: create.reducer((state, action: PayloadAction<boolean>) => {
			state.loggedIn = action.payload;
		}),
		signup: create.asyncThunk(
			async (
				userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
			) => {
				const response = await signupAPI(userData);
				return response;
			},
			{
				pending: (state) => {
					state.status = "loading";
					state.error = null;
				},
				fulfilled: (state, action) => {
					state.status = "success";
					Object.assign(state, action.payload);
				},
				rejected: (state, action) => {
					state.status = "failed";
					state.error = action.error.message || "Signup failed";
				},
			},
		),
		createUser: create.asyncThunk(
			async (
				userData: Omit<UserSliceState, "status" | "error" | "loggedIn">,
			) => {
				const response = await createUserAPI(userData);
				return response;
			},
			{
				pending: (state) => {
					state.status = "loading";
					state.error = null;
				},
				fulfilled: (state, action) => {
					state.status = "success";
					Object.assign(state, action.payload);
				},
				rejected: (state, action) => {
					state.status = "failed";
					state.error = action.error.message || "Signup failed";
				},
			},
		),
		login: create.asyncThunk(
			async (credentials: { email: string; password: string }) => {
				const response = await loginAPI(credentials);
				return response;
			},
			{
				pending: (state) => {
					state.status = "loading";
					state.error = null;
				},
				fulfilled: (state, action) => {
					state.status = "success";
					Object.assign(state, action.payload);
					state.loggedIn = true;
				},
				rejected: (state, action) => {
					state.status = "failed";
					state.error = action.error.message || "Login failed";
					state.loggedIn = false;
				},
			},
		),
	}),
	selectors: {
		selectUser: (user) => user,
		selectUserStatus: (user) => user.status,
		selectUserError: (user) => user.error,
		selectLoggedIn: (user) => user.loggedIn,
	},
});

export const { setUser, clearUser, signup, createUser, login, setLoggedIn } =
	userSlice.actions;
export const { selectUser, selectUserStatus, selectUserError, selectLoggedIn } =
	userSlice.selectors;
