import React, { Component } from 'react'
import './global/styles/style.less'

interface IAppProps {
  context: {
    appName: string
    basePath: string
  }
}

export default class App extends Component<IAppProps, {}> {
  render() {
    return <div>hh</div>
  }
}
