import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL;

// Get token from localStorage
const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    }
});

export const getAllProperties = async () => {
    const response = await axios.get(`${API_URL}/property`, getAuthHeader());
    return response.data;
};

export const getMyProperties = async () => {
    const response = await axios.get(`${API_URL}/property/my-properties`, getAuthHeader());
    return response.data;
};

export const getPropertyById = async (id) => {
    const response = await axios.get(`${API_URL}/property/${id}`);
    return response.data;
};

export const createProperty = async (propertyData, images) => {
    const formData = new FormData();

    // Add property data as JSON part
    formData.append('property', new Blob([JSON.stringify(propertyData)], {
        type: 'application/json'
    }));

    // Add images
    if (images && images.length > 0) {
        images.forEach(image => {
            formData.append('images', image);
        });
    }

    const response = await axios.post(`${API_URL}/property`, formData, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
};

export const deleteProperty = async (id) => {
    await axios.delete(`${API_URL}/property/${id}`, getAuthHeader());
};

