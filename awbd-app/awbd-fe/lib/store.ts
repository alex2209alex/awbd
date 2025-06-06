import type { Action, ThunkAction } from "@reduxjs/toolkit";
import { combineSlices, configureStore } from "@reduxjs/toolkit";
import { counterSlice } from "./features/counter/counterSlice";
import { quotesApiSlice } from "./features/quotes/quotesApiSlice";
import { producerSlice } from "./features/producer/slice";
import { ingredientSlice } from "./features/ingredient/slice";
import { productSlice } from "./features/product/slice";
import { cookerSlice } from "./features/cooker/slice";
import { courierSlice } from "./features/courier/slice";
import { clientSlice } from "./features/client/slice";
import { orderSlice } from "./features/order/slice";
import { userSlice } from "./features/user/slice";

// `combineSlices` automatically combines the reducers using
// their `reducerPath`s, therefore we no longer need to call `combineReducers`.
const rootReducer = combineSlices(
	counterSlice,
	quotesApiSlice,
	producerSlice,
	ingredientSlice,
	productSlice,
	cookerSlice,
	courierSlice,
	clientSlice,
	orderSlice,
	userSlice
);
// Infer the `RootState` type from the root reducer
export type RootState = ReturnType<typeof rootReducer>;

// `makeStore` encapsulates the store configuration to allow
// creating unique store instances, which is particularly important for
// server-side rendering (SSR) scenarios. In SSR, separate store instances
// are needed for each request to prevent cross-request state pollution.
export const makeStore = () => {
	return configureStore({
		reducer: rootReducer,
		// Adding the api middleware enables caching, invalidation, polling,
		// and other useful features of `rtk-query`.
		middleware: (getDefaultMiddleware) => {
			return getDefaultMiddleware().concat(quotesApiSlice.middleware);
		},
	});
};

// Infer the return type of `makeStore`
export type AppStore = ReturnType<typeof makeStore>;
// Infer the `AppDispatch` type from the store itself
export type AppDispatch = AppStore["dispatch"];
export type AppThunk<ThunkReturnType = void> = ThunkAction<
	ThunkReturnType,
	RootState,
	unknown,
	Action
>;
