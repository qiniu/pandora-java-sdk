import React from 'react'
import ReactDom from 'react-dom'
import { Bind } from 'lodash-decorators'
import { ConfigProvider } from 'antd'
import BaseInput from '../../component/baseInput'
import NoticeMethods from './NoticeMethods'

import '../../global/styles/style.less'

export default class MethodInput extends BaseInput {
  @Bind
  getPopupContainer() {
    return this.element
  }

  @Bind
  async render(props: any) {
    if (this.element) {
      ReactDom.render(
        <ConfigProvider getPopupContainer={this.getPopupContainer}>
          <NoticeMethods
            readonly={props.readonly}
            onChange={props.onChange}
            value={props.value}
          />
        </ConfigProvider>,
        this.element
      )
    }
  }
}
