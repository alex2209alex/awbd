import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addProducer,
  addProducerAsync,
  getProducerByIdAsync,
  getProducersAsync,
  Producer,
  removeProducer,
  removeProducerAsync,
  selectProducers,
  selectStatus,
  updateProducer,
  updateProducerAsync,
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
      addItemAsync={addProducerAsync}
      removeItemAsync={removeProducerAsync}
      fetchItems={getProducersAsync}
      fetchItemById={getProducerByIdAsync}
      updateItemAsync={updateProducerAsync}
      apiEndpoint="http://localhost:8080/producers"
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

// գ㜔齂㌪館䦄ޙ莀ᯤ�䖏嶊ㆡ킗ﺒ鹬볹朐鑊磘擑髛ᣛ⁞퍙偨�䰃
