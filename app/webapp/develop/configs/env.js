module.exports = {
  appName: 'customNotice',
  account: {
    username: 'pandora@qiniu.com',
    password: '12345678'
  },
  proxy: {
    '/api': {
      target: 'http://cs20:9220',
      changeOrigin: true
    }
  }
}
