"use client"; // Required for sonner components

import React from "react";
import { Toaster as SonnerToaster } from "sonner";

// This component will wrap Sonner's Toaster, allowing for centralized configuration.
// You can then import and use <AppToaster /> in your main layout file.
export const AppToaster: React.FC = () => {
  return (
    <SonnerToaster
      richColors
      position="top-right"
      closeButton
      toastOptions={{
        classNames: {
          title: "sonner-toast-title", // Added text-nowrap
          actionButton: "btn btn-outline-danger btn-sm mt-n1 me-n2",
        },
      }}
    />
  );
};

// You would then import AppToaster into your main layout component and render it.
// For example, in your App.tsx or equivalent:
// import { AppToaster } from './components/general/Toaster';
//
// function App() {
//   return (
//     <div>
//       {/* Other app content */}
//       <AppToaster />
//     </div>
//   );
// }
