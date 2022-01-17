const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const { srcPath } = require('./paths')

module.exports = {
  resolve: {
    modules: [srcPath, 'node_modules'],
    extensions: ['.tsx', '.ts', '.jsx', '.js']
  },
  target: 'web',
  node: { fs: 'empty' },
  module: {
    rules: [
      {
        test: /\.mjs$/,
        include: /node_modules/,
        type: 'javascript/auto'
      },
      {
        test: /\.[jt]sx?$/,
        loader: 'ts-loader',
        include: srcPath
      },
      {
        test: /\.svg$/,
        use: ['@svgr/webpack', 'url-loader']
      },
      {
        test: /\.less$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
            options: {
              importLoaders: 2,
              sourceMap: false,
              localsConvention: 'camelCase',
              modules: {
                auto: true,
                localIdentName: '[local]_[hash:base64:5]'
              }
            }
          },
          {
            loader: 'less-loader',
            options: {
              lessOptions: {
                javascriptEnabled: true
              }
            }
          }
        ],
        sideEffects: true
      },
      {
        test: /\.css$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
            options: {
              importLoaders: 2,
              sourceMap: false,
              modules: false,
              localsConvention: 'camelCase'
            }
          }
        ],
        sideEffects: true
      }
    ]
  }
}
