const fs = require('fs')
const path = require('path')
const webpack = require('webpack')
const { merge } = require('webpack-merge')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const { srcPath, libPath } = require('./paths')
const commonConfig = require('./webpack.common.conf')

const proConfig = {
  mode: 'production',
  context: srcPath,
  entry: fs.readdirSync('src/modules').reduce((prev, current) => {
    prev[current] = `modules/${current}/index.tsx`
    return prev
  }, {}),
  output: {
    path: libPath,
    filename: '[name].js',
    libraryTarget: 'umd'
  },
  externals: {
    react: 'react',
    'react-dom': 'react-dom',
    '@qn-pandora/visualization-sdk': '@qn-pandora/visualization-sdk',
    '@qn-pandora/pandora-visualization': '@qn-pandora/pandora-visualization'
  },
  module: {
    rules: [
      {
        test: /\.(png|eot|ttf|woff|woff2)$/,
        loader: 'url-loader'
      }
    ]
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: '[name].css'
    }),
    new webpack.DefinePlugin({
      IS_ENV_PRODUCTION: true
    })
  ]
}
module.exports = merge(commonConfig, proConfig)
