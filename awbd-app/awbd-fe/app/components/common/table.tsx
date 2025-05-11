import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { EditModal } from "../common/editModal";
import { Pagination } from "./pagination";
import { Filter } from "./filter";
import { updateProducerAsync } from "@/lib/features/producer/slice";
import { selectUser } from "@/lib/features/user/slice";

export const Table = ({
  items,
  fetchItems,
  fetchItemById,
  removeItem,
  removeItemAsync,
  updateItem,
  addItem,
  addItemAsync,
  updateItemAsync,
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
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(5);
  const [pageItems, setPageItems] = useState<any[]>(items);

  const [fetchItemsFlag, setFetchItemsFlag] = useState(false); // Simple boolean toggle

  const [sortColumn, setSortColumn] = useState<string | null>(null);
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");

  const [filter, setFilter] = useState<{ [key: string]: string }>({});

  const itemsArray = pageItems?.items || [];
  const pagination = pageItems?.pagination;

  const user = useSelector(selectUser);

  const getItems = (page: number) => {
    try {
      const params: any = {
        offset: page,
        limit: itemsPerPage,
        ...(sortColumn ? { sort: `${sortOrder === "asc" ? "+" : "-"}${sortColumn}` } : {}),
      };
      Object.keys(filter).forEach((attr: string) => {
        params[attr] = filter[attr];
      });
      console.log(">>>params: ", params);
      dispatch(fetchItems(params))
        .then((response: any) => {
          console.log(">>>response: ", response);
          if (response.payload) setPageItems(response.payload);
        })
        .catch((error: any) => {
          console.error("Error fetching items:", error);
        });
    } catch (error) {
      console.error("Error fetching items:", error);
    }
  };

  const handleEdit = async (item: any, readOnly = false) => {
    const { payload } = await dispatch(fetchItemById(item?.id))
    console.log(">>>fetchedItem: ", payload)
    setCurrentItem(payload);
    !readOnly ? setShowEditModal(true) : setShowDetailsModal(true);
  };

  const handleDelete = async (id: number) => {
    await dispatch(removeItem(id));
    await dispatch(removeItemAsync(id));
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  const handleSaveEdit = async (updatedState: any) => {
    console.log(">>>updatedState: ", updatedState)
    await dispatch(updateItem(updatedState));
    await dispatch(updateItemAsync(updatedState));
    setShowEditModal(false);
    setFetchItemsFlag((prev) => !prev);
  };

  const handleAddProducer = () => {
    setCurrentItem(null);
    setShowAddModal(true);
  };

  const handleSaveAdd = async (newState: any) => {
    console.log(">>>newState ADD: ", newState)
    await dispatch(addItem(newState));
    await dispatch(addItemAsync(newState));
    setShowAddModal(false);
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  const handleSort = (column: string) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  const handlePaginate = (pageNumber: number) => {
    setCurrentPage(pageNumber);
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  const handleItemsPerPageChange = (itemsPerPage: number) => {
    setItemsPerPage(itemsPerPage);
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  const handleFilterChange = (field: string, value: string) => {
    setFilter((prev) => ({ ...prev, [field]: value || "" }));
    setFetchItemsFlag((prev) => !prev); // Toggle boolean
  };

  useEffect(() => {
    getItems(currentPage);
  }, [fetchItemsFlag, currentPage, itemsPerPage, sortColumn, sortOrder, filter]);

  console.log(">>pageItems: ", pageItems);

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
                {sortColumn === fields[index].name && (sortOrder === "asc" ? " 🔼" : " 🔽")}
              </th>
            ))}
            <th className="fs-4 p-3">Actions</th>
          </tr>
        </thead>
        <tbody>
          {itemsArray.map((item: any) => (
            <tr key={item.id}>
              {fields.map(({ name, type }: { name: string, type: string }) => {
                if (type === "array") return null;
                return (<td key={name} className="fs-4 p-3">
                  {item[name] && item[name].name ? item[name].name : item[name]}
                </td>
                )
              }
              )}
              <td className="fs-4 p-3">
                <button className="btn btn-warning me-2" onClick={async () => await handleEdit(item)}>
                  Edit
                </button>
                <button className="btn btn-danger" onClick={async () => await handleDelete(item.id)}>
                  Delete
                </button>
                <button className="btn btn-danger" onClick={async () => await handleEdit(item, true)}>
                  Details
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

      <Pagination handlePaginate={handlePaginate} handleItemsPerPageChange={handleItemsPerPageChange} pagination={pagination} />

      <EditModal
        isOpen={showEditModal || showAddModal}
        onClose={() => {
          setShowEditModal(false);
          setShowAddModal(false);
          setShowDetailsModal(false);
        }}
        onSave={showEditModal ? handleSaveEdit : handleSaveAdd}
        title={showEditModal ? editModalTitle : (showAddModal ? addModalTitle : "Details")}
        initialState={currentItem}
        labels={fieldLabels}
        properties={fields}
      />
    </div>
  );
};