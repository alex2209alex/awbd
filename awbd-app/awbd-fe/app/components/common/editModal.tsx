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
  detailsLabels: any;
  properties: any[];
  detailsProperties: any;
  readOnly: boolean;
  isEdit: boolean;
}

export const EditModal: React.FC<EditModalProps> = ({
  isOpen,
  onClose,
  onSave,
  initialState,
  apiEndpoint,
  title,
  labels,
  detailsLabels,
  properties,
  isEdit,
  detailsProperties,
  readOnly = false,
}) => {
  if (!isOpen) return null;
  const [state, setState] = useState(initialState);

  console.log(">>>detailsProperties: ", detailsProperties)
  console.log(">>>detailsLabels: ", detailsLabels)
  console.log(">>>properties: ", properties)

  const props = detailsProperties || properties
  const lbls = detailsLabels || labels

  useEffect(() => {
    (async () => {
      const copy = { ...initialState };
      for (let i = 0; i < props.length; i++) {
        const prop = props[i];
        if (prop.type === "array" && Array.isArray(initialState[prop.name])) {
          copy[prop.name] = await normalizeArrayField(
            initialState[prop.name],
            prop.fields
          );
        }
      }
      setState(copy);
    })();
  }, [initialState, props]);

  // ➋ On save: denormalize each array before sending
  const handleSave = async () => {
    const payload = { ...state };
    for (const prop of props) {
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

  console.log(">>>state: ", state, props)

  return (
    <Modal isOpen={isOpen} onClose={onClose} onSave={async () => await handleSave()} title={title} readOnly={readOnly}>
      {props.map(
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
            // if (isEdit && propertyName === "password") return null;
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
              {/* {(!isEdit || propertyName !== "password") &&
                <label htmlFor={propertyName} className="form-label">
                  {lbls[index]}
                </label>
              } */}
              <label htmlFor={propertyName} className="form-label">
                {lbls[index]}
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
                  onSelect={(selectedOption: any) => setState({ ...state, [propertyName]: selectedOption })}
                  labelKey="name"
                  valueKey="id"
                  readOnly={readOnly}
                  apiEndpoint={propertyEndpoint}
                  placeholder={state?.[`${propertyName}Name`] || `Search ${propertyName}`}
                />
              )}
              {propertyType === "array" && (
                <ModalArray
                  array={state?.[propertyName] || ""}
                  propertyName={propertyName || ""}
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
