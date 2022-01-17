import React from 'react'
import { observer } from 'mobx-react'
import { Empty, Spin } from 'antd'
import { BasicComponent } from '@qn-pandora/app-sdk'
import { Modal } from '@qn-pandora/pandora-component'
import { ENotifyMode } from '../../constants'
import MarkdownViewer from './MarkdownViewer'

import styles from './style.module.less'

@observer
export default class DetailModal extends BasicComponent<
  {
    templateContent?: string
    notifyMode?: ENotifyMode
    onClose?: () => void
    loading?: boolean
  },
  any
> {
  render() {
    const { templateContent, onClose, loading } = this.props
    const content = loading ? (
      <Spin spinning={loading}>
        <div className={styles.flex} />
      </Spin>
    ) : templateContent ? (
      <MarkdownViewer text={templateContent} className={styles.disabledClick} />
    ) : (
      <div className={styles.flex}>
        <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
      </div>
    )
    return (
      <Modal
        width={this.props.notifyMode === ENotifyMode.EMAIL ? '824px' : '560px'}
        footer={null}
        visible={true}
        onCancel={onClose}
        wrapClassName={styles.wrapper}
      >
        {content}
      </Modal>
    )
  }
}
