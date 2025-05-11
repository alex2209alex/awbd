// components/SignupForm.js
"use client"
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { signup } from '../../../lib/features/user/slice'; // Adjust the path to your userSlice
import { addClientAsync } from '@/lib/features/client/slice';
import { useRouter } from 'next/navigation';

const SignUpForm = () => {
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const dispatch = useDispatch();
  const router = useRouter();

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    await dispatch(addClientAsync({ email, password, name, phoneNumber }));
    router.push("/login")
  };

  return (
    <form onSubmit={async (e) => await handleSubmit(e)} className="container">
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
        <label htmlFor="password" className="form-label">Password:</label>
        <textarea
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
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
        {/* {status === "loading" ? "Signing Up..." : "Sign Up"} */}
        Sign Up
      </button>
    </form>
  );
};

export default SignUpForm;