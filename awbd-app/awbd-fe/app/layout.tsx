import Image from "next/image";
import type { ReactNode } from "react";
import { StoreProvider } from "./StoreProvider";
import { Nav } from "./components/Nav";
import { ThemeProvider } from "@/components/theme-provider"
import { AppToaster } from "./components/common/Toaster";

import "../globals.css"
import styles from "./styles/layout.module.css";
import "bootstrap/dist/css/bootstrap.min.css";

interface Props {
  readonly children: ReactNode;
}

export default function RootLayout({ children }: Props) {
  return (
    <>
      <StoreProvider>
        <html lang="en" suppressHydrationWarning>
          <body>
            <ThemeProvider attribute="class" defaultTheme="system" enableSystem>
              <section>
                <Nav />
                <main>{children}</main>
              </section>
            </ThemeProvider>
          </body>
        </html>
      </StoreProvider>
      <AppToaster />
    </>
  )
}

// export default function RootLayout({ children }: Props) {
//   return (
//     <StoreProvider>
//       <html lang="en">
//         <body>
//           <section className={""}>
//             <Nav />

//             <main className={""}>{children}</main>
//           </section>
//         </body>
//       </html>
//     </StoreProvider>
//   );
// }
