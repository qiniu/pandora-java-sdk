import * as React from 'react'
import classnames from 'classnames'
import { observer } from 'mobx-react'
import { get } from 'lodash'
import { TagList, Selector } from '@qn-pandora/pandora-component'
import { Bind } from 'lodash-decorators'
import { ENotifyMode } from '../../constants'
import { IMethodItem, defaultMethodItem } from '../constants'

import style from './style.module.less'

const { TagSwitch } = TagList

export interface IItemProps {
  title?: string
  icon: React.ReactElement
  source: any
  value?: IMethodItem
  readonly?: boolean
  isLast?: boolean
  onView?: (id: string, notifyNode: ENotifyMode) => void
  onChange?: (value: IMethodItem, source: any) => void
  templateOptions?: IKeyValues<string>
  hideTempSelector?: boolean
}

@observer
export default class Item extends React.Component<IItemProps, any> {
  @Bind
  handleActiveChange(active: boolean) {
    const { source, value, onChange } = this.props
    const { template } = value || defaultMethodItem
    if (onChange) {
      onChange({ active, template }, source)
    }
  }

  @Bind
  handleTemplateChange(template: string) {
    const { source, value, onChange } = this.props
    const { active } = value || defaultMethodItem
    if (onChange) {
      onChange({ active, template }, source)
    }
  }

  @Bind
  handleView() {
    const { onView, value, source } = this.props
    const id = get(value, 'template') || ''
    if (onView) {
      onView(id, source)
    }
  }

  render() {
    const {
      title,
      icon,
      value,
      hideTempSelector,
      onView,
      readonly,
      templateOptions
    } = this.props
    const { active, template } = value || defaultMethodItem
    const renderIcon = React.cloneElement(icon, {
      className: classnames(icon.props.className, style.icon, {
        [style.active]: active && !readonly,
        [style.readonly]: readonly
      })
    })

    if (!active && readonly) {
      return ''
    }

    return (
      <div
        className={classnames(style.root, { [style.last]: this.props.isLast })}
      >
        {!readonly && (
          <TagSwitch
            type="primary"
            className={classnames(style.tag, { [style.activeTag]: active })}
            title={title}
            active={active}
            onChange={this.handleActiveChange}
          >
            {renderIcon}
          </TagSwitch>
        )}
        {readonly && renderIcon}
        {!hideTempSelector && !readonly && (
          <Selector
            className={classnames(style.selector, {
              [style.disabled]: !active
            })}
            value={template}
            onChange={this.handleTemplateChange as any}
            options={this.props.templateOptions}
          />
        )}
        {readonly && <span>{get(templateOptions, template)}</span>}
        {onView && active && (
          <span className={style.view} onClick={this.handleView}>
            查看
          </span>
        )}
      </div>
    )
  }
}
