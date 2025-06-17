import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCooker,
  addCookerAsync,
  Cooker,
  getCookerByIdAsync,
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
      fetchItemById={getCookerByIdAsync}
      updateItemAsync={updateCookerAsync}
      title="Cookers List"
      headers={["Email", "Password", "Name", "Salary"]}
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
              name: "id", type: "dropdown", endpoint: "/products"
            },
          ]
        },
      ]}
      fieldLabels={["Email", "Password", "Name", "Salary"]}
      filterFields={[
        { name: "email", type: "input" },
        { name: "name", type: "input" },
      ]}
      filterFieldsLabels={["Email", "Name"]}
      editModalTitle="Edit Cooker"
      addModalTitle="Add Cooker"
    />
  );
};
