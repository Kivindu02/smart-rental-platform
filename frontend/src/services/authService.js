import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL;

// Get token from localStorage
const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    }
});

export const login = async (email, password) => {
    const response = await axios.post(`${API_URL}/auth/login`, {
        email,
        password
    });
    return response.data;
};

export const register = async (firstName, lastName, email, password, confirmPassword) => {
    const response = await axios.post(`${API_URL}/auth/register`, {
        firstName,
        lastName,
        email,
        password,
        confirmPassword
    });
    return response.data;
};

export const getAllUsers = async () => {
    const response = await axios.get(`${API_URL}/auth/users`, getAuthHeader());
    return response.data;
};

export const deactivateUser = async (id) => {
    await axios.put(`${API_URL}/auth/users/${id}/deactivate`, {}, getAuthHeader());
};

export const deleteUser = async (id) => {
    await axios.delete(`${API_URL}/auth/${id}`, getAuthHeader());
};