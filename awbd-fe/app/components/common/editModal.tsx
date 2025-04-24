// components/EditModal.tsx
"use client"
import React, { useState } from "react";
import { Modal } from "./Modal"; // Import the generic Modal component
import { SearchableDropdown } from "./searchableDropdown";
import { Dropdown } from "./dropdown";

interface EditModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (state: any) => Promise<void>;
  initialState?: any;
  apiEndpoint: string;
  title: string;
  labels: any[];
  properties: any[];
}

export const EditModal: React.FC<EditModalProps> = ({
  isOpen,
  onClose,
  onSave,
  initialState,
  apiEndpoint,
  title,
  labels,
  properties,
}) => {
  if (!isOpen) return null;
  const [state, setState] = useState(initialState);

  const handleSave = async () => {
    await onSave(state);
    setState({});
    onClose();
  };

  console.log(">>>state in edit modal: ", state)

  return (
    <Modal isOpen={isOpen} onClose={onClose} onSave={async () => await handleSave()} title={title}>
      {properties.map(
        (
          {
            name: propertyName,
            type: propertyType,
            inputType,
            endpoint: propertyEndpoint,
          }: { name: string; type: string; endpoint: string, inputType: string },
          index: number
        ) => {
          console.log(">>>inputType", inputType)
          return (
            <div className="mb-3">
              <label htmlFor={propertyName} className="form-label">
                {labels[index]}
              </label>
              {propertyType === "input" && (
                <input
                  type={inputType || "text"}
                  className="form-control"
                  id="producerName"
                  value={state?.[propertyName] || ""}
                  onChange={(e) =>
                    setState({ ...state, [propertyName]: e.target.value })
                  }
                />
              )}
              {propertyType === "searchDropdown" && (
                <SearchableDropdown
                  onSelect={(selectedOption: any) =>
                    setState({ ...state, [propertyName]: selectedOption })
                  }
                  labelKey="name"
                  valueKey="id"
                  apiEndpoint={`api/${propertyName}`}
                  updateEndpoint={`api/${propertyName}`}
                  placeholder={`Search ${propertyName}`}
                />
              )}
              {propertyType === "dropdown" && (
                <Dropdown
                  onSelect={(selectedOption: any) => {
                    console.log(">>>selectedOption: ", selectedOption);
                    setState({ ...state, [propertyName]: selectedOption })
                  }
                  }
                  labelKey="name"
                  valueKey="id"
                  apiEndpoint={propertyEndpoint}
                  placeholder={`Search ${propertyName}`}
                />
              )}
            </div>
          );
        }
      )}
    </Modal>
  );
};
