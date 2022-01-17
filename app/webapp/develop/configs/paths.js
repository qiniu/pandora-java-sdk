const path = require('path')
const projectPath = path.resolve(__dirname, '../')

const srcPath = path.resolve(projectPath, './src')
const libPath = path.resolve(projectPath, './lib')
const dllPath = path.resolve(projectPath, './node_modules/.dll/')
const dllManifestPath = path.resolve(dllPath, './manifest.json')
const appPlatformPath = path.resolve(projectPath, './node_modules/@qn-pandora/app-platform/lib')

module.exports = {
  srcPath,
  libPath,
  dllPath,
  dllManifestPath,
  appPlatformPath
}
