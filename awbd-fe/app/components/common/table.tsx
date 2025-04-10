import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { EditModal } from "../common/editModal";
import axios from "axios";
import { Pagination } from "./pagination";
import { Filter } from "./filter";

export const Table = ({
  items,
  removeItem,
  updateItem,
  addItem,
  apiEndpoint,
  title,
  headers,
  fields,
  fieldLabels,
  editModalTitle,
  addModalTitle,
}: any) => {
  const [currentItem, setCurrentItem] = useState<any>(null);
  const dispatch = useDispatch();
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);

  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [pageItems, setPageItems] = useState<any[]>(items);

  const [fetchItemsFlag, setFetchItemsFlag] = useState<boolean>(false);

  const [sortColumn, setSortColumn] = useState<string | null>(null);
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");

  const [filter, setFilter] = useState<{ [key: string]: string }>({});
  // const [totalItems, setTotalItems] = useState(0);

  const fetchItems = async (page: number) => {
    try {
      const params: any = {
        offset: page,
        limit: itemsPerPage,
        ...(sortColumn
          ? { sort: `${sortOrder === "asc" ? "+" : "-"}${sortColumn}` }
          : {}),
      };
      Object.keys(filter).forEach((attr: string) => {
        params[attr] = filter[attr];
      });
      console.log(">>>params: ", params);
      const response = await axios.get(apiEndpoint, { params });
      setPageItems(response.data);
      // setTotalItems(response.data.total); // Assuming the backend returns the total number of items
    } catch (error) {
      console.error("Error fetching items:", error);
    }
  };

  const handleEdit = (producer: any) => {
    setCurrentItem(producer);
    setShowEditModal(true);
  };

  const handleDelete = (id: number) => {
    dispatch(removeItem(id));
    setFetchItemsFlag(true);
  };

  const handleSaveEdit = (updatedState: any) => {
    dispatch(updateItem(updatedState));
  };

  const handleAddProducer = () => {
    setCurrentItem(null);
    setShowAddModal(true);
  };

  const handleSaveAdd = async (newState: any) => {
    // const { data: newProducer } = await axios.post(apiEndpoint, newState);
    dispatch(addItem(newState));
    setShowAddModal(false);
    setFetchItemsFlag(true);
  };

  const handleSort = (column: string) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
    setFetchItemsFlag(true);
  };

  const handlePaginate = (pageNumber: number) => {
    setCurrentPage(pageNumber);
    setFetchItemsFlag(true);
  };

  const handleFilterChange = (field: string, value: string) => {
    setFilter((prev) => ({ ...prev, [field]: value || "" }));
    setFetchItemsFlag(true);
  };

  useEffect(() => {
    if (!fetchItemsFlag) return;
    fetchItems(currentPage);
    setFetchItemsFlag(false);
  }, [fetchItemsFlag]);

  return (
    <div className="container mt-5">
      <h1 className="mb-4 text-center">{title}</h1>

      <div className="row mb-4">
        {fields.map(({ name }: { name: string }, index: number) => (
          <div className="col-md-4" key={name}>
            <Filter
              fieldName={name}
              fieldLabel={fieldLabels[index]}
              value={filter[name] || ""}
              onFilterChange={handleFilterChange}
            />
          </div>
        ))}
      </div>

      <table className="table table-striped table-bordered w-100">
        <thead className="thead-dark">
          <tr>
            {headers.map((header: string, index: number) => (
              <th
                key={header}
                className="fs-4 p-3"
                style={{ cursor: "pointer" }}
                onClick={() => handleSort(fields[index].name)}
              >
                {header}
                {sortColumn === fields[index].name &&
                  (sortOrder === "asc" ? " 🔼" : " 🔽")}
              </th>
            ))}
            <th className="fs-4 p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {pageItems.map((item: any) => (
            <tr key={item.id}>
              {fields.map(({ name }: { name: string }) => (
                <td key={name} className="fs-4 p-3">
                  {item[name] && item[name].name ? item[name].name : item[name]}
                </td>
              ))}
              <td className="fs-4 p-3">
                <button
                  className="btn btn-warning me-2"
                  onClick={() => handleEdit(item)}
                >
                  Edit
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(item.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="text-center mt-4">
        <button onClick={handleAddProducer} className="btn btn-primary">
          New
        </button>
      </div>

      <Pagination
        handlePaginate={handlePaginate}
        currentPage={currentPage}
        totalPages={2}
      />

      <EditModal
        isOpen={showEditModal || showAddModal}
        onClose={() => {
          setShowEditModal(false);
          setShowAddModal(false);
        }}
        onSave={showEditModal ? handleSaveEdit : handleSaveAdd}
        title={showEditModal ? editModalTitle : addModalTitle}
        initialState={currentItem}
        labels={fieldLabels}
        properties={fields}
      />
    </div>
  );
};
