import axios from "axios";
import React, { useEffect, useState, useRef } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";

// interface DropdownProps {
//   onSelect: (selected: any) => void;
//   labelKey: string;
//   valueKey: string;
//   apiEndpoint: string;
//   updateEndpoint?: string; // Optional update endpoint
//   placeholder?: string;
//   initialValue?: any;
// }

export const Dropdown = ({
    onSelect,
    labelKey,
    valueKey,
    apiEndpoint,
    updateEndpoint,
    placeholder = "Select...",
    initialValue,
}: any) => {
    const [selectedItem, setSelectedItem] = useState<any>(initialValue || null);
    const [isOpen, setIsOpen] = useState(false);
    const [options, setOptions] = useState<any[]>([]);
    const dropdownRef = useRef<HTMLDivElement>(null);

    console.log(">>>options: ", options)

    useEffect(() => {
        const fetchOptions = async () => {
            try {
                const { data } = await axios.get(apiEndpoint);
                setOptions(data);
            } catch (error) {
                console.error("Error fetching options:", error);
            }
        };

        fetchOptions();
    }, [apiEndpoint]);

    // useEffect(() => {
    //     if (selectedItem && apiEndpoint) {
    //         const updateSelectedItem = async () => {
    //             try {
    //                 await axios.put(apiEndpoint, selectedItem);
    //                 console.log("Updated item:", selectedItem);
    //             } catch (error) {
    //                 console.error("Error updating selected item:", error);
    //             }
    //         };

    //         updateSelectedItem();
    //     }
    // }, [selectedItem, apiEndpoint]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <div className="position-relative" ref={dropdownRef}>
            <button className="form-control d-flex justify-content-between align-items-center" onClick={() => setIsOpen(!isOpen)}>
                {selectedItem ? selectedItem[labelKey] : placeholder}
                {isOpen ? <ChevronUp /> : <ChevronDown />}
            </button>
            {isOpen && (
                <ul className="list-group position-absolute w-100 mt-1 z-3">
                    {options.map((option) => (
                        <li
                            key={option[valueKey]}
                            className="list-group-item list-group-item-action"
                            onClick={() => {
                                onSelect(option);
                                setSelectedItem(option);
                                setIsOpen(false);
                            }}
                        >
                            {option[labelKey]}
                        </li>
                    ))}
                    {options.length === 0 && (
                        <li className="list-group-item text-muted">No options available</li>
                    )}
                </ul>
            )}
        </div>
    );
};