"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useSelector } from "react-redux";
import { selectLoggedIn } from "../../lib/features/user/slice"; // Adjust the path

export const Nav = () => {
  const pathname = usePathname();
  // const loggedIn = useSelector(selectLoggedIn);
  const loggedIn = true;

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <ul className="navbar-nav d-flex flex-row gap-5">
          {!loggedIn ? (
            <>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/signup" ? "active" : ""}`}
                  href="/signup"
                >
                  Sign Up
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/login" ? "active" : ""}`}
                  href="/login"
                >
                  Log In
                </Link>
              </li>
            </>
          ) : (
            <>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/" ? "active" : ""}`}
                  href="/"
                >
                  Home
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/verify" ? "active" : ""}`}
                  href="/verify"
                >
                  Verify
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/producers" ? "active" : ""}`}
                  href="/producers"
                >
                  Producers
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/ingredients" ? "active" : ""}`}
                  href="/ingredients"
                >
                  Ingredients
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/products" ? "active" : ""}`}
                  href="/products"
                >
                  Products
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/couriers" ? "active" : ""}`}
                  href="/couriers"
                >
                  Couriers
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/cookers" ? "active" : ""}`}
                  href="/cookers"
                >
                  Cookers
                </Link>
              </li>
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/clients" ? "active" : ""}`}
                  href="/clients"
                >
                  Clients
                </Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};
