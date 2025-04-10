// A mock function to mimic making an async request for data
export const getProducers = async () => {
    const response = await fetch("http://localhost:3000/api/counter", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
    const result: { data: number } = await response.json();
  
    return result;
  };
  