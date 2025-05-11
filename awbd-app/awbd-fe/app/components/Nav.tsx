"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import { clearUser, selectToken, selectUser } from "../../lib/features/user/slice"; // Adjust the path
import { useEffect } from "react";

export const Nav = () => {
  const pathname = usePathname();
  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  const router = useRouter();
  console.log(">>>user: ", user);
  // const loggedIn = true;
  // useEffect({
  //   if(!user?.token)
  // })

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <ul className="navbar-nav d-flex flex-row gap-5">
          {!user?.token ? (
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
              {user.isAdmin &&
                <li className="nav-item">
                  <Link
                    className={`nav-link ${pathname === "/producers" ? "active" : ""}`}
                    href="/producers"
                  >
                    Producers
                  </Link>
                </li>
              }
              {(user.isAdmin || user.isCooker) &&
                <li className="nav-item">
                  <Link
                    className={`nav-link ${pathname === "/ingredients" ? "active" : ""}`}
                    href="/ingredients"
                  >
                    Ingredients
                  </Link>
                </li>
              }
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/products" ? "active" : ""}`}
                  href="/products"
                >
                  Products
                </Link>
              </li>
              {user.isAdmin &&
                <>
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
              }
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/clients" ? "active" : ""}`}
                  href="/orders"
                >
                  Orders
                </Link>
              </li>
              <li className="nav-item">
                <button
                  className={``}
                  onClick={() => {
                    dispatch(clearUser());
                    router.push("/login")
                  }}
                >
                  Log out
                </button>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};
