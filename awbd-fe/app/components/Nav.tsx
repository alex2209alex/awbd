"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useSelector } from "react-redux";
import { selectLoggedIn } from "../../lib/features/user/slice"; // Adjust the path

import styles from "../styles/layout.module.css";

export const Nav = () => {
  const pathname = usePathname();
  // const loggedIn = useSelector(selectLoggedIn);
  const loggedIn = true;

  return (
    <nav className={styles.nav}>
      {!loggedIn ? (
        <>
          <Link
            className={`${styles.link} ${pathname === "/signup" ? styles.active : ""}`}
            href="/signup"
          >
            Sign Up
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/login" ? styles.active : ""}`}
            href="/login"
          >
            Log In
          </Link>
        </>
      ) : (
        <>
          <Link
            className={`${styles.link} ${pathname === "/" ? styles.active : ""}`}
            href="/"
          >
            Home
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/verify" ? styles.active : ""}`}
            href="/verify"
          >
            Verify
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/producers" ? styles.active : ""}`}
            href="/producers"
          >
            Producers
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/ingredients" ? styles.active : ""}`}
            href="/ingredients"
          >
            Ingredients
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/products" ? styles.active : ""}`}
            href="/products"
          >
            Products
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/couriers" ? styles.active : ""}`}
            href="/couriers"
          >
            Couriers
          </Link>
          <Link
            className={`${styles.link} ${pathname === "/cookers" ? styles.active : ""}`}
            href="/cookers"
          >
            Cookers
          </Link>
        </>
      )}
    </nav>
  );
};