import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addIngredient,
  Ingredient,
  removeIngredient,
  selectIngredients,
  selectStatus,
  updateIngredient,
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
      // items={[]}
      removeItem={removeIngredient}
      updateItem={updateIngredient}
      addItem={addIngredient}
      apiEndpoint="api/ingredients"
      title="Ingredients List"
      headers={["Name", "Price", "Calories", "Producer"]}
      fields={[
        { name: "name", type: "input" },
        { name: "price", type: "input" },
        { name: "calories", type: "input" },
        { name: "producer", type: "dropdown" },
      ]}
      fieldLabels={["Name", "Price", "Calories", "Producer"]}
      editModalTitle="Edit Ingredient"
      addModalTitle="Add Ingredient"
    />
  );
};
