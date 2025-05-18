import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const productStorageExtractor = () => {
  const products = localStorage.getItem("products");
  if (!products) return [];
  const productsAns = []
  const productsList = products.split(",");
  for (const product of productsList) {
    let [productId, quantity] = product.split(":")
    const parsedQuantity = Number.parseInt(quantity);
    if (productId && parsedQuantity && !Number.isNaN(parsedQuantity))
      productsAns.push({ id: productId, quantity: parsedQuantity });
  }
  return productsAns;
}

export const addProductToCart = (product: any) => {
  const products = productStorageExtractor()
  const foundProduct = products.find(currProd => currProd.id == product.id)
  if (!foundProduct) {
    products.push({ id: product.id, quantity: 1 })
    productStorageInserter(products);
    return products;
  }
  const newProducts = products.map(currProduct => {
    if (currProduct.id == product.id)
      return { id: product.id, quantity: 1 }
    return currProduct
  })
  productStorageInserter(newProducts);
  return newProducts
}

export const removeProductFromCart = (productId: any) => {
  const products = productStorageExtractor()
  const foundProduct = products.find(currProd => currProd.id == productId)
  if (!foundProduct) throw Error("Product not found in cart");
  const newProducts = products.filter(currProduct => currProduct.id != productId);
  productStorageInserter(newProducts);
}

export const checkProductInCart = (productId: any) => {
  const products = productStorageExtractor()
  console.log(">>>products in storage: ", products)
  const foundProduct = products.find(currProd => currProd.id == productId)
  return !!foundProduct
}

export const productStorageInserter = (products: any[]) => {
  let productStorage = "";
  let index = 0;
  console.log(">>>products productStorageInserter: ", products)
  for (const product of products) {
    productStorage += `${product.id}:${product.quantity}`;
    if (index < products.length - 1) productStorage += ","
  }
  const len = productStorage.length;
  if (productStorage.endsWith(",")) productStorage = productStorage.slice(0, len - 1)
  localStorage.setItem("products", productStorage);
}