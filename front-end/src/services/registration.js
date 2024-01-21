import defaultAxios from '../plugins/defaultAxios'

export default {
  register: (userInfo) => {
    return new Promise((resolve, reject) => {
      defaultAxios.post('/register', userInfo)
        .then(({ data }) => {
          resolve(data)
        })
        .catch((error) => {
          reject(error.response.data)
        })
    })
  }
}

