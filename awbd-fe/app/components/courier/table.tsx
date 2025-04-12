import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCourier,
  Courier,
  removeCourier,
  selectCouriers,
  selectStatus,
  updateCourier,
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
      apiEndpoint="api/couriers"
      title="Couriers List"
      headers={["Name", "Phone Number", "Salary"]}
      fields={[
        { name: "name", type: "input" },
        { name: "phoneNumber", type: "input" },
        { name: "salary", type: "input" },
      ]}
      fieldLabels={["Name", "Phone Number", "Salary"]}
      editModalTitle="Edit Courier"
      addModalTitle="Add Courier"
    />
  );
};
