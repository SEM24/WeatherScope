<script setup>
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import weatherApi from "@/services/weatherApi.js";

const router = useRouter();
const route = useRoute();

const city = ref(route.params.city);
const history = ref([]);
const loading = ref(false);
const error = ref(null);

const loadHistory = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await weatherApi.getHistory(city.value);
    history.value = response.data;
  } catch (err) {
    error.value = err.response?.data?.message || "Can't load the history on city";
  } finally {
    loading.value = false;
  }
};
const formatDate = (timestamp) => {
  return new Date(timestamp).toLocaleString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};
const goBack = () => {
  router.push({name: 'home'});
};
onMounted(() => {
  loadHistory();
});
</script>

<template>
  <div class="history-container">
    <div class="header">
      <button @click="goBack" class="back-btn">< Back</button>
      <h1 class="title">Weather history: {{ city }}</h1>
    </div>
    <div v-if="loading" class="loading">
      Loading...
    </div>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
    <div v-if="!loading && history.length === 0" class="empty-message">
      History for this city in empty
    </div>

    <div v-if="!loading && history.length > 0" class="history-content">
      <div class="stats-card">
        <div class="stat-item">
          <span class="stat-label">Total amount:</span>
          <span class="stat-value">{{ history.length }}</span>
        </div>
      </div>

      <div class="table-wrapper">
        <table class="history-table">
          <thead>
          <tr>
            <th>Date and Time</th>
            <th>Temperature</th>
            <th>Humidity</th>
            <th>Description</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="record in history" :key="record.id">
            <td class="date-cell">{{ formatDate(record.timestamp) }}</td>
            <td class="temp-cell">
              <span class="temperature">{{ record.temperature }}°C</span>
            </td>
            <td class="humidity-cell">{{ record.humidity }}%</td>
            <td class="description-cell">{{ record.description }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.history-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.back-btn {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #359268;
}

.title {
  color: #2c3e50;
  font-size: 2rem;
  text-transform: capitalize;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 1.2rem;
  color: #666;
}

.error-message {
  padding: 12px;
  background-color: #fee;
  color: #c33;
  border-radius: 8px;
  margin-bottom: 20px;
  text-align: center;
}

.empty-message {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 16px;
  color: #666;
  font-size: 1.2rem;
}

.history-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 1.1rem;
  color: #666;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #42b983;
}

.table-wrapper {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.history-table {
  width: 100%;
  border-collapse: collapse;
}

.history-table thead {
  background-color: #f5f5f5;
}

.history-table th {
  padding: 15px;
  text-align: left;
  font-weight: 600;
  color: #2c3e50;
  border-bottom: 2px solid #e0e0e0;
}

.history-table tbody tr {
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;
}

.history-table tbody tr:hover {
  background-color: #f9f9f9;
}

.history-table td {
  padding: 15px;
  color: #666;
}

.date-cell {
  font-weight: 500;
  color: #2c3e50;
}

.temp-cell {
  font-weight: bold;
}

.temperature {
  color: #42b983;
  font-size: 1.1rem;
}

.humidity-cell {
  color: #3498db;
}

.description-cell {
  text-transform: capitalize;
  font-style: italic;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  .title {
    font-size: 1.5rem;
  }

  .table-wrapper {
    overflow-x: auto;
  }

  .history-table {
    min-width: 600px;
  }
}
</style>