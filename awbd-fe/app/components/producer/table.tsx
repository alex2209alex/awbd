import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addProducer,
  Producer,
  removeProducer,
  selectProducers,
  selectStatus,
  updateProducer,
} from "@/lib/features/producer/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const ProducersTable = () => {
  const producers = useAppSelector(selectProducers);
  return (
    <Table
      items={producers}
      removeItem={removeProducer}
      updateItem={updateProducer}
      addItem={addProducer}
      apiEndpoint="api/producers"
      title="Producers List"
      headers={["Name", "Address"]}
      fields={[
        { name: "name", type: "input" },
        { name: "address", type: "input" },
      ]}
      fieldLabels={["Name", "Address"]}
      editModalTitle="Edit Producer"
      addModalTitle="Add Producer"
    />
  );
};
