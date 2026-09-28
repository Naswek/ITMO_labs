import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

export default defineConfig({
  plugins: [react()],
  base: './',
  server: {
    proxy: {
      '/api': {
        target: process.env.VITE_BACKEND_ORIGIN || 'http://localhost:8080',
        changeOrigin: true,
        cookiePathRewrite: '/',
        rewrite: (path) => `${process.env.VITE_BACKEND_CONTEXT || '/labworks'}${path}`,
      },
    },
  },
})
