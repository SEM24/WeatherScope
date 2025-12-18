# WeatherScope Frontend

A Vue.js 3 application for visualizing weather data with interactive charts and trends analysis.

## Technology Stack

- Vue.js 3
- Vue Router 4
- Axios
- Chart.js
- Vue-ChartJS
- Vite
- Composition API

## Features

- Search and display current weather for any city
- View detailed weather history
- Visualize weather trends with interactive charts
- Analyze temperature and humidity patterns
- Calculate statistics (average, min, max)
- Responsive design for mobile and desktop

## Prerequisites

- Node.js 20.19.0+ or 22.12.0+
- npm or yarn
- WeatherScope Backend running on `http://localhost:8080`

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/SEM24/WeatherScope.git
cd weatherscope-frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Configure API endpoint

Edit `src/services/weatherApi.js` if your backend runs on a different port:

```javascript
const API_BASE_URL = 'http://localhost:8080'; // Change if needed
```

### 4. Run development server

```bash
npm run dev
```

The application will start on `http://localhost:5173`

## Project Structure

```
src/
├── views/
│   ├── HomeView.vue          # Main search and current weather
│   ├── TrendsView.vue        # Weather trends with charts
│   └── HistoryView.vue       # Complete weather history
├── services/
│   └── weatherApi.js         # API integration layer
├── router/
│   └── index.js              # Vue Router configuration
├── App.vue                   # Root component
└── main.js                   # Application entry point
```

## Available Scripts

### Development

```bash
npm run dev
```

Runs the app in development mode with hot-reload at `http://localhost:5173`

### Production Build

```bash
npm run build
```

Builds the app for production to the `dist` folder.

### Preview Production Build

```bash
npm run preview
```

Locally preview the production build.

## Application Features

### Home Page

Search for weather data by city name and view current conditions.

**Screenshot placeholder:**
```

```

Features:
- City name search with autocomplete
- Current temperature display
- Humidity and weather description
- Navigation to trends and history

### Trends View

Visualize weather patterns over selected time periods.

**Screenshot placeholder:**
```
[Trends page with interactive chart]
```

Features:
- Period selection (7, 14, 30, 90 days)
- Interactive dual-axis chart (temperature and humidity)
- Statistical summaries (average, min, max)
- Hover tooltips for detailed data points

### History View

Browse complete weather records in table format.

**Screenshot placeholder:**
```
[History page with data table]
```

Features:
- Chronological weather records
- Sortable data table
- Date and time formatting
- Record count statistics

## API Integration

The frontend communicates with the backend through these endpoints:

```javascript
// Get current weather
weatherApi.getCurrentWeather(city)

// Get average weather
weatherApi.getAverageWeather(city, days)

// Get weather trends
weatherApi.getTrends(city, days)

// Get weather history
weatherApi.getHistory(city)
```

## Configuration

### API Base URL

Located in `src/services/weatherApi.js`:

```javascript
const API_BASE_URL = 'http://localhost:8080';
```

### Router Configuration

Located in `src/router/index.js`:

```javascript
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/trends/:city',
    name: 'trends',
    component: TrendsView,
    props: true
  },
  {
    path: '/history/:city',
    name: 'history',
    component: HistoryView,
    props: true
  }
];
```

## Styling

The application uses scoped CSS with custom styling:

- Gradient background
- Card-based layouts
- Responsive grid system
- Hover effects and transitions
- Mobile-first approach

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### CORS Errors

If you encounter CORS errors, ensure your backend has CORS enabled:

```java
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class WeatherController {
    // ...
}
```

### Connection Refused

Verify the backend is running on `http://localhost:8080`:

```bash
curl http://localhost:8080/London
```

### Chart Not Rendering

Ensure Chart.js is properly installed:

```bash
npm install chart.js vue-chartjs
```

## Production Deployment

### Build for Production

```bash
npm run build
```

### Deploy to Static Hosting

The `dist` folder can be deployed to:
- Netlify
- Vercel
- GitHub Pages
- AWS S3
- Any static hosting service

### Environment Variables

For production, set the API URL via environment variables:

Create `.env.production`:

```
VITE_API_BASE_URL=https://your-api-domain.com
```

Update `weatherApi.js`:

```javascript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
```

## Dependencies

```json
{
  "dependencies": {
    "axios": "^1.13.2",
    "chart.js": "^4.5.1",
    "vue": "^3.5.22",
    "vue-chartjs": "^5.3.3",
    "vue-router": "^4.6.4"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^6.0.1",
    "vite": "^7.1.11",
    "vite-plugin-vue-devtools": "^8.0.3"
  }
}
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.