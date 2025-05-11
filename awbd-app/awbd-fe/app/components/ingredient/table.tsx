import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addIngredient,
  addIngredientAsync,
  getIngredientByIdAsync,
  getIngredientsAsync,
  Ingredient,
  removeIngredient,
  removeIngredientAsync,
  selectIngredients,
  selectStatus,
  updateIngredient,
  updateIngredientAsync,
} from "@/lib/features/ingredient/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const IngredientsTable = () => {
  const ingredients = useAppSelector(selectIngredients);
  console.log(ingredients);
  return (
    <Table
      items={ingredients}
      removeItem={removeIngredient}
      updateItem={updateIngredient}
      addItem={addIngredient}
      addItemAsync={addIngredientAsync}
      removeItemAsync={removeIngredientAsync}
      fetchItems={getIngredientsAsync}
      fetchItemById={getIngredientByIdAsync}
      updateItemAsync={updateIngredientAsync}
      apiEndpoint="http://localhost:8080/ingredients"

      title="Ingredients List"
      headers={["Name", "Price", "Calories", "Producer"]}
      fields={[
        { name: "name", type: "input" },
        { name: "price", type: "input" },
        { name: "calories", type: "input" },
        { name: "producer", type: "dropdown", endpoint: "/producers/search" },
      ]}
      fieldLabels={["Name", "Price", "Calories", "Producer"]}
      editModalTitle="Edit Ingredient"
      addModalTitle="Add Ingredient"
    />
  );
};
