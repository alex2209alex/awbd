// components/EditModal.tsx
import React, { useState } from "react";
import { Modal } from "./Modal"; // Import the generic Modal component
import { SearchableDropdown } from "./searchableDropdown";

interface EditModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (state: any) => void;
  initialState?: any;
  title: string;
  labels: any[];
  properties: any[];
}

export const EditModal: React.FC<EditModalProps> = ({
  isOpen,
  onClose,
  onSave,
  initialState,
  title,
  labels,
  properties,
}) => {
  const [state, setState] = useState(initialState);

  const handleSave = () => {
    onSave(state);
    setState({});
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} onSave={handleSave} title={title}>
      {properties.map(
        (
          {
            name: propertyName,
            type: propertyType,
          }: { name: string; type: string },
          index: number
        ) => {
          return (
            <div className="mb-3">
              <label htmlFor={propertyName} className="form-label">
                {labels[index]}
              </label>
              {propertyType === "input" && (
                <input
                  type="text"
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
            </div>
          );
        }
      )}
    </Modal>
  );
};
