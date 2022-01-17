const path = require('path')
const webpack = require('webpack')
const AddAssetHtmlWebpackPlugin = require('add-asset-html-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const { merge } = require('webpack-merge')
const {
  srcPath,
  libPath,
  dllPath,
  dllManifestPath,
  appPlatformPath
} = require('./paths')
const commonConfig = require('./webpack.common.conf')
const {
  proxy,
  appName,
  account: { username, password }
} = require('./env')

const devConfig = {
  mode: 'development',
  context: srcPath,
  devtool: 'eval-source-map',
  entry: {
    index: path.join(srcPath, 'platform.tsx')
  },
  output: {
    path: libPath,
    libraryTarget: 'umd',
    publicPath: '/',
    filename: '[name]_[hash].js'
  },
  module: {
    rules: [
      {
        test: /\.(png|eot|ttf|woff|woff2)$/,
        loader: 'url-loader',
        options: {
          // 将 image 抽离出来，加快 development 模式下打包速度
          limit: 8192
        }
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: '[name]_[hash].css'
    }),
    new HtmlWebpackPlugin({
      title: appName
    }),
    new webpack.DefinePlugin({
      IS_ENV_PRODUCTION: false,
      USERNAME: JSON.stringify(username),
      PASSWORD: JSON.stringify(password)
    }),
    new webpack.DllReferencePlugin({
      context: dllPath,
      manifest: dllManifestPath
    }),
    new AddAssetHtmlWebpackPlugin([
      {
        filepath: path.resolve(dllPath, '*.js')
      },
      {
        filepath: path.join(appPlatformPath, 'index.css'),
        typeOfAsset: 'css',
        outputPath: 'appPlatform',
        publicPath: '/appPlatform'
      },
      {
        filepath: path.join(appPlatformPath, 'index.js'),
        outputPath: 'appPlatform',
        publicPath: '/appPlatform'
      }
    ])
  ],
  devServer: {
    host: '0.0.0.0',
    port: 8000,
    disableHostCheck: true,
    hot: true,
    compress: true,
    clientLogLevel: 'none',
    //    watchContentBase: true,
    //    quiet: true
    overlay: false,
    historyApiFallback: {
      disableDotRule: true
    },
    proxy
  }
}

module.exports = merge(commonConfig, devConfig)
