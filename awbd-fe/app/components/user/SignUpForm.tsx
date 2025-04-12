// components/SignupForm.js

import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { signup, selectUserStatus, selectUserError } from '../../../lib/features/user/slice'; // Adjust the path to your userSlice

const SignupForm = () => {
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [address, setAddress] = useState('');
  const [email, setEmail] = useState('');
  const dispatch = useDispatch();
  const status = useSelector(selectUserStatus);
  const error = useSelector(selectUserError);

  const handleSubmit = (e: any) => {
    e.preventDefault();
    dispatch(signup({ name, phoneNumber, address, email }));
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
      <button type="submit" className="btn btn-primary" disabled={status === "loading"}>
        {status === "loading" ? "Signing Up..." : "Sign Up"}
      </button>
      {error && <p className="text-danger mt-2">{error}</p>}
    </form>
  );
};

export default SignupForm;