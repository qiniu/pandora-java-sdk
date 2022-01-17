import * as React from 'react'
import * as ReactDOM from 'react-dom'
import { LoginManager, ELoginType } from '@qn-pandora/app-sdk'
import App from './app'

const div = document.createElement('div')
document.body.append(div)

async function init() {
  if (!IS_ENV_PRODUCTION) {
    global['__Pandora__SetupAppPlatform']()
    const loginManager = new LoginManager(ELoginType.PANDORA)
    await loginManager.login({
      username: USERNAME,
      password: PASSWORD
    })
  }
  // provide context
  const context = {
    appName: '', // 当前页面所属的app
    basePath: '', // 当前页面的base路径
    isEditing: '' // 当前页面是否处于编辑态
  }
  ReactDOM.render(<App context={context} />, div)
}

init()
