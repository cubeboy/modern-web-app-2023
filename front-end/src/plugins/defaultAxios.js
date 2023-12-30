import axios from 'axios'

const defaultAxios = axios.create(
  {
    baseURL: '/api'
  }
)
defaultAxios.defaults.headers.common.Accept = 'application/json'
defaultAxios.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error)
  }
)

export default defaultAxios
