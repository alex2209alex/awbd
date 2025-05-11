import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addOrder,
  addOrderAsync,
  getOrderByIdAsync,
  getOrdersAsync,
  order,
  removeOrder,
  removeOrderAsync,
  selectOrders,
  selectStatus,
  updateOrder,
  updateOrderAsync,
} from "@/lib/features/order/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const OrdersTable = () => {
  const orders = useAppSelector(selectOrders);
  return (
    <Table
      items={orders}
      removeItem={removeOrder}
      updateItem={updateOrder}
      addItem={addOrder}
      addItemAsync={addOrderAsync}
      removeItemAsync={removeOrderAsync}
      fetchItems={getOrdersAsync}
      fetchItemById={getOrderByIdAsync}
      updateItemAsync={updateOrderAsync}
      apiEndpoint="http://localhost:8080/orders"
      title="Orders List"
      headers={["Address", "Products"]}
      fields={[
        { name: "address", type: "input" },
        {
          name: "products",
          type: "array",
          fields: [
            {
              name: "id", type: "dropdown", endpoint: "http://localhost:8080/products/search"
            },
            { name: "quantity", type: "input" }
          ]
        },
      ]}
      fieldLabels={["Address", "Products"]}
      editModalTitle="Edit Order"
      addModalTitle="Add Order"
    />
  );
};
