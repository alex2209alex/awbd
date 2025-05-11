// components/EditModal.tsx
"use client"
import React, { useEffect, useState } from "react";
import { Modal } from "./Modal"; // Import the generic Modal component
import { SearchableDropdown } from "./searchableDropdown";
import { Dropdown } from "./dropdown";
import { ModalArray } from "./modalArray";
import { denormalizeArrayField, normalizeArrayField } from "@/app/utils/formNormalizer";
import { NumericFormat } from 'react-number-format';

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
  readOnly = false,
}) => {
  if (!isOpen) return null;
  const [state, setState] = useState(initialState);

  useEffect(() => {
    (async () => {
      const copy = { ...initialState };
      for (let i = 0; i < properties.length; i++) {
        const prop = properties[i];
        if (prop.type === "array" && Array.isArray(initialState[prop.name])) {
          copy[prop.name] = await normalizeArrayField(
            initialState[prop.name],
            prop.fields
          );
        }
      }
      setState(copy);
    })();
  }, [initialState, properties]);

  // ➋ On save: denormalize each array before sending
  const handleSave = async () => {
    const payload = { ...state };
    for (const prop of properties) {
      if (prop.type === "array" && Array.isArray(payload[prop.name])) {
        payload[prop.name] = denormalizeArrayField(
          payload[prop.name],
          prop.fields
        );
      }
    }
    await onSave(payload);
    setState({});
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} onSave={async () => await handleSave()} title={title}>
      {properties.map(
        (
          {
            name: propertyName,
            type: propertyType,
            inputType,
            dataType,
            endpoint: propertyEndpoint,
            fields: propertyFields,
          }: { name: string; type: string; endpoint: string, inputType: string, dataType: string, fields: any },
          index: number
        ) => {
          const renderInput = () => {
            if (dataType === "number")
              return (
                <NumericFormat
                  className="form-control"
                  value={state?.[propertyName] ?? ''}
                  thousandSeparator={true}
                  decimalScale={2}
                  allowNegative={false}
                  disabled={readOnly}
                  onValueChange={({ floatValue }) =>
                    setState({ ...state, [propertyName]: floatValue ?? 0 })
                  }
                />
              )
            return (
              <input
                type={inputType || "text"}
                className="form-control"
                id="producerName"
                value={state?.[propertyName] || ""}
                disabled={readOnly}
                onChange={(e) =>
                  setState({ ...state, [propertyName]: e.target.value })
                }
              />
            )
          }
          return (
            <div className="mb-3">
              <label htmlFor={propertyName} className="form-label">
                {labels[index]}
              </label>
              {propertyType === "input" && renderInput()}
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
                  readOnly={readOnly}
                  apiEndpoint={propertyEndpoint}
                  placeholder={`Search ${propertyName}`}
                />
              )}
              {propertyType === "array" && (
                <ModalArray
                  array={state?.[propertyName] || ""}
                  fields={propertyFields}
                  readOnly={readOnly}
                  onChange={(propertyNewState: any) => setState({ ...state, [propertyName]: propertyNewState })}
                />
              )}
            </div>
          );
        }
      )}
    </Modal>
  );
};
