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
import { selectUser } from "@/lib/features/user/slice";
import apiClient from "@/lib/apiClient";

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
      title="Orders List"
      headers={["Address", "Client", "Courier", "Price", "Status"]}
      roles={[]}
      fields={[
        { name: "address", type: "input" },
        { name: "client", type: "input" },
        { name: "courier", type: "input" },
        { name: "price", type: "input" },
        { name: "status", type: "input" },
        {
          name: "products",
          type: "array",
          fields: [
            {
              name: "id", type: "dropdown", endpoint: "/products"
            },
            { name: "quantity", type: "input" }
          ]
        },
      ]}
      fieldLabels={["Address", "Client", "Courier", "Price", "Status"]}
      detailsFields={[
        { name: "address", type: "input" },
        { name: "clientEmail", type: "input" },
        { name: "clientPhoneNumber", type: "input" },
        { name: "courierEmail", type: "input" },
        { name: "courierPhoneNumber", type: "input" },
        { name: "price", type: "input" },
        {
          name: "products",
          type: "array",
          fields: [
            {
              name: "id", type: "dropdown", endpoint: "/products"
            },
            { name: "quantity", type: "input" }
          ]
        },
      ]}
      filterFields={[
      ]}
      filterFieldsLabels={[]}
      detailsFieldLabels={["Address", "Client Email", "Client Phone Number", "Courier Email", "Courier Phone Number", "Price"]}
      editModalTitle="Edit Order"
      addModalTitle="Add Order"
      additionalButton={true}
    />
  );
};
