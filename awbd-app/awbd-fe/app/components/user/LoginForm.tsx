// components/LoginForm.js

import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { login, selectUserStatus, selectUserError } from '../../../lib/features/user/slice'; // Adjust path

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const dispatch = useDispatch();
    const status = useSelector(selectUserStatus);
    const error = useSelector(selectUserError);

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(login({ email, password }));
    };

    return (
        <form onSubmit={handleSubmit} className="container">
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
            <button type="submit" className="btn btn-primary" disabled={status === "loading"}>
                {status === "loading" ? "Logging In..." : "Log In"}
            </button>
            {error && <p className="text-danger mt-2">{error}</p>}
        </form>
    );
};

export default LoginForm;