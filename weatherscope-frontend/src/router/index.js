import {createRouter, createWebHistory} from "vue-router";
import HomeView from "@/views/HomeView.vue";

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView,
    },
    {
        path: '/trends/:city',
        name: 'trends',
        component: () => import('@/views/TrendsView.vue'),
        props: true
    },
    {
        path: '/history/:city',
        name: 'history',
        component: () => import('@/views/HistoryView.vue'),
        props: true,
    }
];
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
});
export default router;