import axios from "axios";
import React, { useEffect, useState } from "react";

interface SearchableDropdownProps {
  onSelect: (selected: any) => void; // Function when selecting an option
  labelKey: string; // Key for displaying the option (e.g., "name")
  valueKey: string; // Key for the option value (e.g., "id")
  apiEndpoint: string; // API endpoint to fetch options
  updateEndpoint: string; // API endpoint to update selected item
  placeholder?: string;
}

export const SearchableDropdown: React.FC<SearchableDropdownProps> = ({
  onSelect,
  labelKey,
  valueKey,
  apiEndpoint,
  updateEndpoint,
  placeholder = "Search...",
}) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedItem, setSelectedItem] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const [options, setOptions] = useState<any[]>([]);

  useEffect(() => {
    if (!searchTerm) return;

    const fetchOptions = async () => {
      try {
        const { data } = await axios.get(`${apiEndpoint}?search=${searchTerm}`);
        setOptions(data);
      } catch (error) {
        console.error("Error fetching options:", error);
      }
    };

    fetchOptions();
  }, [searchTerm]);

  useEffect(() => {
    if (!selectedItem) return;

    const updateSelectedItem = async () => {
      try {
        await axios.put(updateEndpoint, selectedItem);
        console.log("Updated item:", selectedItem);
      } catch (error) {
        console.error("Error updating selected item:", error);
      }
    };

    updateSelectedItem();
  }, [selectedItem]);

  return (
    <div className="position-relative">
      <input
        type="text"
        className="form-control"
        placeholder={placeholder}
        value={searchTerm}
        onFocus={() => setIsOpen(true)}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      {isOpen && (
        <ul className="list-group position-absolute w-100 mt-1 z-3">
          {options.map((option) => (
            <li
              key={option[valueKey]}
              className="list-group-item list-group-item-action"
              onClick={() => {
                onSelect(option);
                setSearchTerm(option[labelKey]); // Set selected value
                setSelectedItem(option[labelKey]); // Set selected value
                setIsOpen(false); // Close dropdown
              }}
            >
              {option[labelKey]}
            </li>
          ))}
          {options.length === 0 && (
            <li className="list-group-item text-muted">No results found</li>
          )}
        </ul>
      )}
    </div>
  );
};
