import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addCooker,
  Cooker,
  removeCooker,
  selectCookers,
  selectStatus,
  updateCooker,
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
      apiEndpoint="api/cookers"
      title="Cookers List"
      headers={["Name", "Salary"]}
      fields={[
        { name: "name", type: "input" },
        { name: "salary", type: "input" },
      ]}
      fieldLabels={["Name", "Salary"]}
      editModalTitle="Edit Cooker"
      addModalTitle="Add Cooker"
    />
  );
};
