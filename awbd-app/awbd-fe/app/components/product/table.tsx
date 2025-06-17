import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addProduct,
  addProductAsync,
  getProductByIdAsync,
  getProductsAsync,
  Product,
  removeProduct,
  removeProductAsync,
  selectProducts,
  selectStatus,
  updateProduct,
  updateProductAsync,
} from "@/lib/features/product/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const ProductsTable = () => {
  const producers = useAppSelector(selectProducts);
  return (
    <Table
      items={producers}
      removeItem={removeProduct}
      updateItem={updateProduct}
      addItem={addProduct}
      addItemAsync={addProductAsync}
      removeItemAsync={removeProductAsync}
      fetchItems={getProductsAsync}
      fetchItemById={getProductByIdAsync}
      updateItemAsync={updateProductAsync}
      title="Products List"
      headers={["Name", "Price", "Description"]}
      filterFields={[
        { name: "name", type: "input" },
        { name: "description", type: "input" },
      ]}
      filterFieldsLabels={["Name", "Description"]}
      fields={[
        { name: "name", type: "input" },
        { name: "price", type: "input" },
        { name: "description", type: "input" },
        {
          name: "ingredients",
          type: "array",
          fields: [
            {
              name: "id", type: "dropdown", endpoint: "/ingredients"
            },
            { name: "quantity", type: "input" }
          ]
        },
      ]}
      fieldLabels={["Name", "Price", "Description", "Ingredients"]}
      editModalTitle="Edit Product"
      addModalTitle="Add Product"
      addToCart={true}
    />
  );
};
