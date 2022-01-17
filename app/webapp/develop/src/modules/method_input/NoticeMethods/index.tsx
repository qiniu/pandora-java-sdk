import React from 'react'
import { observer } from 'mobx-react'
import { Bind } from 'lodash-decorators'
import { toString } from 'lodash'
import { Spin } from 'antd'
import { TagList } from '@qn-pandora/pandora-component'
import { Icon } from '@qn-pandora/pandora-component-icons'
import { ITemplate } from 'apis/template/model'
import {
  getTemplate,
  listEmailTemplates,
  listSmsTemplates
} from 'apis/template'
import { Loadings } from '@qn-pandora/app-sdk'
import { IMethod, IMethodItem, defaultMethod } from './constants'
import Item from './Item'
import { ENotifyMode } from '../constants'
import { action, observable } from 'mobx'
import DetailModal from './DetailModal'

interface IMethondProps {
  value?: IMethod
  readonly?: boolean
  onView?: (name: string, notifyNode: ENotifyMode) => void
  onChange?: (value: IMethod) => void
}

const loadings = new Loadings()

@observer
export default class NoticeMethods extends React.Component<IMethondProps, any> {
  loadings = loadings

  @observable notifyNode: ENotifyMode = ENotifyMode.EMAIL
  @observable isDetail: boolean = false
  @observable template: string = ''
  @observable.ref templateOptions: IKeyValues = {}
  @observable.ref templates: IKeyValues<ITemplate[]> = {}

  @Bind
  @action
  seIsDetail(isDetail: boolean) {
    this.isDetail = isDetail
  }

  @Bind
  @action
  setTemplates(templates: IKeyValues) {
    this.templates = templates
  }

  @Bind
  @action
  setTemplateOptions(templateOptions: IKeyValues) {
    this.templateOptions = templateOptions
  }

  @Bind
  @action
  setTemplate(template: string) {
    this.template = template
  }

  @Bind
  handleMethodsChange(item: IMethodItem, source: string) {
    const { value = defaultMethod, onChange } = this.props
    if (onChange) {
      onChange({
        ...value,
        [source]: item
      })
    }
  }

  @Bind
  @action
  seNotifyMode(notifyNode: ENotifyMode) {
    this.notifyNode = notifyNode
  }

  @Bind
  @loadings.handle('list')
  async handleView(id: string, notifyNode: ENotifyMode) {
    this.seNotifyMode(notifyNode)
    const { data } = await getTemplate(id)
    this.seIsDetail(true)
    this.setTemplate(data.content)
  }

  @Bind
  @loadings.handle('list')
  async listSmsTemplates() {
    try {
      const data = await listSmsTemplates()
      return data.data
    } catch (error) {
      console.error(error)
      return Promise.resolve([])
    }
  }

  @Bind
  @loadings.handle('list')
  async listEmailTemplates() {
    try {
      const data = await listEmailTemplates()
      return data.data
    } catch (error) {
      console.error(error)
      return Promise.resolve([])
    }
  }

  @Bind
  async init() {
    const res: { [key in ENotifyMode]: IKeyValues<string> } = {
      [ENotifyMode.EMAIL]: {},
      [ENotifyMode.SMS]: {}
    }
    const sms = (await this.listSmsTemplates()) || []
    sms.map((item: any) => {
      const { id, name } = item
      res[ENotifyMode.SMS][toString(id)] = name
    })
    const email = (await this.listEmailTemplates()) || []
    email.map((item: any) => {
      const { id, name } = item
      res[ENotifyMode.EMAIL][toString(id)] = name
    })
    this.setTemplateOptions(res)
    this.setTemplates({ [ENotifyMode.SMS]: sms, [ENotifyMode.EMAIL]: email })
  }

  componentDidMount() {
    this.init()
  }

  render() {
    const { value = defaultMethod, readonly } = this.props
    return (
      <TagList>
        <Spin spinning={this.loadings.isLoading('list')}>
          <Item
            onView={this.handleView}
            readonly={readonly}
            title="短信"
            icon={<Icon type="message" />}
            source={ENotifyMode.SMS}
            value={value[ENotifyMode.SMS]}
            onChange={this.handleMethodsChange}
            templateOptions={this.templateOptions[ENotifyMode.SMS] as any}
            isLast={value.sms.active && !value.email.active && readonly}
          />
          <Item
            onView={this.handleView}
            readonly={readonly}
            title="邮箱"
            icon={<Icon type="mail" />}
            source={ENotifyMode.EMAIL}
            value={value[ENotifyMode.EMAIL]}
            onChange={this.handleMethodsChange}
            templateOptions={this.templateOptions[ENotifyMode.EMAIL] as any}
            isLast={value.email.active}
          />
        </Spin>
        {this.isDetail && (
          <DetailModal
            templateContent={this.template}
            notifyMode={this.notifyNode}
            onClose={() => {
              this.seIsDetail(false)
              this.setTemplate('')
            }}
          />
        )}
      </TagList>
    )
  }
}
