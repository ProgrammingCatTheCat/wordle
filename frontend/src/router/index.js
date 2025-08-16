import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Wordle',
      name: 'Wordle',
      component: () => import('../components/Wordle.vue') // use Main.vue as Main page
    },
  ],
})

export default router
