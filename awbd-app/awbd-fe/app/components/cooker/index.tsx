"use client";

import { CookersTable } from "./table";
// import styles from "./Counter.module.css";

export const Cookers = () => {
  return (
    <div className="p-4">
      {/* <h2 className="text-xl font-bold mb-4">Producers</h2> */}
      <CookersTable />
    </div>
  );
};
