"use client"
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { productStorageExtractor, productStorageInserter } from '@/lib/utils';
import apiClient from '@/lib/apiClient';
import { Minus, PlusCircle, PlusIcon } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { addOrderApi } from '@/lib/features/order/api';
import { addOrderAsync, updateOrderAsync } from '@/lib/features/order/slice';

export const Cart = () => {
    const [cart, setCart] = useState<any>([]);
    const [products, setProducts] = useState<any>([]);
    const [address, setAddress] = useState("");
    const dispatch = useDispatch()

    useEffect(() => {
        setCart(productStorageExtractor());
    }, []);

    // Fetch product details for items in the cart
    useEffect(() => {
        const fetchProducts = async () => {
            const productDetails = await Promise.all(
                cart.map(async (item) => {
                    try {
                        const res = await apiClient.get(`/products/${item.id}`);
                        return { ...res.data, quantity: item.quantity };
                    } catch (err) {
                        console.error(`Error fetching product ${item.id}`, err);
                        return null;
                    }
                })
            );
            setProducts(productDetails.filter(Boolean)); // remove failed fetches
        };

        if (cart.length > 0) {
            fetchProducts();
        } else {
            setProducts([]);
        }
    }, [cart]);

    const updateCart = (newCart) => {
        productStorageInserter(newCart)
        window.dispatchEvent(new Event('cartUpdated'));
        setCart(newCart);
    };

    // Handlers
    const handleIncrease = (id) => {
        const newCart = cart.map((item) =>
            item.id == id ? { ...item, quantity: item.quantity + 1 } : item
        );
        updateCart(newCart);
    };

    const handleDecrease = (id) => {
        const newCart = cart
            .map((item) =>
                item.id == id ? { ...item, quantity: Math.max(1, item.quantity - 1) } : item
            )
            .filter((item) => item.quantity > 0);
        updateCart(newCart);
    };

    const handleRemove = (id) => {
        const newCart = cart.filter((item) => item.id != id);
        updateCart(newCart);
    };

    const handleClearAll = () => {
        localStorage.removeItem('products');
        window.dispatchEvent(new Event('cartUpdated'));
        setCart([]);
        setProducts([]);
    };

    const handleSendOrder = async () => {
        console.log(">>>>>CART: ", cart)
        localStorage.removeItem('products');
        setCart([]);
        setProducts([]);
        window.dispatchEvent(new Event('cartUpdated'));
        const order = { address, products: cart }
        await dispatch(addOrderAsync(order))
    };

    return (
        <div>
            <div className="container py-4" style={{ paddingLeft: "200px", paddingRight: "200px" }}>
                <h1 className="h3 mb-4 fw-bold">Your Cart</h1>

                {products.length === 0 ? (
                    <p>Your cart is empty.</p>
                ) : (
                    <div className="d-flex flex-column gap-3">
                        {products.map((product) => (
                            <div key={product.id} className="d-flex justify-content-between align-items-center border-bottom pb-2">
                                <div>
                                    <h5 className="mb-1">{product.name}</h5>
                                    <p className="mb-0">Quantity: {product.quantity}</p>
                                </div>
                                <div className="btn-group" role="group">
                                    <button
                                        onClick={() => handleIncrease(product.id)}
                                        className="btn btn-success btn-sm"
                                        style={{ marginRight: "20px", width: "40px" }}
                                    >
                                        <PlusIcon />
                                    </button>
                                    <button
                                        onClick={() => handleDecrease(product.id)}
                                        className="btn btn-warning btn-sm"
                                        style={{ marginRight: "20px", width: "40px" }}
                                    >
                                        <Minus />
                                    </button>
                                    <button
                                        onClick={() => handleRemove(product.id)}
                                        className="btn btn-danger btn-sm"
                                        style={{ marginRight: "20px", width: "80px" }}
                                    >
                                        Remove
                                    </button>
                                </div>
                            </div>
                        ))}

                        <div className='flex'>
                            <span style={{ paddingRight: "20px" }}>Address:</span>
                            <input className='w-1/3 bg-white border border-black text-black' defaultValue={address} onChange={(e) => setAddress(e.target.value)} />
                        </div>

                        <button
                            onClick={handleClearAll}
                            className="btn btn-danger mt-3"
                        >
                            Remove All
                        </button>
                        <button
                            onClick={async () => await handleSendOrder()}
                            className="btn btn-primary mt-3"
                        >
                            Send order
                        </button>
                    </div>
                )}
            </div>
        </div>

    );
};