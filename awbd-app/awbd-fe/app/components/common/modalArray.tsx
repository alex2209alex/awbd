import React, { useEffect, useState } from "react";
import { Dropdown } from "./dropdown"; // Assuming you have this
import apiClient from "@/lib/apiClient";

export const ModalArray = ({ array, propertyName = "", fields, onChange, readOnly = false }: any) => {
  const addTempKeyToArr = (arr: any[]) => arr?.length ? arr.map(item => { return { ...item, _tempKey: Math.random().toString(36).substr(2, 9) } }) : []
  console.log(">>>arr: ", array)
  const [currentArray, setCurrentArray] = useState<any[]>(addTempKeyToArr(array) || []);
  const [editingItem, setEditingItem] = useState<any | null>(null);
  const [showForm, setShowForm] = useState(false);

  const [itemNames, setItemNames] = useState<any>({});
  useEffect(() => {
    const fetchItems = async () => {
      if (!propertyName) return;
      const uniqueIds = [...new Set(currentArray.map(item => item.id))];
      console.log(">>>uniqueIds: ", uniqueIds)
      console.log(">>>propertyName: ", propertyName)

      const promises = uniqueIds.map((id) => {
        const actId = id?.id ? id.id : id
        console.log(">>>actId: ", actId)
        return apiClient.get(`/${propertyName}/${actId}`).then((res: any) => ({ id: actId, name: res.data.name }))
      }
      );

      const results = await Promise.all(promises);

      console.log(">>>results: ", results)

      const namesMap: any = {};
      results.forEach(({ id, name }) => {
        namesMap[id] = name;
      });

      console.log(">>>namesMap: ", namesMap)

      setItemNames(namesMap);
    };

    if (currentArray.length > 0) {
      fetchItems();
    }
  }, [currentArray]);

  useEffect(() => {
    onChange(currentArray);
  }, [currentArray]);

  console.log(">>>currentArray: ", currentArray)

  const handleDelete = (tempKey: string) => {
    console.log(">>>tempKey: ", tempKey)
    const updated = currentArray.filter((item) => item._tempKey !== tempKey);
    setCurrentArray(updated);
  };

  console.log(">>>itemNames: ", itemNames)

  const handleSaveItem = () => {
    if (editingItem._tempKey) {
      // Editing existing item
      setCurrentArray((prev) =>
        prev.map((item) =>
          item._tempKey === editingItem._tempKey ? editingItem : item
        )
      );
    } else {
      const newItem = {
        ...editingItem,
        _tempKey: Math.random().toString(36).substr(2, 9), // temporary key
      };
      setCurrentArray([...currentArray, newItem]);
    }
    setEditingItem(null);
    setShowForm(false);
  };

  const handleFieldChange = (fieldName: string, value: any) => {
    setEditingItem((prev: any) => ({ ...prev, [fieldName]: value }));
  };

  const renderField = (field: any) => {
    const value = editingItem?.[field.name] || "";
    if (field.type === "dropdown") {
      return (
        <Dropdown
          apiEndpoint={field.endpoint}
          valueKey="id"
          labelKey="name"
          placeholder={`Select ${field.name}`}
          readOnly={readOnly}
          onSelect={(selectedOption: any) =>
            handleFieldChange(field.name, selectedOption)
          }
        />
      );
    }
    return (
      <input
        type="text"
        className="form-control mb-2"
        disabled={readOnly}
        value={value}
        onChange={(e) => handleFieldChange(field.name, e.target.value)}
      />
    );
  };

  return (
    <div>
      <span>{propertyName.slice(0, 1).toUpperCase() + propertyName.slice(1)}</span>
      <div className="container mt-3">
        <table className="table">
          <thead>
            <tr>
              {fields.map((field: any) => (
                <th key={field.name}>{field.name === "id" ? "Name" : field.name}</th>
              ))}
              {!readOnly && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {currentArray.map((item: any, index) => (
              <tr key={item._tempKey || item.id}>
                {fields.map((field: any) => (
                  <td key={field.name}>
                    {field.name === "id" ? itemNames[item.id?.id ? item.id.id : item.id] || `Item ${index}` : item[field.name]}
                  </td>
                ))}
                {!readOnly && <td>
                  <button
                    type="button"
                    className="btn btn-warning btn-sm me-2"
                    onClick={() => {
                      setEditingItem(item);
                      setShowForm(true);
                    }}
                  >
                    Edit
                  </button>
                  <button
                    type="button"
                    className="btn btn-danger btn-sm"
                    onClick={() => handleDelete(item._tempKey)}
                  >
                    Delete
                  </button>
                </td>
                }
              </tr>
            ))}
          </tbody>
        </table>

        {!readOnly && <div className="text-center">
          <button
            type="button"
            onClick={() => {
              setEditingItem({});
              setShowForm(true);
            }}
            className="btn btn-primary"
          >
            + Add
          </button>
        </div>
        }

        {!readOnly && showForm && (
          <div className="card mt-3 p-3">
            <h5>{editingItem?._tempKey ? "Edit Item" : "Add Item"}</h5>
            {fields.map((field: any) => (
              <div key={field.name}>{renderField(field)}</div>
            ))}
            <div className="mt-3">
              <button type="button" className="btn btn-success me-2" onClick={handleSaveItem}>
                Save
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => {
                  setEditingItem(null);
                  setShowForm(false);
                }}
              >
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
