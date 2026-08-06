import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL;

const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
    }
});

export const getAllReviews = async () => {
    const response = await axios.get(`${API_URL}/reviews`);
    return response.data;
};

export const getReviewsByProperty = async (propertyId) => {
    const response = await axios.get(`${API_URL}/reviews/property/${propertyId}`);
    return response.data;
};

export const createReview = async (propertyId, reviewData) => {
    const response = await axios.post(
        `${API_URL}/reviews/property/${propertyId}`,
        reviewData,
        getAuthHeader()
    );
    return response.data;
};

export const deleteReview = async (reviewId) => {
    await axios.delete(`${API_URL}/reviews/${reviewId}`, getAuthHeader());
};