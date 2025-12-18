<script setup>
import {computed, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import weatherApi from "@/services/weatherApi.js";
import {Line} from 'vue-chartjs';
import {
  CategoryScale,
  Chart as ChartJS,
  Filler,
  Legend,
  LinearScale,
  LineElement,
  PointElement,
  Title,
  Tooltip
} from 'chart.js';

const router = useRouter();
const route = useRoute();

const city = ref(route.params.city);
const days = ref(7);
const trends = ref([]);
const loading = ref(false);
const error = ref(null);

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Filler
);

const periodOptions = [
  {value: 2, label: '2 days'},
  {value: 7, label: '7 days'},
  {value: 14, label: '14 days'},
  {value: 30, label: '30 days'},
  {value: 90, label: '90 days'}
];

const loadTrends = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await weatherApi.getTrends(city.value, days.value);
    trends.value = response.data;
  } catch (err) {
    error.value = err.response?.data?.message || "Couldn't load weather trends.";
  } finally {
    loading.value = false;
  }
};
const formatDate = (timestamp) => {
  return new Date(timestamp).toLocaleDateString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
  });
};
const statistics = computed(() => {
  if (trends.value.length === 0) return null;
  const temps = trends.value.map(t => t.temperature);
  const humidities = trends.value.map(t => t.humidity);

  return {
    avgTemp: (temps.reduce((a, b) => a + b, 0) / temps.length).toFixed(1),
    minTemp: Math.min(...temps).toFixed(1),
    maxTemp: Math.max(...temps).toFixed(1),
    avgHumidity: (humidities.reduce((a, b) => a + b, 0) / humidities.length).toFixed(1)
  };
});

const chartData = computed(() => {
  const sortedTrends = [...trends.value].sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));
  return {
    labels: sortedTrends.map(t => formatDate(t.timestamp)),
    datasets: [
      {
        label: 'Temperature (°C)',
        data: sortedTrends.map(t => t.temperature),
        borderColor: '#42b983',
        backgroundColor: 'rgba(66, 185, 131, 0.1)',
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6
      },
      {
        label: 'Humidity (%)',
        data: sortedTrends.map(t => t.humidity),
        borderColor: '#3498db',
        backgroundColor: 'rgba(52, 152, 219, 0.1)',
        tension: 0.4,
        fill: true,
        pointRadius: 4,
        pointHoverRadius: 6,
        yAxisID: 'y1'
      }
    ]
  };
});
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  interaction: {
    mode: 'index',
    intersect: false
  },
  plugins: {
    legend: {
      position: 'top',
      labels: {
        padding: 15,
        font: {
          size: 14
        }
      }
    },
    tooltip: {
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      padding: 12,
      titleFont: {
        size: 14
      },
      bodyFont: {
        size: 13
      }
    }
  },
  scales: {
    y: {
      type: 'linear',
      display: true,
      position: 'left',
      title: {
        display: true,
        text: 'Temperature (°C)',
        font: {
          size: 12
        }
      }
    },
    y1: {
      type: 'linear',
      display: true,
      position: 'right',
      title: {
        display: true,
        text: 'Humidity (%)',
        font: {
          size: 12
        }
      },
      grid: {
        drawOnChartArea: false
      }
    }
  }
};

const changePeriod = (newDays) => {
  days.value = newDays;
  loadTrends();
};

const goBack = () => {
  router.push({name: 'home'});
};

onMounted(() => {
  loadTrends();
});
</script>

<template>
  <div class="trends-container">
    <div class="header">
      <button @click="goBack" class="back-btn">< Back</button>
      <h1 class="title">Weather trends: {{ city }}</h1>
    </div>
    <div class="period-selector">
      <button v-for="option in periodOptions"
              :key="option.value"
              @click="changePeriod(option.value)"
              :class="['period-btn', {active: days===option.value}]"
              :disabled="loading">{{ option.label }}
      </button>
    </div>
    <div v-if="loading" class="loading">
      Loading data...
    </div>
    <div v-if="error" class="error-message">{{ error }}</div>
    <div v-if="!loading && trends.length === 0" class="empty-message">
      No data for chosen period of time
    </div>
    <div v-if="!loading && trends.length > 0" class="trends-content">
      <!-- Статистика -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">📊</div>
          <div class="stat-info">
            <div class="stat-label">Average temperature</div>
            <div class="stat-value">{{ statistics.avgTemp }}°C</div>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">🔥</div>
          <div class="stat-info">
            <div class="stat-label">Maximum</div>
            <div class="stat-value">{{ statistics.maxTemp }}°C</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">❄️</div>
          <div class="stat-info">
            <div class="stat-label">Minimum</div>
            <div class="stat-value">{{ statistics.minTemp }}°C</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">💧</div>
          <div class="stat-info">
            <div class="stat-label">Average humidity</div>
            <div class="stat-value">{{ statistics.avgHumidity }}%</div>
          </div>
        </div>
      </div>
    </div>
    <div class="chart-card">
      <h2 class="chart-title">Weather changing graph</h2>
      <div class="chart-wrapper">
        <Line :data="chartData" :options="chartOptions"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.trends-container {
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

.period-selector {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.period-btn {
  padding: 10px 20px;
  background-color: white;
  color: #2c3e50;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.3s;
}

.period-btn:hover:not(:disabled) {
  border-color: #42b983;
  color: #42b983;
}

.period-btn.active {
  background-color: #42b983;
  color: white;
  border-color: #42b983;
}

.period-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

.trends-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  font-size: 2.5rem;
}

.stat-info {
  flex: 1;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 5px;
}

.stat-value {
  color: #2c3e50;
  font-size: 1.5rem;
  font-weight: bold;
}

.chart-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.chart-title {
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 1.5rem;
}

.chart-wrapper {
  height: 400px;
  position: relative;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  .title {
    font-size: 1.5rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .chart-wrapper {
    height: 300px;
  }
}
</style>