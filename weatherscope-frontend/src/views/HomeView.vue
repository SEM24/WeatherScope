<script setup>
import weatherApi from "@/services/weatherApi.js";
import {useRouter} from 'vue-router';
import {ref} from "vue";

const router = useRouter();

const cityName = ref('');
const weather = ref(null);
const loading = ref(false);
const error = ref(null);

const searchWeather = async () => {
  if (!cityName.value.trim()) {
    error.value = 'Enter city name';
    return;
  }
  loading.value = true;
  error.value = null;
  try {
    const response = await weatherApi.getCurrentWeather(cityName.value);
    weather.value = response.data;
  } catch (err) {
    error.value = err.response?.data?.message || 'Check city name.';
    weather.value = null;
  } finally {
    loading.value = false;
  }
};
const formatDate = (timestamp) => {
  return new Date(timestamp).toLocaleString('ru-RU');
};

const viewTrends = () => {
  router.push({name: 'trends', params: {city: weather.value.city}});
};

const viewHistory = () => {
  router.push({name: 'history', params: {city: weather.value.city}});
};
</script>

<template>
  <div class="home-container">
    <h1 class="title">WeatherScope</h1>
    <div class="search-section">
      <input type="text" v-model="cityName" @keyup.enter="searchWeather" placeholder="Enter city name"
             class="city-input">
      <button @click="searchWeather" :disabled="loading" class="search-btn">{{ loading ? 'Loading...' : 'Find' }}
      </button>
    </div>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
    <div v-if="weather" class="weather-card">
      <h2 class="city-name">{{ weather.city }}</h2>
      <div class="weather-main">
        <div class="temperature">
          {{ Math.floor(weather.temperature)}}°C
        </div>
        <div class="description">
          {{ weather.description }}
        </div>
      </div>

      <div class="weather-details">
        <div class="detail-item">
          <span class="label">Humidity:</span>
          <span class="value">{{ weather.humidity }}</span>
        </div>
      </div>

      <div class="timestamp">
        Обновлено: {{ formatDate(weather.timestamp) }}
      </div>

      <div class="actions">
        <button @click="viewTrends" class="action-btn">
          📊 Trends
        </button>
        <button @click="viewHistory" class="action-btn">
          📜 History
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.title {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
  font-size: 2.5rem;
}

.search-section {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.city-input {
  flex: 1;
  padding: 12px 16px;
  font-size: 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  transition: border-color 0.3s;
}

.city-input:focus {
  outline: none;
  border-color: #42b983;
}

.search-btn {
  padding: 12px 24px;
  font-size: 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-btn:hover:not(:disabled) {
  background-color: #359268;
}

.search-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.error-message {
  padding: 12px;
  background-color: #fee;
  color: #c33;
  border-radius: 8px;
  margin-bottom: 20px;
  text-align: center;
}

.weather-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.city-name {
  text-transform: capitalize;
  text-align: center;
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 2rem;
}

.weather-main {
  text-align: center;
  margin-bottom: 30px;
}

.temperature {
  font-size: 4rem;
  font-weight: bold;
  color: #42b983;
}

.description {
  font-size: 1.5rem;
  color: #666;
  text-transform: capitalize;
}

.weather-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
  margin-bottom: 20px;
}

.detail-item {
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
}

.label {
  color: #666;
  font-weight: 500;
}

.value {
  color: #2c3e50;
  font-weight: bold;
}

.timestamp {
  text-align: center;
  color: #999;
  font-size: 0.9rem;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 10px;
}

.action-btn {
  flex: 1;
  padding: 12px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.action-btn:hover {
  background-color: #2980b9;
}
</style>