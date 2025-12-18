import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const weatherApi = {
    getCurrentWeather(city) {
        return axios.get(`${API_BASE_URL}/${city}`);
    },

    getAverageWeather(city, days) {
        return axios.get(`${API_BASE_URL}/avg/${city}`, {
            params: { days }
        });
    },

    getTrends(city, days) {
        return axios.get(`${API_BASE_URL}/trends/${city}`, {
            params: { days }
        });
    },

    getHistory(city) {
        return axios.get(`${API_BASE_URL}/history/${city}`);
    }
};

export default weatherApi;