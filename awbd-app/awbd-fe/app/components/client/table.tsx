import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  addClient,
  addClientAsync,
  Client,
  getClientsAsync,
  removeClient,
  removeClientAsync,
  selectClients,
  updateClient,
  updateClientAsync,
} from "@/lib/features/client/slice";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Table } from "../common/table";

export const ClientsTable = () => {
  const producers = useAppSelector(selectClients);
  return (
    <Table
      items={producers}
      removeItem={removeClient}
      updateItem={updateClient}
      addItem={addClient}
      addItemAsync={addClientAsync}
      removeItemAsync={removeClientAsync}
      fetchItems={getClientsAsync}
      updateItemAsync={updateClientAsync}
      apiEndpoint="http://localhost:8080/clients"
      title="Clients List"
      headers={["Email", "Password", "Name", "Phone Number"]}
      fields={[
        { name: "email", type: "input" },
        { name: "password", type: "input", inputType: "password" },
        { name: "name", type: "input" },
        { name: "phoneNumber", type: "input" },
      ]}
      fieldLabels={["Email", "Password", "Name", "Phone Number"]}
      editModalTitle="Edit Client"
      addModalTitle="Add Client"
    />
  );
};
