import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addProduct,
  Product,
  removeProduct,
  selectProducts,
  selectStatus,
  updateProduct,
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
      apiEndpoint="api/producets"
      title="Products List"
      headers={["Name", "Price", "Description"]}
      fields={[
        { name: "name", type: "input" },
        { name: "price", type: "input" },
        { name: "description", type: "input" },
      ]}
      fieldLabels={["Name", "Price", "Description"]}
      editModalTitle="Edit Product"
      addModalTitle="Add Product"
    />
  );
};
