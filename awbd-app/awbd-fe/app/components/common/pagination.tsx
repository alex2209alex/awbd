"use client"

import * as React from "react"
import * as Select from "@radix-ui/react-select"
import { ChevronDown } from "lucide-react"

interface Props {
  pagination: any
  handleItemsPerPageChange: (value: number) => void
  handlePaginate: (page: number) => void
}

export const Pagination = ({
  pagination,
  handleItemsPerPageChange,
  handlePaginate,
}: Props) => {
  if (!pagination || Object.keys(pagination).length === 0) return null;
  console.log(">>pagination: ", pagination)
  const { itemsTotal, hasNextPage, page, pageSize, pagesTotal } = pagination
  return (
    <div className="d-flex flex-column flex-md-row justify-content-between align-items-center gap-4 mt-4">
      {/* Items per page */}
      <div className="d-flex align-items-center gap-2">
        <span className="text-muted">Items per page:</span>
        <Select.Root
          value={pageSize.toString()}
          onValueChange={(val) => handleItemsPerPageChange(Number.parseInt(val))}
        >
          <Select.Trigger className="btn btn-outline-secondary d-flex align-items-center">
            <Select.Value />
            <ChevronDown className="ms-2" />
          </Select.Trigger>
          <Select.Portal>
            <Select.Content className="dropdown-menu">
              <Select.Viewport>
                {[5, 10, 20, 50].map((num) => (
                  <Select.Item
                    key={num}
                    value={num.toString()}
                    className="dropdown-item"
                  >
                    <Select.ItemText>{num}</Select.ItemText>
                  </Select.Item>
                ))}
              </Select.Viewport>
            </Select.Content>
          </Select.Portal>
        </Select.Root>
      </div>

      {/* Pagination Controls */}
      <div className="d-flex align-items-center gap-4">
        <button
          type="button"
          onClick={() => handlePaginate(page - 1)}
          disabled={page === 1}
          className="btn btn-outline-secondary"
        >
          Previous
        </button>

        <span className="text-muted">
          Page {page} of {pagesTotal}
        </span>

        <button
          type="button"
          onClick={() => handlePaginate(page + 1)}
          disabled={!hasNextPage}
          className="btn btn-outline-secondary"
        >
          Next
        </button>
      </div>
    </div>
  )
}
