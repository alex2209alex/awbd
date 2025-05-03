// components/Filter.tsx
import React from "react";

interface FilterProps {
  fieldName: string;
  fieldLabel: string;
  value: string;
  onFilterChange: (field: string, value: string) => void;
}

export const Filter: React.FC<FilterProps> = ({
  fieldName,
  fieldLabel,
  value,
  onFilterChange,
}) => {
  const handleFilterChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onFilterChange(fieldName, event.target.value);
  };

  return (
    <div className="mb-3">
      <label htmlFor={fieldName} className="form-label">
        {fieldLabel}
      </label>
      <input
        type="text"
        className="form-control"
        id={fieldName}
        value={value}
        onChange={handleFilterChange}
      />
    </div>
  );
};
