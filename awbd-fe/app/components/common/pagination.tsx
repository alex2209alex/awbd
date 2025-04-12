import React from "react";

export const Pagination = ({ handlePaginate, currentPage, totalPages }: any) => {
  return (
    <div className="d-flex justify-content-center mt-4">
      <button
        className="btn btn-secondary me-2"
        onClick={() => handlePaginate(currentPage - 1)}
        disabled={currentPage === 1}
      >
        Previous
      </button>
      <span className="mx-2">
        Page {currentPage} of {totalPages}
      </span>
      <button
        className="btn btn-secondary ms-2"
        onClick={() => handlePaginate(currentPage + 1)}
        disabled={currentPage === totalPages}
      >
        Next
      </button>
    </div>
  );
};
