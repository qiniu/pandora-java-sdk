const webpack = require('webpack')
const { dllPath, dllManifestPath } = require('./paths')

module.exports = {
  mode: 'production',
  entry: {
    vendor: ['react', 'react-dom', 'lodash', 'mobx', 'mobx-react']
  },
  output: {
    path: dllPath,
    filename: '[name]_[hash].dll.js',
    library: '[name]_[hash]',
    libraryTarget: 'umd'
  },
  plugins: [
    new webpack.DllPlugin({
      context: dllPath,
      path: dllManifestPath,
      name: '[name]_[hash]'
    })
  ]
}
