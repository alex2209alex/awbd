"use client";

import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import { clearUser, selectToken, selectUser, setUserByToken } from "../../lib/features/user/slice"; // Adjust the path
import { useEffect, useState } from "react";
import { productStorageExtractor } from "@/lib/utils";
import { ShoppingCart } from "lucide-react";
import apiClient from "@/lib/apiClient";
import { toast } from "sonner";

export const Nav = () => {
  const pathname = usePathname();
  const user = useSelector(selectUser);
  const dispatch = useDispatch();
  const router = useRouter();
  let prodLocalSt = "";
  if (typeof window !== 'undefined')
    prodLocalSt = localStorage.getItem("products");
  useEffect(() => {
    const checkAndSetUser = () => {
      let localToken = "";
      if (typeof window !== 'undefined')
        localToken = localStorage.getItem("token_awbd");

      if (!user?.token && localToken) {
        dispatch(setUserByToken({ token: localToken }));
      } else if (user?.token) {
        if (user.exp < Date.now() / 1000) {
          dispatch(clearUser());
          router.push("/login");
        }
      }
    };
    checkAndSetUser(); // Run once immediately
    const interval = setInterval(checkAndSetUser, 5000); // Check every 5s

    return () => clearInterval(interval); // Cleanup on unmount
  }, [user?.token, user?.exp, dispatch, router])

  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
    const updateCartCount = () => {
      const cart = productStorageExtractor()
      const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
      setCartCount(totalItems);
    };

    updateCartCount();

    window.addEventListener('cartUpdated', updateCartCount);
    const interval = setInterval(updateCartCount, 500); // Check every 5s
    return () => clearInterval(interval); // Cleanup on unmount
    // return () => window.removeEventListener('cartUpdated', updateCartCount);
  }, [prodLocalSt]);

  const handleLoyaltyCard = async () => {
    try {
      const resp = await apiClient.post("/loyalty-cards");
      console.log("resp: ", resp)
      toast.success("Loyalty card created!", { duration: 0 })
    } catch (e) {
      console.log(">>>e: ", e)
    }
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      {/* <nav className="flex justify-between items-center p-4 bg-blue-600 text-black"> */}
      <div className="container-fluid">
        <ul className="navbar-nav d-flex flex-row gap-5 flex items-center">
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
              {(user.isAdmin) &&
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
                </>
              }
              <li className="nav-item">
                <Link
                  className={`nav-link ${pathname === "/orders" ? "active" : ""}`}
                  href="/orders"
                >
                  Orders
                </Link>
              </li>


              {user.isClient &&
                <li className="nav-item">
                  <Link
                    className={`nav-link ${pathname === "/clients" ? "active" : ""}`}
                    href="/cart"
                  >
                    <ShoppingCart size={24} />
                    {cartCount > 0 && (
                      <span className="badge rounded-pill bg-danger">
                        {cartCount}
                      </span>
                    )}
                  </Link>
                </li>
              }

              <li className="nav-item">
              </li>
              <li className="nav-item ml-auto flex flex-row gap-2">
                {user?.isClient && <button className="btn btn-secondary" onClick={async () => await handleLoyaltyCard()}>
                  Loyalty Card
                </button>
                }
                <button
                  className="nav-link"
                  onClick={() => {
                    dispatch(clearUser());
                    router.push("/login");
                  }}
                >
                  Log out
                </button>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav >
  );
};
