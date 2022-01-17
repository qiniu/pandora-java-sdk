import React from 'react'
import ReactDom from 'react-dom'
import { action, observable } from 'mobx'
import { get } from 'lodash'
import { Bind } from 'lodash-decorators'
import classnames from 'classnames'
import { ConfigProvider } from 'antd'
import { appEnv, utils } from '@qn-pandora/app-sdk'
import { Selector } from '@qn-pandora/pandora-component'
import BaseInput from '../../component/baseInput'

import '../../global/styles/style.less'
import style from './style.module.less'

const { fetch } = utils

interface IOption {
  text: string
  value: string
}

export default class UserInput extends BaseInput {
  @observable.ref options: IOption[] = []
  @observable tenanatid: string = ''
  @observable isFirst: boolean = true

  @Bind
  @action
  setOptions(options: IOption[]) {
    this.options = options
  }

  @Bind
  @action
  setTenanatid(tenanatid: string) {
    this.tenanatid = tenanatid
  }

  @Bind
  @action
  setIsFirst(isFirst: boolean) {
    this.isFirst = isFirst
  }

  @Bind
  async listApiOptions() {
    if (this.tenanatid) {
      try {
        const data: any = await fetch(
          `${appEnv.basepath}/pandora/custom/v1/src_trigger/tenants/users?tenantid=${this.tenanatid} `
        )
        const options = get(data, 'data') || []
        this.setOptions(
          options.map((item: any) => ({
            text: item.name,
            value: item.id
          }))
        )
      } catch (error) {
        console.error(error)
      }
    }
  }

  @Bind
  getPopupContainer() {
    return this.element
  }

  @Bind
  async render(props: any) {
    const tenanatid = get(props.values, 'tenanatid')
    let value = props.value
    if (tenanatid !== this.tenanatid) {
      this.setTenanatid(tenanatid)
      if (!this.isFirst) {
        value = []
        props.onChange([])
      }
      await this.listApiOptions()
    }
    if (this.element) {
      this.setIsFirst(false)
      ReactDom.render(
        <ConfigProvider getPopupContainer={this.getPopupContainer}>
          <Selector
            onChange={props.onChange}
            mode="multiple"
            options={this.options}
            value={value}
            disabled={props.readonly}
            className={classnames({ [style.readonly]: props.readonly })}
          />
        </ConfigProvider>,
        this.element
      )
    }
  }
}
