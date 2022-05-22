import {createRouter, createWebHistory} from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/user',
        children: [
            {
                path: '/user',
                name: 'User',
                component: () => import("@/views/User"),
            },
            {
                path: '/book',
                name: 'Book',
                component: () => import("@/views/Book"),
            },
            {
              path: "/news",
              name: "News",
              component: () => import("@/views/News"),
            },
            {
                path: '/person',
                name: 'Person',
                component: () => import("@/views/Person"),
            },
            {
                path: '/chat',
                name: 'Chat',
                component: () => import("@/views/Chat"),
            },
            {
                path: '/file',
                name: 'File',
                component: () => import("@/views/File"),
            },
            {
                path: '/player',
                name: 'Player',
                component: () => import("@/views/Player")
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/Register")
    },
    {
        path: '/videoDetail',
        name: 'VideoDetail',
        component: () => import("@/views/VideoDetail")
    },
    {
        path: '/video',
        name: 'Video',
        component: () => import("@/views/Video")
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
