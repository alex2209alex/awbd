import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCourier,
  addCourierAsync,
  Courier,
  getCourierByIdAsync,
  getCouriersAsync,
  removeCourier,
  removeCourierAsync,
  selectCouriers,
  selectStatus,
  updateCourier,
  updateCourierAsync,
} from "@/lib/features/courier/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const CouriersTable = () => {
  const producers = useAppSelector(selectCouriers);
  return (
    <Table
      items={producers}
      removeItem={removeCourier}
      updateItem={updateCourier}
      addItem={addCourier}
      addItemAsync={addCourierAsync}
      removeItemAsync={removeCourierAsync}
      fetchItems={getCouriersAsync}
      fetchItemById={getCourierByIdAsync}
      updateItemAsync={updateCourierAsync}
      title="Couriers List"
      headers={["Email", "Password", "Name", "Phone Number", "Salary"]}
      fields={[
        { name: "email", type: "input" },
        { name: "password", type: "input", inputType: "password" },
        { name: "name", type: "input" },
        { name: "phoneNumber", type: "input" },
        { name: "salary", type: "input" },
      ]}
      fieldLabels={["Email", "Password", "Name", "Phone Number", "Salary"]}
      filterFields={[
        { name: "email", type: "input" },
        { name: "name", type: "input" },
      ]}
      filterFieldsLabels={["Email", "Name"]}
      editModalTitle="Edit Courier"
      addModalTitle="Add Courier"
    />
  );
};
