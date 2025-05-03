// components/AdminCreateUserForm.js

import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createUser, selectUserStatus, selectUserError } from '../../../lib/features/user/slice'; // Adjust the path to your userSlice

const AdminCreateUserForm = () => {
    const [name, setName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState(''); // Add password field
    const dispatch = useDispatch();
    const status = useSelector(selectUserStatus);
    const error = useSelector(selectUserError);

    const handleSubmit = (e: any) => {
        e.preventDefault();
        dispatch(createUser({ name, phoneNumber, address, email, password })); // Send password
    };

    return (
        <form onSubmit={handleSubmit} className="container">
            <div className="mb-3">
                <label htmlFor="name" className="form-label">Name:</label>
                <input
                    type="text"
                    id="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="phoneNumber" className="form-label">Phone Number:</label>
                <input
                    type="tel"
                    id="phoneNumber"
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="address" className="form-label">Address:</label>
                <textarea
                    id="address"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
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
            <div className="mb-3">
                <label htmlFor="role" className="form-label">Role:</label>
                <input
                    type="role"
                    id="role"
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                    required
                    className="form-control"
                />
            </div>
            <button type="submit" className="btn btn-primary" disabled={status === "loading"}>
                {status === "loading" ? "Creating User..." : "Create User"}
            </button>
            {error && <p className="text-danger mt-2">{error}</p>}
        </form>
    );
};

export default AdminCreateUserForm;