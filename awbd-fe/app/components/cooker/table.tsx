import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCooker,
  addCookerAsync,
  Cooker,
  getCookersAsync,
  removeCooker,
  removeCookerAsync,
  selectCookers,
  selectStatus,
  updateCooker,
  updateCookerAsync,
} from "@/lib/features/cooker/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const CookersTable = () => {
  const producers = useAppSelector(selectCookers);
  return (
    <Table
      items={producers}
      removeItem={removeCooker}
      updateItem={updateCooker}
      addItem={addCooker}
      addItemAsync={addCookerAsync}
      removeItemAsync={removeCookerAsync}
      fetchItems={getCookersAsync}
      updateItemAsync={updateCookerAsync}
      apiEndpoint="http://localhost:8080/cooks"

      title="Cookers List"
      headers={["Email", "Password", "Name", "Salary", "Products"]}
      fields={[
        { name: "email", type: "input" },
        { name: "password", type: "input", inputType: "password" },
        { name: "name", type: "input" },
        { name: "salary", type: "input", dataType: "number" },
        {
          name: "products",
          type: "array",
          fields: [
            {
              name: "id", type: "dropdown", endpoint: "http://localhost:8080/products/search"
            },
          ]
        },
      ]}
      fieldLabels={["Email", "Password", "Name", "Salary", "Products"]}
      editModalTitle="Edit Cooker"
      addModalTitle="Add Cooker"
    />
  );
};
