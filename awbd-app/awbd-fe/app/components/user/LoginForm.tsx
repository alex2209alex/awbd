// components/LoginForm.js
"use client"
import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from "next/navigation"
import { login, selectToken } from '../../../lib/features/user/slice'; // Adjust path

const LoginForm = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const dispatch = useDispatch();
    const router = useRouter();

    const token = useSelector(selectToken);
    console.log(">>>token: ", token)

    const handleSubmit = async (e) => {
        e.preventDefault();
        await dispatch(login({ email, password }));
        setSubmitted(true)
    };

    useEffect(() => {
        if (token && submitted) {
            // Save token to localStorage
            // localStorage.clear()
            localStorage.setItem("token_awbd", token);

            // Redirect to home page
            router.push("/")
            setSubmitted(false)
        }
    }, [token, router, submitted]);


    return (
        <form onSubmit={async (e) => await handleSubmit(e)} className="container">
            <div className="mb-3">
                <label htmlFor="email" className="form-label">Email:</label>
                <input
                    type="email"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label">Password:</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
            <button type="submit" className="btn btn-primary" >
                {/* {status === "loading" ? "Logging In..." : "Log In"} */}
                Log In
            </button>
        </form>
    );
};

export default LoginForm;